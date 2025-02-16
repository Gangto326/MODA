package com.example.modapjt.presentation.auth.signup

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.repository.SignUpRepository
import com.example.modapjt.domain.model.SignUpEvent
import com.example.modapjt.domain.model.SignUpState
import com.example.modapjt.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val _signUpState = mutableStateOf(SignUpState())
    val signUpState: State<SignUpState> = _signUpState

    private var timerJob: Job? = null
    private val repository = SignUpRepository(RetrofitInstance.signupApi)

    sealed class UiEvent {
        object SignUpSuccess : UiEvent()
        data class ShowError(val message: String) : UiEvent()
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    name = event.name,
                    error = null
                )
            }

            is SignUpEvent.UsernameChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    username = event.username,
                    isUsernameVerified = false,
                    usernameVerificationMessage = null,
                    error = null
                )
            }

            is SignUpEvent.EmailChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    email = event.email,
                    isEmailVerified = false,
                    emailVerificationMessage = null,
                    error = null
                )
            }

            is SignUpEvent.EmailVerificationCodeChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    emailVerificationCode = event.code,
                    error = null
                )
            }

            is SignUpEvent.PasswordChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    password = event.password,
                    passwordMatchMessage = checkPasswordMatch(event.password, _signUpState.value.confirmPassword),
                    error = null
                )
            }

            is SignUpEvent.ConfirmPasswordChanged -> {
                _signUpState.value = _signUpState.value.copy(
                    confirmPassword = event.confirmPassword,
                    passwordMatchMessage = checkPasswordMatch(_signUpState.value.password, event.confirmPassword),
                    error = null
                )
            }

            is SignUpEvent.VerifyUsername -> verifyUsername()
            is SignUpEvent.SendEmailVerification -> sendEmailVerification()
            is SignUpEvent.VerifyEmailCode -> verifyEmailCode()
            is SignUpEvent.Submit -> submitSignUp()
        }
    }

    private fun checkPasswordMatch(password: String, confirmPassword: String): String? {
        return if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
            "비밀번호가 일치하지 않습니다."
        } else {
            null
        }
    }

    private fun verifyUsername() {
        val username = _signUpState.value.username
        if (username.isEmpty()) {
            _signUpState.value = _signUpState.value.copy(
                usernameVerificationMessage = "아이디를 입력해주세요."
            )
            return
        }

        viewModelScope.launch {
            _signUpState.value = _signUpState.value.copy(isLoading = true)
            when (val result = repository.checkUsername(username)) {
                is Resource.Success -> {
                    _signUpState.value = _signUpState.value.copy(
                        isUsernameVerified = !result.data,  // 여기서 NOT 연산 추가
                        usernameVerificationMessage = if (result.data)
                            "이미 사용중인 아이디입니다."
                        else
                            "사용 가능한 아이디입니다.",
                        isLoading = false
                    )
                }


                is Resource.Error -> {
                    _signUpState.value = _signUpState.value.copy(
                        usernameVerificationMessage = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _signUpState.value = _signUpState.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun startEmailVerificationTimer() {
        timerJob?.cancel()
        _signUpState.value = _signUpState.value.copy(
            remainingTime = 600,
            isTimerRunning = true
        )

        timerJob = viewModelScope.launch {
            while (_signUpState.value.remainingTime > 0 && _signUpState.value.isTimerRunning) {
                delay(1000)
                _signUpState.value = _signUpState.value.copy(
                    remainingTime = _signUpState.value.remainingTime - 1
                )
            }
            if (_signUpState.value.remainingTime <= 0) {
                _signUpState.value = _signUpState.value.copy(
                    isTimerRunning = false,
                    emailVerificationMessage = "인증 시간이 만료되었습니다. 다시 시도해주세요."
                )
            }
        }
    }

    private fun sendEmailVerification() {
        val email = _signUpState.value.email
        if (!_signUpState.value.isEmailFieldValid()) {
            _signUpState.value = _signUpState.value.copy(
                emailVerificationMessage = "이메일을 입력해주세요."
            )
            return
        }

        viewModelScope.launch {
            _signUpState.value = _signUpState.value.copy(isLoading = true)
            when (val result = repository.sendEmailVerification(email)) {
                is Resource.Success -> {
                    _signUpState.value = _signUpState.value.copy(
                        isEmailVerificationSent = true,
                        isLoading = false,
                        emailVerificationMessage = "인증번호가 전송되었습니다."
                    )
                    startEmailVerificationTimer()
                }
                is Resource.Error -> {
                    _signUpState.value = _signUpState.value.copy(
                        emailVerificationMessage = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _signUpState.value = _signUpState.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun verifyEmailCode() {
        val state = _signUpState.value
        val code = state.emailVerificationCode
        if (code.isEmpty()) {
            _signUpState.value = state.copy(
                emailVerificationMessage = "인증번호를 입력해주세요."
            )
            return
        }

        viewModelScope.launch {
            _signUpState.value = state.copy(isLoading = true)
            when (val result = repository.verifyEmail(state.email, code)) {
                is Resource.Success -> {
                    _signUpState.value = state.copy(
                        isEmailVerified = result.data ?: false,
                        isLoading = false,
                        emailVerificationMessage = if (result.data == true)
                            "인증되었습니다."
                        else
                            "인증번호가 올바르지 않습니다.",
                        isTimerRunning = false
                    )
                    if (result.data == true) {
                        timerJob?.cancel()
                    }
                }
                is Resource.Error -> {
                    _signUpState.value = state.copy(
                        emailVerificationMessage = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _signUpState.value = state.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun submitSignUp() {
        val state = _signUpState.value

        // 현재 상태 로깅
        Log.d("SignUpViewModel", "Submit state: $state")

        when {
            state.name.isEmpty() -> {
                _signUpState.value = state.copy(error = "닉네임을 입력해주세요.")
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowError("닉네임을 입력해주세요."))
                }
                return
            }
            state.username.isEmpty() -> {
                _signUpState.value = state.copy(error = "아이디를 입력해주세요.")
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowError("아이디를 입력해주세요."))
                }
                return
            }
            !state.isUsernameVerified -> {
                _signUpState.value = state.copy(error = "아이디 중복확인을 해주세요.")
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowError("아이디 중복확인을 해주세요."))
                }
                return
            }
            state.email.isEmpty() -> {
                _signUpState.value = state.copy(error = "이메일을 입력해주세요.")
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowError("이메일을 입력해주세요."))
                }
                return
            }
            !state.isEmailVerified -> {
                _signUpState.value = state.copy(error = "이메일 인증을 완료해주세요.")
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowError("이메일 인증을 완료해주세요."))
                }
                return
            }
            state.password.isEmpty() -> {
                _signUpState.value = state.copy(error = "비밀번호를 입력해주세요.")
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowError("비밀번호를 입력해주세요."))
                }
                return
            }
            state.confirmPassword.isEmpty() -> {
                _signUpState.value = state.copy(error = "비밀번호 확인을 입력해주세요.")
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowError("비밀번호 확인을 입력해주세요."))
                }
                return
            }
            state.password != state.confirmPassword -> {
                _signUpState.value = state.copy(error = "비밀번호가 일치하지 않습니다.")
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowError("비밀번호가 일치하지 않습니다."))
                }
                return
            }
        }

        viewModelScope.launch {
            _signUpState.value = state.copy(isLoading = true)

            // 요청 데이터 로깅
            Log.d("SignUpViewModel", "Request data: email=${state.email}, " +
                    "username=${state.username}, nickname=${state.name}")

            when (val result = repository.signUp(
                email = state.email,
                username = state.username,
                password = state.password,
                nickname = state.name
            )) {
                is Resource.Success -> {
                    Log.d("SignUpViewModel", "Sign up success")
                    _signUpState.value = state.copy(
                        isLoading = false,
                        error = null
                    )
                    _uiEvent.send(UiEvent.SignUpSuccess) // 🚀 로그인 페이지로 이동
                }
                is Resource.Error -> {
                    Log.e("SignUpViewModel", "Sign up error: ${result.message}")

                    // ✅ "회원가입이 완료되었습니다." 메시지가 포함되어 있으면 성공 처리
                    if (result.message.contains("회원가입이 완료되었습니다.")) {
                        Log.d("SignUpViewModel", "회원가입 성공 메시지 감지 → 성공으로 처리")
                        _signUpState.value = state.copy(
                            isLoading = false,
                            error = null
                        )
                        _uiEvent.send(UiEvent.SignUpSuccess) // 🚀 로그인 페이지로 이동
                    } else {
                        _signUpState.value = state.copy(
                            error = result.message,
                            isLoading = false
                        )
                        _uiEvent.send(UiEvent.ShowError(result.message)) // ❌ 실제 에러만 ShowError 처리
                    }
                }



            is Resource.Loading -> {
                    _signUpState.value = state.copy(
                        isLoading = true
                    )
                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

