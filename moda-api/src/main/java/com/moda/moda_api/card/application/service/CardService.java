package com.moda.moda_api.card.application.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moda.moda_api.card.application.mapper.CardDtoMapper;
import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.domain.Card;
import com.moda.moda_api.card.domain.CardFactory;
import com.moda.moda_api.card.domain.CardId;
import com.moda.moda_api.card.domain.CardRepository;
import com.moda.moda_api.card.domain.EmbeddingVector;
import com.moda.moda_api.card.domain.UrlCache;
import com.moda.moda_api.card.domain.UrlCacheRepository;
import com.moda.moda_api.card.exception.CardNotFoundException;
import com.moda.moda_api.card.presentation.request.MoveCardRequest;
import com.moda.moda_api.card.presentation.request.UpdateCardRequest;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.common.infrastructure.ImageStorageService;
import com.moda.moda_api.common.pagination.SliceRequestDto;
import com.moda.moda_api.common.pagination.SliceResponseDto;

import com.moda.moda_api.search.domain.CardSearchRepository;
import com.moda.moda_api.summary.application.service.SummaryService;
import com.moda.moda_api.summary.infrastructure.api.PythonAiClient;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.user.domain.UserId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Slice;
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
	private final UrlCacheRepository urlCacheRepository;
	private final SummaryService summaryService;
	private final ImageStorageService imageStorageService;
	private final PythonAiClient pythonAiClient;
	private final CardSearchRepository cardSearchRepository;

	/**
	 * URL을 입력 받고 새로운 카드 생성 후 알맞은 보드로 이동합니다.
	 * @param userId
	 * @param url
	 * @return
	 */
	@Transactional
	public CompletableFuture<Boolean> createCard(String userId, String url) {
		UserId userIdObj = new UserId("user");
		String urlHash = UrlCache.generateHash(url);

		return urlCacheRepository.findByUrlHash(urlHash)
			.map(cache -> createCardFromCache(userIdObj, urlHash))
			.orElseGet(() -> {
				try {
					return createNewCard(userIdObj, url, urlHash);
				} catch (Exception e) {
					e.printStackTrace();
					return CompletableFuture.completedFuture(false); // 실패 시 false 반환
				}
			});
	}

	// UrlHash가 있는 경우
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
			existingCard.getEmbedding(),
			existingCard.getKeywords(),
			cache.getSubContents()
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
	// UrlHash가 없는 경우 새로운 것을 만들어야한다.
	private CompletableFuture<Boolean> createNewCard(UserId userIdObj, String url, String urlHash) throws Exception {

		// 여기서 2가지 경우로 다시 나눠야한다.
		// summary에서 2가지 경우로 나눠보자.
		return summaryService.getSummary(url)
			.thenApply(SummaryResultDto -> {
				Card card = cardFactory.create(
					userIdObj,
					SummaryResultDto.getCategoryId(),
					SummaryResultDto.getTypeId(),
					urlHash,
					SummaryResultDto.getTitle(),
					SummaryResultDto.getContent(),
					SummaryResultDto.getThumbnailContent(),
					SummaryResultDto.getThumbnailUrl(),
					SummaryResultDto.getEmbeddingVector(),
					SummaryResultDto.getKeywords(),
					SummaryResultDto.getSubContent()
				);

				urlCacheRepository.save(
					UrlCache.builder()
						.urlHash(urlHash)
						.cachedTitle(SummaryResultDto.getTitle())
						.cachedContent(SummaryResultDto.getContent())
						.originalUrl(url)
						.subContents(SummaryResultDto.getSubContent())
						.keywords(SummaryResultDto.getKeywords())
						.build()
				);
				cardRepository.save(card);
				return true;
			});
	}

	@Transactional
	public Boolean createImages(String userId, List<MultipartFile> multipartFiles) {
		UserId userIdObj = new UserId("user");

		List<Card> cards = multipartFiles.stream()
			.map(file -> {
				try {
					String s3Url = imageStorageService.uploadMultipartFile(file);
					String urlHash = UrlCache.generateHash(s3Url);

					// 실제 AI 분석
					// AIAnalysisResponseDTO aiAnalysisResponseDTO = pythonAiClient.imageAnalysis(new AiImageRequestDTO(s3Url));

					// AI Test생성
					AIAnalysisResponseDTO aiAnalysisResponseDTO = AIAnalysisResponseDTO.builder()
						.keywords(new String[]{"으억","냠냠"})
						.embeddingVector(new EmbeddingVector(null))
						.categoryId(new CategoryId(1L))
						.content("AIContnet")
						.thumbnailContent("abcd")
						.build();

					urlCacheRepository.save(
						UrlCache.builder()
							.urlHash(urlHash)
							.cachedTitle("title")
							.cachedContent("ImageContent")
							.originalUrl(s3Url)
							.subContents(new String[]{"핵심키워드"})
							.keywords(aiAnalysisResponseDTO.getKeywords())
							.build()
					);

					return Card.builder()
						.cardId(new CardId(UUID.randomUUID().toString()))
						.userId(userIdObj)
						.categoryId(aiAnalysisResponseDTO.getCategoryId())
						.typeId(1) // 이미지 타입
						.urlHash(urlHash)
						.title("ImageTitle")
						.content(s3Url)
						.thumbnailContent(aiAnalysisResponseDTO.getThumbnailContent())
						.thumbnailUrl(s3Url)
						.embedding(aiAnalysisResponseDTO.getEmbeddingVector())
						.keywords(aiAnalysisResponseDTO.getKeywords())
						.subContents(new String[]{"핵심키워드"})
						.viewCount(0)
						.createdAt(LocalDateTime.now())
						.build();

				} catch (Exception e) {
					throw new RuntimeException("Failed to process image", e);
				}
			})
			.collect(Collectors.toList());


		cards.forEach(System.out::println);
		cardRepository.saveAll(cards);
		cardSearchRepository.saveAll(cards);
		return true;
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

		// Slicex` 값 생성
		SliceRequestDto sliceRequestDto = SliceRequestDto.builder()
			.page(page)
			.size(size)
			.sortBy(sortBy)
			.sortDirection(sortDirection)
			.build();

		// Slice와 파라미터 조건에 맞는 카드를 가져옵니다.
		Slice<Card> cards = findCardListByCategory(
				userIdObj,
				categoryIdObj,
				sliceRequestDto
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


	private Slice<Card> findCardListByCategory(UserId userId, CategoryId categoryId, SliceRequestDto sliceRequestDto) {

		if (categoryId.equals(CategoryId.all())) {
			return cardRepository.findByUserId(
					userId,
					sliceRequestDto.toPageable()
			);
		}

		return cardRepository.findByUserIdAndCategoryId(
				userId,
				categoryId,
				sliceRequestDto.toPageable()
		);
	}
}
