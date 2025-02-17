package com.example.modapjt.data.dto.request

data class SignUpRequest(
    val email: String,
    val userName: String,
    val password: String,
    val nickname: String
)

data class EmailVerificationRequest(
    val email: String,
    val code: String? = null
)

data class UserNameRequest(
    val userId: String
)
