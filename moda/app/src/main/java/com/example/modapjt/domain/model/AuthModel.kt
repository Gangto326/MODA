package com.example.modapjt.domain.model

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,  // 1: 이메일/닉네임, 2: 비밀번호
    val isEmailVerified: Boolean = false  // 이메일 중복 확인 상태
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object Submit : LoginEvent()
}

sealed class SignUpEvent {
    data class EmailChanged(val email: String) : SignUpEvent()
    data class PasswordChanged(val password: String) : SignUpEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpEvent()
    data class NameChanged(val name: String) : SignUpEvent()
    object Submit : SignUpEvent()
    object NextPage : SignUpEvent()
    object PreviousPage : SignUpEvent()
    object VerifyEmail : SignUpEvent()
}