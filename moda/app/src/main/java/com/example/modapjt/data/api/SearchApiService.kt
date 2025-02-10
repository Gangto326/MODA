package com.example.modapjt.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface SearchApiService {
    @GET("api/search/auto/{query}")
    suspend fun getAutoCompleteKeywords(@Path("query") query: String): List<String>

    // ✅ 관심 키워드 가져오는 API
    @GET("/api/search/keyword/{keyword}")
    suspend fun getInterestKeywords(@Path("keyword") keyword: String): List<String>
}

