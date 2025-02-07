package com.example.modapjt.data.api

import com.example.modapjt.data.dto.response.CardDTO
import com.example.modapjt.data.dto.response.CardDetailDTO
import com.example.modapjt.data.dto.response.CategoryDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// API 요청을 정의하는 인터페이스
interface CardApiService {
    @GET("card")
    suspend fun getCardList(
        @Query("userId") userId: String,
        @Query("categoryId") categoryId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDirection") sortDirection: String = "DESC"
    ): Response<List<CardDTO>>

    @GET("card/{cardId}")
    suspend fun getCardDetail(
        @Path("cardId") cardId: String,
        @Query("userId") userId: String
    ): Response<CardDetailDTO>
}