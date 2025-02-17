package com.example.modapjt.domain.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.FindPasswordRepository
import com.example.modapjt.domain.model.FindPasswordEvent
import com.example.modapjt.domain.model.FindPasswordState
import com.example.modapjt.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FindPasswordViewModel : ViewModel() {
    private val repository = FindPasswordRepository()

    private val _state = mutableStateOf(FindPasswordState())
    val state: State<FindPasswordState> = _state

    private var verificationJob: Job? = null
    private var timerJob: Job? = null

    fun onFindPasswordEvent(event: FindPasswordEvent) {
        when (event) {
            is FindPasswordEvent.UsernameChanged -> {
                _state.value = _state.value.copy(username = event.username)
            }
            is FindPasswordEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is FindPasswordEvent.VerificationCodeChanged -> {
                _state.value = _state.value.copy(verificationCode = event.code)
            }
            is FindPasswordEvent.NewPasswordChanged -> {
                _state.value = _state.value.copy(newPassword = event.password)
            }
            is FindPasswordEvent.ConfirmNewPasswordChanged -> {
                _state.value = _state.value.copy(confirmNewPassword = event.password)
            }
            is FindPasswordEvent.SendVerification -> {
                sendVerificationEmail()
            }
            is FindPasswordEvent.VerifyCode -> {
                verifyCode()
            }
            is FindPasswordEvent.SubmitNewPassword -> {
                submitNewPassword()
            }
        }
    }

    fun resetState() {
        _state.value = _state.value.copy(
            username = "",
            email = "",
            verificationCode = "",
            newPassword = "",
            confirmNewPassword = "",
            isEmailVerificationSent = false,
            canChangePassword = false,
            isLoading = false,
            error = null,
            remainingTime = 0,
            isPasswordResetSuccessful = false  // 이 상태도 초기화
        )
    }

    private fun sendVerificationEmail() {
        if (_state.value.email.isEmpty()) {
            _state.value = _state.value.copy(error = "이메일을 입력해주세요.")
            return
        }

        repository.sendVerificationEmail(_state.value.email)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data) {
                            _state.value = _state.value.copy(
                                isEmailVerificationSent = true,
                                error = null
                            )
                            startVerificationTimer()
                        } else {
                            _state.value = _state.value.copy(
                                error = "이메일 전송에 실패했습니다."
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun verifyCode() {
        if (_state.value.username.isEmpty()) {
            _state.value = _state.value.copy(error = "아이디를 입력해주세요.")
            return
        }
        if (_state.value.verificationCode.isEmpty()) {
            _state.value = _state.value.copy(error = "인증번호를 입력해주세요.")
            return
        }

        repository.verifyPasswordChangeCode(
            _state.value.email,
            _state.value.username,
            _state.value.verificationCode
        )
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data) {
                            _state.value = _state.value.copy(
                                canChangePassword = true,
                                error = null
                            )
                            timerJob?.cancel()
                        } else {
                            _state.value = _state.value.copy(
                                error = "잘못된 아이디/인증번호입니다."
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun submitNewPassword() {
        if (_state.value.newPassword != _state.value.confirmNewPassword) {
            _state.value = _state.value.copy(error = "비밀번호가 일치하지 않습니다.")
            return
        }

        repository.resetPassword(
            _state.value.email,
            _state.value.username,
            _state.value.newPassword,
            _state.value.confirmNewPassword
        )
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data) {
                            _state.value = _state.value.copy(
                                isPasswordResetSuccessful = true
                            )
                        } else {
                            _state.value = _state.value.copy(
                                error = "비밀번호 변경에 실패했습니다."
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }


    private fun startVerificationTimer() {
        timerJob?.cancel()
        _state.value = _state.value.copy(remainingTime = 600) // 10분 = 600초

        timerJob = viewModelScope.launch {
            while (_state.value.remainingTime > 0) {
                delay(1000) // 1초 대기
                _state.value = _state.value.copy(
                    remainingTime = _state.value.remainingTime - 1
                )
            }
            // 시간 초과 시 처리
            if (_state.value.remainingTime <= 0) {
                _state.value = _state.value.copy(
                    isEmailVerificationSent = false,
                    error = "인증 시간이 만료되었습니다. 다시 시도해주세요."
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        verificationJob?.cancel()
    }
}