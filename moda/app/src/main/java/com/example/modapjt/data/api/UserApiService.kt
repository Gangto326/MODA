package com.example.modapjt.data.api

import com.example.modapjt.data.dto.request.LoginRequest
import com.example.modapjt.data.dto.response.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface UserApiService {
    @GET("/api/user/")
    suspend fun getUserProfile(): UserProfileResponse

//    @POST("/api/user/login")
//    suspend fun  getUserLogin(
//        @Body userLoginRequest: UserLoginRequest
//    ): Response<Boolean>

//    @POST("/api/user/signup")
//    suspend fun getUserSignup(
//        @Body userSignupRequest: UserSignupRequest
//    ): Response<Boolean>
    @POST("/api/user/login")
    suspend fun login(@Body request: LoginRequest): Response<Boolean>


}
