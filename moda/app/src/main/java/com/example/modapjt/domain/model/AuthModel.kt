package com.example.modapjt.domain.model

// 로그인 관련 상태
data class LoginState(
    val username: String = "",          // 아이디
    val password: String = "",          // 비밀번호
    val isLoading: Boolean = false,     // 로딩 상태
    val error: String? = null           // 에러 메시지
)

// 회원가입 관련 상태
data class SignUpState(
    val name: String = "",              // 닉네임
    val username: String = "",          // 아이디
    val email: String = "",             // 이메일
    val emailVerificationCode: String = "", // 이메일 인증 코드
    val password: String = "",          // 비밀번호
    val confirmPassword: String = "",    // 비밀번호 확인
    val isLoading: Boolean = false,     // 로딩 상태
    val error: String? = null,          // 에러 메시지
    val isUsernameVerified: Boolean = false,    // 아이디 중복확인 완료 여부
    val isEmailVerificationSent: Boolean = false, // 이메일 인증코드 전송 여부
    val isEmailVerified: Boolean = false        // 이메일 인증 완료 여부
) {
    // 회원가입 버튼 활성화 조건 검사
    fun isValid(): Boolean {
        return name.isNotEmpty() &&
                username.isNotEmpty() &&
                isUsernameVerified &&
                email.isNotEmpty() &&
                isEmailVerified &&
                password.isNotEmpty() &&
                password == confirmPassword
    }
}

// 로그인 관련 이벤트
sealed class LoginEvent {
    data class UsernameChanged(val username: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object Submit : LoginEvent()
}

// 회원가입 관련 이벤트
sealed class SignUpEvent {
    // 입력값 변경 이벤트
    data class NameChanged(val name: String) : SignUpEvent()
    data class UsernameChanged(val username: String) : SignUpEvent()
    data class EmailChanged(val email: String) : SignUpEvent()
    data class EmailVerificationCodeChanged(val code: String) : SignUpEvent()
    data class PasswordChanged(val password: String) : SignUpEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpEvent()

    // 검증 관련 이벤트
    object VerifyUsername : SignUpEvent()        // 아이디 중복 확인
    object SendEmailVerification : SignUpEvent() // 이메일 인증 요청
    object VerifyEmailCode : SignUpEvent()      // 이메일 인증코드 확인

    // 제출 이벤트
    object Submit : SignUpEvent()
}