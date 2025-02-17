package com.example.modapjt.data.api

import com.example.modapjt.data.dto.request.EmailVerificationRequest
import com.example.modapjt.data.dto.request.SignUpRequest
import com.example.modapjt.data.dto.request.UserNameRequest
import com.example.modapjt.data.dto.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApiService {
    @POST("/api/auth/check-user-name")
    suspend fun checkUsername(
        @Body request: UserNameRequest
    ): Boolean

    @POST("/api/auth/email/send")
    suspend fun sendEmailVerification(
        @Body request: EmailVerificationRequest
    ): Boolean  // ApiResponse<Unit>에서 Boolean으로 변경

    @POST("/api/auth/email/verify")
    suspend fun verifyEmailCode(
        @Body request: EmailVerificationRequest
    ): Boolean  // ApiResponse<Boolean>에서 Boolean으로 변경

    @POST("/api/user/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): ApiResponse<Unit>
}
