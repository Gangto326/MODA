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

// API 요청을 정의하는 인터페이스
interface ApiService {
    // 특정 유저의 보드 가져오기 (userId 사용)
    @GET("api/board")
    suspend fun getBoardList(
        @Query("userId") userId: String, // 현재 로그인된 유저의 ID : userId를 쿼리 파라미터로 전달
//        @Header("token") token: String // 토큰이 필요한 경우
    ): Response<List<BoardDTO>>

}
