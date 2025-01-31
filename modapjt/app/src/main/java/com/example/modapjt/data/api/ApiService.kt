package com.example.modapjt.data.api

import com.example.modapjt.data.dto.response.ApiResponse
import com.example.modapjt.data.dto.response.BoardDTO
import com.example.modapjt.data.dto.response.CardDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// 서버 요청 정의
interface ApiService {
    @GET("boards/{boardId}/cards")
    fun getContents(@Path("boardId") boardId: String): Call<List<CardDTO>>


    @GET("boards/{nickName}")
    suspend fun getBoardList(
        @Path("nickName") nickName: String,
        @Header("token") token: String
    ): Response<ApiResponse<List<BoardDTO>>>

//    @POST("card")
//    suspend fun addCard(
//        @Header("token") token: String,
//        @Body request: ShareContentRequest
//    ): Response<ApiResponse<CardDTO>>

    @GET("card")
    suspend fun getCardList(
        @Header("token") token: String,
        @Query("boardId") boardId: String
    ): Response<ApiResponse<List<CardDTO>>>
}
