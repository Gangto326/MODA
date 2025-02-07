package com.example.modapjt.data.api

import com.example.modapjt.data.dto.response.CategoryDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// API 요청을 정의하는 인터페이스
interface CategoryApiService {
    @GET("api/category")
    suspend fun getCategoryList(
        @Query("userId") userId: String
    ): Response<List<CategoryDTO>>
}

