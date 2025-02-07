package com.moda.moda_api.card.application.service;

import com.moda.moda_api.card.application.mapper.CardDtoMapper;
import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.domain.*;
import com.moda.moda_api.card.exception.CardNotFoundException;
import com.moda.moda_api.card.presentation.request.MoveCardRequest;
import com.moda.moda_api.card.presentation.request.UpdateCardRequest;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.common.pagination.SliceRequestDto;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.search.domain.CardSearchRepository;
import com.moda.moda_api.summary.application.service.LilysSummaryService;
import com.moda.moda_api.summary.infrastructure.api.LilysAiClient;
import com.moda.moda_api.user.domain.UserId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CardService {
	private final CardRepository cardRepository;
	private final CardFactory cardFactory;
	private final CardDtoMapper cardDtoMapper;
	private final LilysSummaryService lilysSummaryService;
	private final EmbeddingApiClient embeddingApiClient;
	private final UrlCacheRepository urlCacheRepository;
	private final LilysAiClient lilysAiClient;

	/**
	 * URL을 입력 받고 새로운 카드 생성 후 알맞은 보드로 이동합니다.
	 * @param userId
	 * @param url
	 * @return
	 */
	@Transactional
	public CompletableFuture<Boolean> createCard(String userId, String url) {
		UserId userIdObj = new UserId("01234");
		String urlHash = UrlCache.generateHash(url);

		return urlCacheRepository.findByUrlHash(urlHash)
			.map(cache -> createCardFromCache(userIdObj, urlHash))    // url Hash가 있다면 기존에 있던것을 실행
			.orElseGet(() -> createNewCard(userIdObj, url, urlHash)); // url Hash가 없다면 새로 만들기
	}

	private CompletableFuture<Boolean> createCardFromCache(UserId userIdObj, String urlHash) {
		Card existingCard = cardRepository.findByUrlHash(urlHash).get();
		UrlCache cache = urlCacheRepository.findByUrlHash(urlHash).get();

		Card card = cardFactory.create(
			userIdObj,
			existingCard.getCategoryId(),
			existingCard.getTypeId(),
			urlHash,
			cache.getCachedTitle(),
			cache.getCachedContent(),
			existingCard.getThumbnailContent(),
			existingCard.getThumbnailUrl(),
			existingCard.getEmbedding()
		);

		cardRepository.save(card);
		return CompletableFuture.completedFuture(true);
	}

	/**
	 * 새로운 카드인 경우 urlCache테이블과 ElasticSearch에 모두 저장
	 * @param userIdObj
	 * @param url
	 * @param urlHash
	 * @return
	 */
	private CompletableFuture<Boolean> createNewCard(UserId userIdObj, String url, String urlHash) {
		return lilysSummaryService.summarize(url)
			.thenApply(summaryResponse -> {
				EmbeddingVector embeddingVector = embeddingApiClient.embedContent(summaryResponse.getContent());
				CategoryId categoryIdObj = new CategoryId(1L);

				Card card = cardFactory.create(
					userIdObj,
					categoryIdObj,
					summaryResponse.getTypeId(),
					urlHash,
					summaryResponse.getTitle(),
					summaryResponse.getContent(),
					summaryResponse.getThumbnailContent(),
					summaryResponse.getThumbnailUrl(),
					embeddingVector
				);

				urlCacheRepository.save(
					UrlCache.builder()
						.urlHash(urlHash)
						.cachedTitle(summaryResponse.getTitle())
						.cachedContent(summaryResponse.getContent())
						.originalUrl(url)
						.build()
				);

				cardRepository.save(card);
				return true;
			});
	}

	/**
	 * 페이지네이션에 맞게 카드의 리스트 반환합니다.
	 *
	 * 최대 15개의 카드를 반환하며 Slice 형식으로 다음 값이 있는지를 SliceInfo에 함께 반환
	 * @param userId
	 * @param categoryId
	 * @param page
	 * @param size
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public SliceResponseDto<CardListResponse> getCardList(
		String userId, Long categoryId, Integer page, Integer size, String sortBy, String sortDirection
	) {
		UserId userIdObj = new UserId(userId);
		CategoryId categoryIdObj = new CategoryId(categoryId);

		// Slice 값 생성
		SliceRequestDto sliceRequestDto = SliceRequestDto.builder()
			.page(page)
			.size(size)
			.sortBy(sortBy)
			.sortDirection(sortDirection)
			.build();

		// Slice와 파라미터 조건에 맞는 카드를 가져옵니다.
		Slice<Card> cards = cardRepository.findByUserIdAndCategoryId(
			userIdObj,
			categoryIdObj,
			sliceRequestDto.toPageable()
		);

		// 페이지네이션 메타 데이터와 함께 반환합니다.
		return SliceResponseDto.of(
			cards.map(cardDtoMapper::toResponse)
		);
	}

	/**
	 * 카드의 Detail 정보 반환
	 * @param userId
	 * @param cardId
	 * @return
	 */
	public CardDetailResponse getCardDetail(String userId, String cardId) {
		UserId userIdObj = new UserId(userId);
		CardId cardIdObj = new CardId(cardId);

		// 카드 탐색
		Card card = findCard(userIdObj, cardIdObj);
		return cardDtoMapper.toDetailResponse(card);
	}

	/**
	 * , 로 나뉜 String을 받고 입력으로 받은 모든 카드를 제거합니다.
	 * @param userId
	 * @param cardIds
	 * @return
	 */
	@Transactional
	public Boolean deleteCard(String userId, String cardIds) {
		UserId userIdObj = new UserId(userId);

		// ,로 구분된 cardId들을 Value Object로 매핑하며 id 검증
		List<CardId> cardIdList = toCardIds(Arrays.asList(cardIds.split(",")));

		// 제거할 CardId별 Card 가져오기
		List<Card> cardsToDelete = findCardList(userIdObj, cardIdList);

		// Card 삭제 권한 검증
		cardsToDelete.forEach(card -> card.validateOwnership(userIdObj));

		// 카드 삭제
		return cardRepository.deleteAll(cardsToDelete);
	}

	/**
	 * 카드의 Content를 변경합니다.
	 * @param userId
	 * @param request
	 * @return
	 */
	@Transactional
	public CardDetailResponse updateCardContent(String userId, UpdateCardRequest request) {
		UserId userIdObj = new UserId(userId);
		CardId cardIdObj = new CardId(request.getCardId());

		// Content를 변경할 카드 탐색
		Card card = findCard(userIdObj, cardIdObj);

		// Content 변경 권한 검증
		card.validateOwnership(userIdObj);

		// Card의 콘텐츠 변경 후 저장
		card.changeContent(request.getContent());
		cardRepository.save(card);

		return cardDtoMapper.toDetailResponse(card);
	}

	/**
	 * 입력으로 들어온 모든 카드를 BordId에 해당하는 보드로 옮깁니다.
	 * @param userId
	 * @param request
	 * @return
	 */
	@Transactional
	public Boolean updateCardBoard(String userId, MoveCardRequest request) {
		UserId userIdObj = new UserId(userId);
		CategoryId categoryIdObj = new CategoryId(request.getCategoryId());

		// 입력의 모든 CardId를 Value Object로 매핑
		List<CardId> cardIdList = toCardIds(request.getCardIdList());

		// CardId에 해당하는 모든 카드를 가져옴
		List<Card> cardsToMove = findCardList(userIdObj, cardIdList);

		// 각 카드의 변경 권한 검증
		cardsToMove.forEach(card -> card.validateOwnership(userIdObj));

		// 모든 카드를 입력으로 들어온 BoardId로 이동 후 저장
		cardsToMove.stream().forEach(card -> card.moveCategory(categoryIdObj));
		cardRepository.saveAll(cardsToMove);

		return true;
	}

	/**
	 * String으로 들어오는 모든 CardId 입력을 CardId Value Object로 변환하여 반환합니다.
	 * @param cardIds
	 * @return
	 */
	private List<CardId> toCardIds(List<String> cardIds) {
		return cardIds.stream()
			.map(CardId::new)
			.collect(Collectors.toList());
	}

	/**
	 * UserId와 CardId로 Card를 찾습니다.
	 * @param userId
	 * @param cardId
	 * @return
	 */
	private Card findCard(UserId userId, CardId cardId) {
		return cardRepository.findByUserIdAndCardId(userId, cardId)
			.orElseThrow(() -> new CardNotFoundException("카드를 찾을 수 없습니다."));
	}

	/**
	 * CardIdList에 존재하는 모든 Card를 찾습니다.
	 * @param cardIdList
	 * @return
	 */
	private List<Card> findCardList(UserId userId, List<CardId> cardIdList) {
		return cardIdList.stream()
			.map(cardId -> findCard(userId, cardId))
			.collect(Collectors.toList());
	}
}
