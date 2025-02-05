package com.example.modapjt.data.api

import com.example.modapjt.data.dto.response.ApiResponse
import com.example.modapjt.data.dto.response.BoardDTO
import com.example.modapjt.data.dto.response.CardDTO
import com.example.modapjt.data.dto.response.CardDetailDTO
import com.example.modapjt.data.dto.response.CardResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// API 요청을 정의하는 인터페이스
interface ApiService {
    // 특정 유저의 보드 가져오기 (userId 사용)
    @GET("api/board")
    suspend fun getBoardList(
        @Query("userId") userId: String, // 현재 로그인된 유저의 ID : userId를 쿼리 파라미터로 전달
//        @Header("token") token: String // 토큰이 필요한 경우
    ): Response<List<BoardDTO>>

    // 특정 보드의 카드 목록 가져오기
//    @GET("api/card")
//    suspend fun getCardList(
//        @Query("userId") userId: String,
//        @Query("boardId") boardId: String,
//        @Query("page") page: Int = 1,             // 기본값 추가
//        @Query("size") size: Int = 15,            // 기본값 추가
//        @Query("sortBy") sortBy: String = "createdAt",  // 기본값 추가
//        @Query("sortDirection") sortDirection: String = "DESC" // 기본값 추가
//    ): Response<List<CardDTO>> // "content"만 가져오도록 Response<List<CardDTO>> 사용
    @GET("api/card")
    suspend fun getCardList(
        @Query("userId") userId: String,
        @Query("boardId") boardId: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDirection") sortDirection: String = "DESC"
    ): Response<CardResponse> // List<CardDTO>가 아니라 CardResponse를 반환하도록 변경

    // 카드 상세 정보 조회 api
    @GET("api/card/{cardId}")
    suspend fun getCardDetail(
        @Path("cardId") cardId: String,
        @Query("userId") userId: String
    ): Response<CardDetailDTO>

}
