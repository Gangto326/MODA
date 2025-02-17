package com.example.modapjt.data.api

import com.example.modapjt.data.dto.request.EmailVerificationRequest
import com.example.modapjt.data.dto.request.FindUserIdRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface FindIdApiService {
    @POST("/api/auth/email/send")
    suspend fun sendEmailVerification(
        @Body request: EmailVerificationRequest
    ): Boolean

    @POST("/api/auth/find-user-id")
    suspend fun findUserId(
        @Body request: FindUserIdRequest
    ): String
}