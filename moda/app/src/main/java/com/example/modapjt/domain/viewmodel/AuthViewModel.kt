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
            is LoginEvent.EmailChanged -> {
                _loginState.value = _loginState.value.copy(email = event.email)
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
        val email = _loginState.value.email
        val password = _loginState.value.password

        if (email == "user" && password == "1234") {
            _loginState.value = _loginState.value.copy(isLoading = true)

            viewModelScope.launch {
                delay(1000)
                _loginState.value = _loginState.value.copy(isLoading = false, error = null)
                onLoginSuccess()
            }
        } else {
            _loginState.value = _loginState.value.copy(error = "이메일 또는 비밀번호가 잘못되었습니다.")
        }
    }

    fun onSignUpEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    email = event.email,
                    isEmailVerified = false  // 이메일이 변경되면 인증 상태 초기화
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
            is SignUpEvent.Submit -> {
                submitSignUp()
            }
            is SignUpEvent.NextPage -> {
                if (canMoveToNextPage()) {
                    _signUpState.value = _signUpState.value.copy(currentPage = 2)
                }
            }
            is SignUpEvent.PreviousPage -> {
                _signUpState.value = _signUpState.value.copy(currentPage = 1)
            }
            is SignUpEvent.VerifyEmail -> {
                verifyEmail()
            }
        }
    }

    private fun canMoveToNextPage(): Boolean {
        return _signUpState.value.isEmailVerified &&
                _signUpState.value.name.isNotEmpty() &&
                _signUpState.value.email.isNotEmpty()
    }

    private fun verifyEmail() {
        viewModelScope.launch {
            _signUpState.value = _signUpState.value.copy(isLoading = true)
            delay(1000) // 실제로는 API 호출로 대체될 부분

            // 임시로 항상 사용 가능하다고 가정
            _signUpState.value = _signUpState.value.copy(
                isEmailVerified = true,
                isLoading = false
            )
        }
    }

    private fun submitSignUp() {
        _signUpState.value = _signUpState.value.copy(isLoading = true)

        if (_signUpState.value.password != _signUpState.value.confirmPassword) {
            _signUpState.value = _signUpState.value.copy(
                error = "비밀번호가 일치하지 않습니다.",
                isLoading = false
            )
            return
        }

        viewModelScope.launch {
            delay(1000) // 실제로는 API 호출로 대체될 부분
            _signUpState.value = _signUpState.value.copy(isLoading = false, error = null)
            // TODO: 회원가입 성공 처리 (예: 로그인 화면으로 이동)
        }
    }
}