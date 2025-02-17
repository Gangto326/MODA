package com.example.modapjt.domain.model

data class SignUpState(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val emailVerificationCode: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUsernameVerified: Boolean = false,
    val isEmailVerificationSent: Boolean = false,
    val isEmailVerified: Boolean = false,
    val usernameVerificationMessage: String? = null,
    val emailVerificationMessage: String? = null,
    val passwordMatchMessage: String? = null,
    val remainingTime: Int = 600,
    val isTimerRunning: Boolean = false
) {
    fun isValid(): Boolean {
        return name.isNotEmpty() &&
                username.isNotEmpty() &&
                isUsernameVerified &&
                email.isNotEmpty() &&
                isEmailVerified &&
                password.isNotEmpty() &&
                password == confirmPassword
    }

    fun isEmailFieldValid(): Boolean {
        return email.isNotEmpty()
    }
}

sealed class SignUpEvent {
    data class NameChanged(val name: String) : SignUpEvent()
    data class UsernameChanged(val username: String) : SignUpEvent()
    data class EmailChanged(val email: String) : SignUpEvent()
    data class EmailVerificationCodeChanged(val code: String) : SignUpEvent()
    data class PasswordChanged(val password: String) : SignUpEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpEvent()

    object VerifyUsername : SignUpEvent()
    object SendEmailVerification : SignUpEvent()
    object VerifyEmailCode : SignUpEvent()
    object Submit : SignUpEvent()
}

