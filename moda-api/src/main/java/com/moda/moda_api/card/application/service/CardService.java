package com.moda.moda_api.card.application.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.moda.moda_api.card.application.response.CardMainResponse;
import com.moda.moda_api.card.application.response.HotTopicResponse;
import com.moda.moda_api.card.domain.*;
import com.moda.moda_api.card.exception.DuplicateCardException;
import com.moda.moda_api.card.exception.InvalidCardContentException;
import com.moda.moda_api.card.presentation.request.CardBookmarkRequest;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moda.moda_api.card.application.mapper.CardDtoMapper;
import com.moda.moda_api.card.application.response.CardDetailResponse;
import com.moda.moda_api.card.application.response.CardListResponse;
import com.moda.moda_api.card.exception.CardNotFoundException;
import com.moda.moda_api.card.presentation.request.MoveCardRequest;
import com.moda.moda_api.card.presentation.request.UpdateCardRequest;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.common.infrastructure.ImageStorageService;
import com.moda.moda_api.common.pagination.SliceRequestDto;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.notification.application.NotificationService;
import com.moda.moda_api.notification.domain.NotificationType;
import com.moda.moda_api.search.domain.CardSearchRepository;
import com.moda.moda_api.summary.application.service.SummaryService;
import com.moda.moda_api.summary.infrastructure.api.PythonAiClient;
import com.moda.moda_api.summary.infrastructure.dto.AIAnalysisResponseDTO;
import com.moda.moda_api.summary.infrastructure.dto.AiImageRequestDTO;
import com.moda.moda_api.user.domain.UserId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CardService {
	private final CardRepository cardRepository;
	private final CardFactory cardFactory;
	private final CardDtoMapper cardDtoMapper;
	private final UserKeywordRepository userKeywordRepository;
	private final VideoCreatorRepository videoCreatorRepository;
	private final CardViewCountRepository cardViewCountRepository;
	private final HotTopicRepository hotTopicRepository;
	private final UrlCacheRepository urlCacheRepository;
	private final SummaryService summaryService;
	private final ImageStorageService imageStorageService;
	private final PythonAiClient pythonAiClient;
	private final CardSearchRepository cardSearchRepository;
	private final NotificationService notificationService;

	/**
	 * URL을 입력 받고 새로운 카드 생성 후 알맞은 보드로 이동합니다.
	 * @param userId
	 * @param url
	 * @return
	 */
	@Transactional
	public CompletableFuture<Boolean> createCard(String userId, String url) {
		UserId userIdObj = new UserId(userId);
		String urlHash = UrlCache.generateHash(url);

		if (cardRepository.existsByUserIdAndUrlHashAndDeletedAtIsNull(userIdObj, urlHash))
			throw new DuplicateCardException("같은 URL을 가진 카드가 이미 존재합니다.");

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

		UrlCache cache = urlCacheRepository.findByUrlHash(urlHash).get();

		Card card = cardFactory.create(
			userIdObj,
			cache.getCategoryId(),
			cache.getTypeId(),
			urlHash,
			cache.getCachedTitle(),
			cache.getCachedContent(),
			cache.getCachedThumbnailContent(),
			// TODO: 2-14 종원 확인 필요
			cache.getCachedThumbnailUrl(),
			cache.getCachedEmbedding(),
			cache.getCachedKeywords(),
			cache.getCachedSubContents()
		);

		// 유저별 핵심 키워드 저장 (Redis)
		userKeywordRepository.saveKeywords(userIdObj, card.getKeywords());

		// 핫 토픽 키워드 저장 (Redis)
		hotTopicRepository.incrementKeywordScore(card.getKeywords());

		cardRepository.save(card);
		cardSearchRepository.save(card);
		notificationService.sendFCMNotification(userIdObj.getValue(), NotificationType.card,card);

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
				String thumbnailUrl = SummaryResultDto.getThumbnailUrl() !=
					null ? SummaryResultDto.getThumbnailUrl() :
					"https://a805bucket.s3.ap-northeast-2.amazonaws.com/images/logo/download.jpg";
				Card card = cardFactory.create(
					userIdObj,
					SummaryResultDto.getCategoryId(),
					SummaryResultDto.getTypeId(),
					urlHash,
					SummaryResultDto.getTitle(),
					SummaryResultDto.getContent(),
					SummaryResultDto.getThumbnailContent(),
					thumbnailUrl,
					SummaryResultDto.getEmbeddingVector(),
					SummaryResultDto.getKeywords(),
					SummaryResultDto.getSubContent()
				);

				System.out.println(card.toString());

				urlCacheRepository.save(
					UrlCache.builder()
						.urlHash(urlHash)
						.categoryId(card.getCategoryId())
						.typeId(card.getTypeId())
						.cachedTitle(card.getTitle())
						.cachedContent(card.getContent())
						.originalUrl(url)
						.cachedThumbnailContent(card.getThumbnailContent())
						.cachedThumbnailUrl(card.getThumbnailUrl())
						.cachedEmbedding(card.getEmbedding())
						.cachedKeywords(card.getKeywords())
						.cachedSubContents(card.getSubContents())
						.build()
				);

				// 유저별 핵심 키워드 저장 (Redis)
				userKeywordRepository.saveKeywords(userIdObj, card.getKeywords());

				// 핫 토픽 키워드 저장 (Redis)
				hotTopicRepository.incrementKeywordScore(card.getKeywords());

				cardRepository.save(card);
				cardSearchRepository.save(card);

				notificationService.sendFCMNotification(userIdObj.getValue(), NotificationType.card,card);


				return true;
			});
	}

	@Transactional
	public Boolean createImages(String userId, List<MultipartFile> multipartFiles) {
		UserId userIdObj = new UserId(userId);

		List<Card> cards = multipartFiles.stream()
			.map(file -> {
				try {
					String s3Url = imageStorageService.uploadMultipartFile(file);
					String urlHash = UrlCache.generateHash(s3Url);

					// 실제 AI 분석
					// AIAnalysisResponseDTO aiAnalysisResponseDTO = pythonAiClient.imageAnalysis(new AiImageRequestDTO(s3Url));

					// AI Test생성
					AIAnalysisResponseDTO aiAnalysisResponseDTO = AIAnalysisResponseDTO.builder()
						.keywords(new String[] {"3차"})
						.embeddingVector(new EmbeddingVector(null))
						.categoryId(new CategoryId(3L))
						.content("AIContnet")
						.thumbnailContent("abcd")
						.build();

					return Card.builder()
						.cardId(new CardId(UUID.randomUUID().toString()))
						.userId(userIdObj)
						.categoryId(aiAnalysisResponseDTO.getCategoryId())
						.typeId(4) // 이미지 타입
						.urlHash(null)
						.title("ImageTitle")
						.content(aiAnalysisResponseDTO.getContent())
						.thumbnailContent(aiAnalysisResponseDTO.getThumbnailContent())
						.thumbnailUrl(s3Url)
						.embedding(aiAnalysisResponseDTO.getEmbeddingVector())
						.keywords(aiAnalysisResponseDTO.getKeywords())
						.subContents(null)
						.build();


				} catch (Exception e) {
					throw new RuntimeException("Failed to process image", e);
				}
			})
			.collect(Collectors.toList());

		cards.forEach(System.out::println);
		cardRepository.saveAll(cards);
		cardSearchRepository.save(cards.get(0));

		notificationService.sendFCMNotification(userIdObj.getValue(), NotificationType.card,cards.get(0));

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
	`	 * @param sortDirection
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

		// 조회수 증가 로직
		cardViewCountRepository.incrementViewCount(cardIdObj);

		// 카드 탐색
		Card card = findCardDetail(cardIdObj);
		return cardDtoMapper.toDetailResponse(userIdObj, card);
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

		// ES에서 카드 삭제
		cardSearchRepository.deleteAllById(cardIdList);

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

		return cardDtoMapper.toDetailResponse(userIdObj, card);
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

	private Card findCardDetail(CardId cardId) {
		return cardRepository.findByCardId(cardId)
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

	/**
	 * 북마크 변경
	 * @param userId
	 * @param request
	 * @return
	 */
	@Transactional
	public Boolean cardBookmark(String userId, CardBookmarkRequest request) {
		UserId userIdObj = new UserId(userId);
		CardId cardIdObj = new CardId(request.getCardId());

		Card card = findCard(userIdObj, cardIdObj);
		card.setBookmark(request.getIsBookmark());

		cardRepository.save(card);
		cardSearchRepository.save(card);
		return true;
	}

	/**
	 * 메인 화면의 핵심 키워드 5개와 크리에이터 이름을 반환합니다.
	 * @param userId
	 * @return
	 */
	public CardMainResponse getMainKeywords(String userId) {
		UserId userIdObj = new UserId(userId);
		List<Object[]> results = cardRepository.findCategoryExistenceByUserId(userIdObj);

		// 기본적으로 모든 categoryId(2~10) 값에 대해 false로 초기화
		Map<Long, Boolean> categories = LongStream.rangeClosed(2, 10)
				.boxed()
				.collect(Collectors.toMap(category -> category, category -> false));


		// 전체 콘텐츠(1번) 값 초기화
		boolean hasAnyContent = false;

		// 쿼리 결과를 기반으로 존재하는 categoryId를 true로 설정
		for (Object[] result : results) {
			Long categoryId = (Long) result[0];
			Long count = (Long) result[1];

			boolean exists = count > 0;
			categories.put(categoryId, count > 0);

			// 하나라도 count > 0 이면 전체 콘텐츠(1번)를 true로 설정
			if (exists) {
				hasAnyContent = true;
			}
		}

		categories.put(1L, hasAnyContent);

		return CardMainResponse.builder()
			.topKeywords(userKeywordRepository.getTopKeywords(userIdObj, 5))
			.creator(videoCreatorRepository.getCreatorByUserId(userIdObj))
				.categories(categories)
			.build();
	}

	/**
	 * 최근 인기 토픽을 순위 변동 메타데이터와 함께 limit개를 반환합니다.
	 * @param limit
	 * @return
	 */
	public List<HotTopicResponse> getHotTopics(Integer limit) {

		return hotTopicRepository.getTopKeywordsWithChange(limit);
	}
}
