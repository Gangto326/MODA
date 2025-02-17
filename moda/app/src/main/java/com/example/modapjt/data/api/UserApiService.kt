package com.example.modapjt.data.api

import com.example.modapjt.data.dto.request.LoginRequest
import com.example.modapjt.data.dto.response.UserStatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {
//    @GET("/api/user/")
//    suspend fun getUserProfile(): UserProfileResponse

//    @POST("/api/user/login")
//    suspend fun  getUserLogin(
//        @Body userLoginRequest: UserLoginRequest
//    ): Response<Boolean>

//    @POST("/api/user/signup")
//    suspend fun getUserSignup(
//        @Body userSignupRequest: UserSignupRequest
//    ): Response<Boolean>

    // 로그인
    @POST("/api/user/login")
    suspend fun login(@Body request: LoginRequest): Response<Boolean>

    // 로그아웃 : 응답 처리 X
    @DELETE("/api/user/logout")
    suspend fun logout(): Response<Unit>

    // 마이페이지 정보 가져오기
    @GET("/api/card/status")
    suspend fun getUserStatus(): UserStatusResponse



}
