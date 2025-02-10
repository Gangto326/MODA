package com.moda.moda_api.search.application.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moda.moda_api.card.domain.CardContentType;
import com.moda.moda_api.category.domain.CategoryId;
import com.moda.moda_api.common.pagination.SliceRequestDto;
import com.moda.moda_api.common.pagination.SliceResponseDto;
import com.moda.moda_api.search.application.mapper.CardSearchDtoMapper;
import com.moda.moda_api.search.application.response.CardDocumentListResponse;
import com.moda.moda_api.search.application.response.SearchResultByCardList;
import com.moda.moda_api.search.application.response.SearchTypeScore;
import com.moda.moda_api.search.domain.CardDocument;
import com.moda.moda_api.search.domain.CardSearchRepository;
import com.moda.moda_api.user.domain.UserId;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {
	private final CardSearchRepository cardSearchRepository;
	private final CardSearchDtoMapper cardSearchDtoMapper;

	/**
	 * 검색어와 가장 일치하는 사용자의 게시글들의 모든 핵심 키워드 리스트를 가져옵니다.
	 * 가져온 키워드들로 만들 수 있는 조합을 만들어 10개의 String 문장을 반환합니다.
	 * @param userId
	 * @param query
	 * @return
	 */
	public List<String> autoCompleteQuery(String userId, String query) {
		UserId userIdObj = new UserId(userId);

		String[] searchTerms = query.trim().split("\\s+");

		// 마지막 단어를 제외한 모든 단어들을 List로 변환
		List<String> completeKeywords = searchTerms.length > 1
			? Arrays.asList(Arrays.copyOfRange(searchTerms, 0, searchTerms.length - 1))
			: List.of(searchTerms[0]);

		// 마지막 단어는 prefix 매칭
		String prefixMatch = searchTerms[searchTerms.length - 1];

		List<CardDocument> documents = cardSearchRepository.findAutoCompleteSuggestions(
			userIdObj,
			completeKeywords,
			prefixMatch
		);

		return documents.stream()
			.flatMap(cardDocument -> {
				/**
				 * 검색어와 가장 유사한 키워드를 찾습니다.
				 *
				 * 가장 첫 글자를 기준으로 포함되는지 확인합니다.
				 * 첫 글자가 중복되거나, 첫 글자가 오탈자인 경우가 고려되지 않습니다.
				 */
				Optional<String> matchedKeyword = Arrays.stream(cardDocument.getKeywords())
					.filter(k -> k.contains(searchTerms[0]))
					.findFirst();

				if (matchedKeyword.isEmpty())
					return Stream.empty();

				// 나머지 키워드와 조합
				return Arrays.stream(cardDocument.getKeywords())
					.filter(k -> !k.equals(matchedKeyword.get()))
					.map(k -> matchedKeyword.get() + " " + k);
			})
			.distinct()
			.limit(10)
			.toList();
	}

	/**
	 * 사용자의 요청에 맞는 콘텐츠가 보일 메인 페이지 데이터를 반환합니다.
	 *
	 * 1(Img)의 경우는 10개, 다른 콘텐츠(2, 3, 4)의 경우 5개의 최적 컨텐츠를 반환.
	 * @param userId
	 * @param query
	 * @return
	 */
	public CompletableFuture<SearchResultByCardList> searchCardDocumentListByMainPage(
		String userId, String query, Long categoryId
	) {
		UserId userIdObj = new UserId(userId);
		List<Integer> targetTypes = List.of(1, 2, 3, 4);

		// IMG(1) 타입은 10개
		Map<Integer, Integer> typeSizes = Map.of(1, 10, 2, 5, 3, 5, 4, 5);

		// 쿼리 기준 검색인 경우
		if (categoryId == 0L) {
			// 각 타입별 검색을 비동기로 실행
			List<CompletableFuture<Map.Entry<CardContentType, List<CardDocument>>>> futures = executeAsyncSearches(
				userIdObj, query, targetTypes, typeSizes);

			// 모든 비동기 작업이 완료되면 결과 합치기 및 메타데이터 생성
			return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApply(v -> {
					Map<CardContentType, List<CardDocumentListResponse>> results = processSearchResults(futures, query,
						userIdObj);
					List<SearchTypeScore> topScores = extractTopScores(results);

					return SearchResultByCardList.builder()
						.contentResults(results)
						.topScores(topScores)
						.build();
				});
		}

		// 카테고리 기준 검색인 경우
		else {
			CategoryId categoryIdObj = new CategoryId(categoryId);

			// 각 타입별 검색을 비동기로 실행
			List<CompletableFuture<Map.Entry<CardContentType, List<CardDocument>>>> futures = executeAsyncSearchesByCategory(
				userIdObj, categoryIdObj, targetTypes, typeSizes);

			// 모든 비동기 작업이 완료되면 결과 합치기. topScores 메타데이터는 null입니다.
			return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApply(v -> {
					Map<CardContentType, List<CardDocumentListResponse>> results = processSearchResults(futures, "",
						userIdObj);

					return SearchResultByCardList.builder()
						.contentResults(results)
						.topScores(null)
						.build();
				});
		}
	}

	/**
	 * 키워드와 "정확히 일치"하는 컨텐츠(이미지 제외)를 5개 반환합니다.
	 * @param userId
	 * @param keyword
	 * @return
	 */
	public List<CardDocumentListResponse> searchByKeyword(String userId, String keyword) {
		UserId userIdObj = new UserId(userId);

		PageRequest pageRequest = PageRequest.of(0, 5);
		List<CardDocument> cardDocumentList = cardSearchRepository.searchByKeyword(userIdObj, keyword, pageRequest);

		return cardDocumentList.stream()
			.map(cardDocument -> cardSearchDtoMapper.toListResponse(cardDocument, keyword, userIdObj))
			.collect(Collectors.toList());
	}

	/**
	 * 사용자의 요청과 typeId를 기준으로 페이지네이션한 리스트를 반환합니다.
	 * @param userId
	 * @param searchText
	 * @param categoryId
	 * @param typeId
	 * @param page
	 * @param size
	 * @param sortBy
	 * @param sortDirection
	 * @return
	 */
	public SliceResponseDto<CardDocumentListResponse> search(
		String userId, String searchText, Long categoryId, Integer typeId,
		Integer page, Integer size, String sortBy, String sortDirection
	) {
		UserId userIdObj = new UserId(userId);

		// Slice 값 생성
		SliceRequestDto sliceRequestDto = SliceRequestDto.builder()
			.page(page)
			.size(size)
			.sortBy(sortBy)
			.sortDirection(sortDirection)
			.build();

		// 쿼리 기준 검색인 경우
		if (categoryId == 0L) {
			Slice<CardDocument> cardDocuments = cardSearchRepository.searchComplex(
				typeId, userIdObj, searchText, sliceRequestDto.toPageable()
			);

			return SliceResponseDto.of(
				cardDocuments.map(cardDocument -> cardSearchDtoMapper.toListResponse(
					cardDocument, searchText, userIdObj)
				)
			);
		}

		// 카테고리 기준 검색인 경우
		else {
			CategoryId categoryIdObj = new CategoryId(categoryId);
			Slice<CardDocument> cardDocuments = categoryIdObj.equals(CategoryId.all())
					// 카테고리가 ALL인 경우
					? cardSearchRepository.searchByAllCategoryAndType(typeId, userIdObj, sliceRequestDto.toPageable())
					// 카테고리가 지정된 경우
					: cardSearchRepository.searchByCategoryAndType(typeId, categoryIdObj, userIdObj, sliceRequestDto.toPageable());

			return SliceResponseDto.of(
				cardDocuments.map(cardDocument -> cardSearchDtoMapper.toListResponse(
					cardDocument, "", userIdObj)
				)
			);
		}
	}

	/**
	 * 비동기로 검색을 실행합니다. (검색어 기준)
	 * @param userId
	 * @param searchText
	 * @param targetTypes
	 * @param typeSizes
	 * @return
	 */
	private List<CompletableFuture<Map.Entry<CardContentType, List<CardDocument>>>> executeAsyncSearches(
		UserId userId, String searchText, List<Integer> targetTypes, Map<Integer, Integer> typeSizes) {

		return targetTypes.stream()
			.map(typeId -> CompletableFuture.supplyAsync(() -> {
				// 각 타입별 가져올 갯수를 PageRequest로 생성
				PageRequest pageRequest = PageRequest.of(0, typeSizes.get(typeId));

				// 타입별 데이터 가져오기
				Slice<CardDocument> results = cardSearchRepository.searchComplex(
					typeId, userId, searchText, pageRequest);
				return Map.entry(
					CardContentType.from(typeId),
					results.getContent().stream()
						.toList()
				);
			}))
			.toList();
	}

	/**
	 * 비동기로 검색을 실행합니다. (카테고리 기준)
	 * @param userId
	 * @param categoryId
	 * @param targetTypes
	 * @param typeSizes
	 * @return
	 */
	private List<CompletableFuture<Map.Entry<CardContentType, List<CardDocument>>>> executeAsyncSearchesByCategory(
		UserId userId, CategoryId categoryId, List<Integer> targetTypes, Map<Integer, Integer> typeSizes) {

		return targetTypes.stream()
			.map(typeId -> CompletableFuture.supplyAsync(() -> {
				// 각 타입별 가져올 갯수를 PageRequest로 생성
				PageRequest pageRequest = PageRequest.of(0, typeSizes.get(typeId));

				// 타입별 카테고리 기준 데이터 가져오기
				Slice<CardDocument> results = categoryId.equals(CategoryId.all())
						// 카테고리가 ALL인 경우
						? cardSearchRepository.searchByAllCategoryAndType(typeId, userId, pageRequest)
						// 카테고리가 지정된 경우
						: cardSearchRepository.searchByCategoryAndType(typeId, categoryId, userId, pageRequest);

				return Map.entry(
					CardContentType.from(typeId),
					results.getContent().stream()
						.toList()
				);
			}))
			.toList();
	}

	/**
	 * 검색 결과를 처리합니다.
	 * @param futures
	 * @param searchText
	 * @return
	 */
	private Map<CardContentType, List<CardDocumentListResponse>> processSearchResults(
		List<CompletableFuture<Map.Entry<CardContentType, List<CardDocument>>>> futures,
		String searchText, UserId userId) {

		return futures.stream()
			.map(CompletableFuture::join)
			.filter(entry -> !entry.getValue().isEmpty())

			// 각 ContentType별 cardList를 Key: Value 형식으로 매핑
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> entry.getValue().stream()
					.map(cardDocument -> cardSearchDtoMapper.toListResponse(cardDocument, searchText, userId))
					.collect(Collectors.toList())
			));
	}

	/**
	 * 각 List내의 컨텐츠 중 최고 점수를 추출합니다.
	 * @param results
	 * @return
	 */
	private List<SearchTypeScore> extractTopScores(
		Map<CardContentType, List<CardDocumentListResponse>> results) {

		return results.entrySet().stream()
			.filter(entry -> !entry.getValue().isEmpty())

			// 각 타입별 리스트의 0번째 게시글의 점수를 비교하여 우선순위 metaData를 생성
				.filter(entry -> entry.getValue().get(0).getScore() != null)
			.map(entry -> SearchTypeScore.builder()
				.contentType(entry.getKey())
				.score(entry.getValue().get(0).getScore())
				.build())
			.sorted(Comparator.comparing(SearchTypeScore::getScore).reversed())
			.toList();
	}
}
