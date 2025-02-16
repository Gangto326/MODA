package com.example.modapjt.data.api

import com.example.modapjt.data.dto.response.UserProfileResponse
import retrofit2.http.GET

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



}
