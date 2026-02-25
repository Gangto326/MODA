package com.example.modapjt.domain.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.FindIdRepository
import com.example.modapjt.domain.model.SignUpState
import com.example.modapjt.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FindIdViewModel : ViewModel() {
    private val repository = FindIdRepository()
    private var timerJob: Job? = null

    private val _state = mutableStateOf(FindIdState())
    val state: State<FindIdState> = _state


    fun onEmailChanged(email: String) {
        _state.value = state.value.copy(
            email = email,
            error = null
        )
    }

    fun onVerificationCodeChanged(code: String) {
        _state.value = state.value.copy(
            verificationCode = code,
            error = null
        )
    }


    private fun startTimer() {
        timerJob?.cancel() // 기존 타이머가 있다면 취소
        _state.value = _state.value.copy(
            remainingTime = 600,  // 10분 = 600초
            isTimerRunning = true
        )

        timerJob = viewModelScope.launch {
            while (_state.value.remainingTime > 0) {
                delay(1000) // 1초 대기
                _state.value = _state.value.copy(
                    remainingTime = _state.value.remainingTime - 1
                )
            }
            // 시간 초과 처리
            if (_state.value.remainingTime <= 0) {
                _state.value = _state.value.copy(
                    isEmailVerificationSent = false,
                    isTimerRunning = false,
                    emailVerificationMessage = "인증 시간이 만료되었습니다."
                )
            }
        }
    }

    fun sendVerification() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)

            when (val result = repository.sendEmailVerification(state.value.email)) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        isEmailVerificationSent = result.data == "true",
                        error = null,
                        isLoading = false
                    )
                    if (result.data == "true") {
                        startTimer() // 인증번호 전송 성공시 타이머 시작
                    }
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun resetState() {
        timerJob?.cancel() // 타이머 취소
        _state.value = _state.value.copy(
            email = "",
            verificationCode = "",
            isEmailVerificationSent = false,
            foundId = null,
            error = null,
            isLoading = false,
            isTimerRunning = false,
            remainingTime = 0,
            emailVerificationMessage = null
        )
    }


    fun verifyAndFindId() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)

            Log.d("FindIdViewModel", "verifyAndFindId() 실행됨")

            when (val result = repository.findUserId(
                email = state.value.email,
                code = state.value.verificationCode
            )) {
                is Resource.Success -> {
                    Log.d("FindIdViewModel", "아이디 찾기 성공: ${result.data}")
                    _state.value = state.value.copy(
                        foundId = result.data,
                        error = null,
                        isLoading = false
                    )
                    timerJob?.cancel() // 인증 성공시 타이머 중지
                }
                is Resource.Error -> {
                    Log.e("FindIdViewModel", "아이디 찾기 실패: ${result.message}")
                    _state.value = state.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel() // ViewModel이 제거될 때 타이머 취소
    }
}

data class FindIdState(
    val email: String = "",
    val verificationCode: String = "",
    val isEmailVerificationSent: Boolean = false,
    val foundId: String? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val isTimerRunning: Boolean = false,
    val remainingTime: Int = 600,
    val emailVerificationMessage: String? = null,
)