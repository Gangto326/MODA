package com.example.modapjt.data.api

import com.example.modapjt.data.dto.response.CardApiResponse
import com.example.modapjt.data.dto.response.CardDTO
import com.example.modapjt.data.dto.response.CardDetailDTO
import com.example.modapjt.data.dto.response.CategoryDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// API 요청을 정의하는 인터페이스
interface CardApiService {
    // 카드 리스트 API
    @GET("api/card")
    suspend fun getCardList(
        @Query("userId") userId: String,
        @Query("categoryId") categoryId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDirection") sortDirection: String = "DESC"
    ): Response<CardApiResponse>

    // 카드 상세 페이지 API
    @GET("api/card/{cardId}")
    suspend fun getCardDetail(
        @Path("cardId") cardId: String,
        @Query("userId") userId: String
    ): Response<CardDetailDTO>

    // 카드 삭제 API
    @DELETE("api/card/{cardId}")
    suspend fun deleteCard(
        @Path("cardId") cardId: String
    ): Response<Boolean>
}