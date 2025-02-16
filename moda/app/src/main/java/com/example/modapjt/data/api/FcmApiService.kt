package com.example.modapjt.data.api

import com.example.modapjt.data.dto.request.FCMTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmApiService {

    @POST("/api/notifications/token")
    suspend fun postToken(
        @Body request: FCMTokenRequest
    )}