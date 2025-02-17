package com.example.modapjt.data.api

import com.example.modapjt.data.dto.request.EmailVerificationRequest
import com.example.modapjt.data.dto.request.ResetPasswordRequest
import com.example.modapjt.data.dto.request.VerifyPasswordChangeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface FindPasswordApiService {
    // 이메일 인증번호 발송
    @POST("/api/auth/email/send")
    suspend fun sendVerificationEmail(
        @Body request: EmailVerificationRequest
    ): Boolean

    // 인증번호 확인 및 비밀번호 변경 확인
    @POST("/api/auth/password-change-check")
    suspend fun verifyPasswordChangeCode(
        @Body request: VerifyPasswordChangeRequest
    ): Boolean

    // 비밀번호 재설정
    @PATCH("/api/user/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Boolean
}