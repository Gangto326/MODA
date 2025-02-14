package com.example.modapjt.domain.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.domain.model.LoginEvent
import com.example.modapjt.domain.model.LoginState
import com.example.modapjt.domain.model.SignUpEvent
import com.example.modapjt.domain.model.SignUpState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _signUpState = mutableStateOf(SignUpState())
    val signUpState: State<SignUpState> = _signUpState

    private var onLoginSuccess: () -> Unit = {}

    fun setOnLoginSuccess(callback: () -> Unit) {
        onLoginSuccess = callback
    }

    fun onLoginEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UsernameChanged -> {
                _loginState.value = _loginState.value.copy(username = event.username)
            }
            is LoginEvent.PasswordChanged -> {
                _loginState.value = _loginState.value.copy(password = event.password)
            }
            is LoginEvent.Submit -> {
                login()
            }
        }
    }

    private fun login() {
        val username = _loginState.value.username
        val password = _loginState.value.password

        if (username.isEmpty() || password.isEmpty()) {
            _loginState.value = _loginState.value.copy(error = "아이디와 비밀번호를 입력해주세요.")
            return
        }

        // 임시 로그인 로직 (실제로는 API 호출로 대체)
        if (username == "user" && password == "1234") {
            _loginState.value = _loginState.value.copy(isLoading = true)

            viewModelScope.launch {
                delay(1000)
                _loginState.value = _loginState.value.copy(isLoading = false, error = null)
                onLoginSuccess()
            }
        } else {
            _loginState.value = _loginState.value.copy(error = "아이디 또는 비밀번호가 잘못되었습니다.")
        }
    }

    fun onSignUpEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    email = event.email,
                    isEmailVerified = false
                )
            }
            is SignUpEvent.PasswordChanged -> {
                _signUpState.value = _signUpState.value.copy(password = event.password)
            }
            is SignUpEvent.ConfirmPasswordChanged -> {
                _signUpState.value = _signUpState.value.copy(confirmPassword = event.confirmPassword)
            }
            is SignUpEvent.NameChanged -> {
                _signUpState.value = _signUpState.value.copy(name = event.name)
            }
            is SignUpEvent.UsernameChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    username = event.username,
                    isUsernameVerified = false
                )
            }
            is SignUpEvent.EmailVerificationCodeChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    emailVerificationCode = event.code
                )
            }
            is SignUpEvent.VerifyUsername -> {
                verifyUsername()
            }
            is SignUpEvent.SendEmailVerification -> {
                sendEmailVerification()
            }
            is SignUpEvent.VerifyEmailCode -> {
                verifyEmailCode()
            }
            is SignUpEvent.Submit -> {
                submitSignUp()
            }
        }
    }

    private fun verifyUsername() {
        val username = _signUpState.value.username
        if (username.isEmpty()) {
            _signUpState.value = _signUpState.value.copy(error = "아이디를 입력해주세요.")
            return
        }

        viewModelScope.launch {
            _signUpState.value = _signUpState.value.copy(isLoading = true)
            delay(1000) // 실제로는 API 호출로 대체될 부분

            // 임시로 항상 사용 가능하다고 가정
            _signUpState.value = _signUpState.value.copy(
                isUsernameVerified = true,
                isLoading = false,
                error = null
            )
        }
    }

    private fun sendEmailVerification() {
        val email = _signUpState.value.email
        if (email.isEmpty()) {
            _signUpState.value = _signUpState.value.copy(error = "이메일을 입력해주세요.")
            return
        }

        viewModelScope.launch {
            _signUpState.value = _signUpState.value.copy(isLoading = true)
            delay(1000) // 실제로는 API 호출로 대체될 부분

            _signUpState.value = _signUpState.value.copy(
                isEmailVerificationSent = true,
                isLoading = false,
                error = null
            )
        }
    }

    private fun verifyEmailCode() {
        val code = _signUpState.value.emailVerificationCode
        if (code.isEmpty()) {
            _signUpState.value = _signUpState.value.copy(error = "인증번호를 입력해주세요.")
            return
        }

        viewModelScope.launch {
            _signUpState.value = _signUpState.value.copy(isLoading = true)
            delay(1000) // 실제로는 API 호출로 대체될 부분

            _signUpState.value = _signUpState.value.copy(
                isEmailVerified = true,
                isLoading = false,
                error = null
            )
        }
    }

    private fun submitSignUp() {
        val state = _signUpState.value

        when {
            state.name.isEmpty() -> {
                _signUpState.value = state.copy(error = "닉네임을 입력해주세요.")
                return
            }
            state.username.isEmpty() -> {
                _signUpState.value = state.copy(error = "아이디를 입력해주세요.")
                return
            }
            !state.isUsernameVerified -> {
                _signUpState.value = state.copy(error = "아이디 중복확인을 해주세요.")
                return
            }
            state.email.isEmpty() -> {
                _signUpState.value = state.copy(error = "이메일을 입력해주세요.")
                return
            }
            !state.isEmailVerified -> {
                _signUpState.value = state.copy(error = "이메일 인증을 완료해주세요.")
                return
            }
            state.password.isEmpty() -> {
                _signUpState.value = state.copy(error = "비밀번호를 입력해주세요.")
                return
            }
            state.password != state.confirmPassword -> {
                _signUpState.value = state.copy(error = "비밀번호가 일치하지 않습니다.")
                return
            }
        }

        _signUpState.value = state.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000) // 실제로는 API 호출로 대체될 부분
            _signUpState.value = state.copy(isLoading = false, error = null)
            // TODO: 회원가입 성공 처리 (예: 로그인 화면으로 이동)
        }
    }
}