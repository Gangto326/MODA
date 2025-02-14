package com.example.modapjt.data.api

import com.example.modapjt.data.dto.response.HomeKeywordResponse
import com.example.modapjt.data.dto.response.KeywordSearchResponse
import com.example.modapjt.data.dto.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApiService {
    @GET("api/search/auto/{query}")
    suspend fun getAutoCompleteKeywords(@Path("query") query: String): List<String>

    // ✅ 관심 키워드 가져오는 API
    @GET("/api/search/keyword/{keyword}")
    suspend fun getInterestKeywords(@Path("keyword") keyword: String): List<String>

    /**
     * 사용자 ID를 기반으로 검색 결과를 가져오는 API
     * @param userId 사용자 ID
     * @return 검색 결과를 포함하는 Response 객체
     */
    @GET("api/search/main")
    suspend fun searchMain(
        @Query("userId") userId: String
    ): SearchResponse

    @GET("api/card/main")
    suspend fun homeKeyword(
        @Query("userId") userId : String
    ): HomeKeywordResponse

    @GET("/api/search/keyword/{keyword}")
    suspend fun getSearchDataByKeyword(
        @Path("keyword") keyword: String,
        @Query("userId") userId: String
    ): List<KeywordSearchResponse>

}

