package com.example.modapjt.domain.model

// 로그인 관련 상태
data class LoginState(
    val username: String = "",          // 아이디
    val password: String = "",          // 비밀번호
    val isLoading: Boolean = false,     // 로딩 상태
    val error: String? = null           // 에러 메시지
)


// 로그인 관련 이벤트
sealed class LoginEvent {
    data class UsernameChanged(val username: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object Submit : LoginEvent()
}

// FindIdState.kt
data class FindIdState(
    val email: String = "",
    val verificationCode: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmailVerificationSent: Boolean = false,
    val foundUsername: String? = null,
    val isEmailVerified: Boolean = false
)

data class FindPasswordState(
    val username: String = "",
    val email: String = "",
    val verificationCode: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val isEmailVerificationSent: Boolean = false,
    val canChangePassword: Boolean = false,
    val isPasswordResetSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val remainingTime: Int = 0 // 인증번호 제한시간 (초)
)



// FindIdEvent.kt
sealed class FindIdEvent {
    data class EmailChanged(val email: String) : FindIdEvent()
    data class VerificationCodeChanged(val code: String) : FindIdEvent()
    object SendVerification : FindIdEvent()
    object VerifyCode : FindIdEvent()
}

// FindPasswordEvent.kt
sealed class FindPasswordEvent {
    data class UsernameChanged(val username: String) : FindPasswordEvent()
    data class EmailChanged(val email: String) : FindPasswordEvent()
    data class VerificationCodeChanged(val code: String) : FindPasswordEvent()
    data class NewPasswordChanged(val password: String) : FindPasswordEvent()
    data class ConfirmNewPasswordChanged(val password: String) : FindPasswordEvent()
    object SendVerification : FindPasswordEvent()
    object VerifyCode : FindPasswordEvent()
    object SubmitNewPassword : FindPasswordEvent()
}