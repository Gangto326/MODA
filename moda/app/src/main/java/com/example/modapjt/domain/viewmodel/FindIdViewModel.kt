package com.example.modapjt.domain.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.repository.FindIdRepository
import com.example.modapjt.utils.Resource
import kotlinx.coroutines.launch

class FindIdViewModel : ViewModel() {
    private val repository = FindIdRepository()

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

    fun sendVerification() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)

            when (val result = repository.sendEmailVerification(state.value.email)) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        isEmailVerificationSent = result.data == "true",  // String 비교로 변경
                        error = null,
                        isLoading = false
                    )
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

    fun verifyAndFindId() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)

            when (val result = repository.findUserId(
                email = state.value.email,
                code = state.value.verificationCode
            )) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        foundId = result.data,
                        error = null,
                        isLoading = false
                    )
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
}

data class FindIdState(
    val email: String = "",
    val verificationCode: String = "",
    val isEmailVerificationSent: Boolean = false,
    val foundId: String? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)