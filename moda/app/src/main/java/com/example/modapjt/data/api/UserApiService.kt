package com.example.modapjt.data.api

import com.example.modapjt.data.dto.response.UserProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiService {
    @GET("/api/user/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: String): UserProfileResponse
}
