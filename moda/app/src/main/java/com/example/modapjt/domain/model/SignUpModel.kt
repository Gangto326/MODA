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
    val passwordValidationMessage: String? = null, //비밀번호 검증
    val isTimerRunning: Boolean = false
) {
    // 비밀번호 유효성 검사 함수 추가
    fun isPasswordValid(): Boolean {
        return password.length >= 8
    }

    fun isPasswordsMatch(): Boolean {
        return password == confirmPassword && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }


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

