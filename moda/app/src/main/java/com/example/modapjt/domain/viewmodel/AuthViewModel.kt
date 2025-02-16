package com.example.modapjt.domain.viewmodel

//import androidx.lifecycle.ViewModelProvider
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.request.LoginRequest
import com.example.modapjt.data.storage.TokenManager
import com.example.modapjt.domain.model.LoginEvent
import com.example.modapjt.domain.model.LoginState
import kotlinx.coroutines.launch


class AuthViewModel(private val tokenManager: TokenManager) : ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    init {
        checkLoginStatus()
    }
    private var onLoginSuccess: () -> Unit = {}


    fun setOnLoginSuccess(callback: () -> Unit) {
        onLoginSuccess = callback
    }

    fun checkLoginStatus() {
        _isLoggedIn.value = !tokenManager.getAccessToken().isNullOrEmpty()
    }

//    fun logout() {
//        viewModelScope.launch {
//            tokenManager.clearTokens() // 토큰 삭제
//            _isLoggedIn.value = false
//        }
//    }
    // -> 토큰 삭제만 하고 API요청안하고 있어서 아래 코드로 변경
    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApi.logout()

                if (response.isSuccessful) {
                    tokenManager.clearTokens() // 토큰 삭제
                    _isLoggedIn.value = false
                    onLogoutSuccess() // 로그아웃 성공 시 실행할 콜백
                } else {
                    Log.e("AuthViewModel", "로그아웃 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "로그아웃 중 오류 발생", e)
            }
        }
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

        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(isLoading = true)

                val response = RetrofitInstance.userApi.login(LoginRequest(username, password))

                if (response.isSuccessful) {
                    // ✅ 헤더에서 Access Token 가져오기
                    val accessToken = response.headers()["Authorization"]
                    print(accessToken)
//                    val refreshToken = response.headers()["Refresh-Token"] // -> 헤더 X, 쿠키에 있음
//                    print(refreshToken)
                    if (!accessToken.isNullOrEmpty()) {
                        tokenManager.saveAccessToken(accessToken)
                        _isLoggedIn.value = true
                        _loginState.value = _loginState.value.copy(isLoading = false, error = null)
                        onLoginSuccess() // 여기에 콜백 실행 추가
                    } else {
                        _loginState.value = _loginState.value.copy(
                            isLoading = false,
                            error = "서버 응답에 토큰이 없습니다."
                        )
                    }
                } else {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        error = "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다."
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error", e) // 로깅 추가
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    error = "네트워크 오류: ${e.message}"
                )
            }
        }
    }
}



class AuthViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

