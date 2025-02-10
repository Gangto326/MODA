package com.example.modapjt.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface SearchApiService {
    @GET("api/search/auto/{query}")
    suspend fun getAutoCompleteKeywords(@Path("query") query: String): List<String>
}

