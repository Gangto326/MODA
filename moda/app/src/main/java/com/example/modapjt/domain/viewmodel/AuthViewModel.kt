//package com.example.modapjt.domain.viewmodel
//
//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.modapjt.domain.model.FindIdEvent
//import com.example.modapjt.domain.model.FindIdState
//import com.example.modapjt.domain.model.FindPasswordEvent
//import com.example.modapjt.domain.model.FindPasswordState
//import com.example.modapjt.domain.model.LoginEvent
//import com.example.modapjt.domain.model.LoginState
//import com.example.modapjt.domain.model.SignUpEvent
//import com.example.modapjt.domain.model.SignUpState
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class AuthViewModel : ViewModel() {
//    private val _loginState = mutableStateOf(LoginState())
//    val loginState: State<LoginState> = _loginState
//
//    private val _signUpState = mutableStateOf(SignUpState())
//    val signUpState: State<SignUpState> = _signUpState
//
//    private var onLoginSuccess: () -> Unit = {}
//
//    fun setOnLoginSuccess(callback: () -> Unit) {
//        onLoginSuccess = callback
//    }
//
//    fun onLoginEvent(event: LoginEvent) {
//        when (event) {
//            is LoginEvent.UsernameChanged -> {
//                _loginState.value = _loginState.value.copy(username = event.username)
//            }
//            is LoginEvent.PasswordChanged -> {
//                _loginState.value = _loginState.value.copy(password = event.password)
//            }
//            is LoginEvent.Submit -> {
//                login()
//            }
//        }
//    }
//
//    private fun login() {
//        val username = _loginState.value.username
//        val password = _loginState.value.password
//
//        if (username.isEmpty() || password.isEmpty()) {
//            _loginState.value = _loginState.value.copy(error = "아이디와 비밀번호를 입력해주세요.")
//            return
//        }
//
//        // 임시 로그인 로직 (실제로는 API 호출로 대체)
//        if (username == "user" && password == "1234") {
//            _loginState.value = _loginState.value.copy(isLoading = true)
//
//            viewModelScope.launch {
//                delay(1000)
//                _loginState.value = _loginState.value.copy(isLoading = false, error = null)
//                onLoginSuccess()
//            }
//        } else {
//            _loginState.value = _loginState.value.copy(error = "아이디 또는 비밀번호가 잘못되었습니다.")
//        }
//    }
//
//    fun onSignUpEvent(event: SignUpEvent) {
//        when (event) {
//            is SignUpEvent.EmailChanged -> {
//                _signUpState.value = _signUpState.value.copy(
//                    email = event.email,
//                    isEmailVerified = false
//                )
//            }
//            is SignUpEvent.PasswordChanged -> {
//                _signUpState.value = _signUpState.value.copy(password = event.password)
//            }
//            is SignUpEvent.ConfirmPasswordChanged -> {
//                _signUpState.value = _signUpState.value.copy(confirmPassword = event.confirmPassword)
//            }
//            is SignUpEvent.NameChanged -> {
//                _signUpState.value = _signUpState.value.copy(name = event.name)
//            }
//            is SignUpEvent.UsernameChanged -> {
//                _signUpState.value = _signUpState.value.copy(
//                    username = event.username,
//                    isUsernameVerified = false
//                )
//            }
//            is SignUpEvent.EmailVerificationCodeChanged -> {
//                _signUpState.value = _signUpState.value.copy(
//                    emailVerificationCode = event.code
//                )
//            }
//            is SignUpEvent.VerifyUsername -> {
//                verifyUsername()
//            }
//            is SignUpEvent.SendEmailVerification -> {
//                sendEmailVerification()
//            }
//            is SignUpEvent.VerifyEmailCode -> {
//                verifyEmailCode()
//            }
//            is SignUpEvent.Submit -> {
//                submitSignUp()
//            }
//        }
//    }
//
//    private fun verifyUsername() {
//        val username = _signUpState.value.username
//        if (username.isEmpty()) {
//            _signUpState.value = _signUpState.value.copy(error = "아이디를 입력해주세요.")
//            return
//        }
//
//        viewModelScope.launch {
//            _signUpState.value = _signUpState.value.copy(isLoading = true)
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//
//            // 임시로 항상 사용 가능하다고 가정
//            _signUpState.value = _signUpState.value.copy(
//                isUsernameVerified = true,
//                isLoading = false,
//                error = null
//            )
//        }
//    }
//
//    private fun sendEmailVerification() {
//        val email = _signUpState.value.email
//        if (email.isEmpty()) {
//            _signUpState.value = _signUpState.value.copy(error = "이메일을 입력해주세요.")
//            return
//        }
//
//        viewModelScope.launch {
//            _signUpState.value = _signUpState.value.copy(isLoading = true)
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//
//            _signUpState.value = _signUpState.value.copy(
//                isEmailVerificationSent = true,
//                isLoading = false,
//                error = null
//            )
//        }
//    }
//
//    private fun verifyEmailCode() {
//        val code = _signUpState.value.emailVerificationCode
//        if (code.isEmpty()) {
//            _signUpState.value = _signUpState.value.copy(error = "인증번호를 입력해주세요.")
//            return
//        }
//
//        viewModelScope.launch {
//            _signUpState.value = _signUpState.value.copy(isLoading = true)
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//
//            _signUpState.value = _signUpState.value.copy(
//                isEmailVerified = true,
//                isLoading = false,
//                error = null
//            )
//        }
//    }
//
//    private fun submitSignUp() {
//        val state = _signUpState.value
//
//        when {
//            state.name.isEmpty() -> {
//                _signUpState.value = state.copy(error = "닉네임을 입력해주세요.")
//                return
//            }
//            state.username.isEmpty() -> {
//                _signUpState.value = state.copy(error = "아이디를 입력해주세요.")
//                return
//            }
//            !state.isUsernameVerified -> {
//                _signUpState.value = state.copy(error = "아이디 중복확인을 해주세요.")
//                return
//            }
//            state.email.isEmpty() -> {
//                _signUpState.value = state.copy(error = "이메일을 입력해주세요.")
//                return
//            }
//            !state.isEmailVerified -> {
//                _signUpState.value = state.copy(error = "이메일 인증을 완료해주세요.")
//                return
//            }
//            state.password.isEmpty() -> {
//                _signUpState.value = state.copy(error = "비밀번호를 입력해주세요.")
//                return
//            }
//            state.password != state.confirmPassword -> {
//                _signUpState.value = state.copy(error = "비밀번호가 일치하지 않습니다.")
//                return
//            }
//        }
//
//        _signUpState.value = state.copy(isLoading = true)
//
//        viewModelScope.launch {
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//            _signUpState.value = state.copy(isLoading = false, error = null)
//            // TODO: 회원가입 성공 처리 (예: 로그인 화면으로 이동)
//        }
//    }
//
//    private val _findIdState = mutableStateOf(FindIdState())
//    val findIdState: State<FindIdState> = _findIdState
//
//    private val _findPasswordState = mutableStateOf(FindPasswordState())
//    val findPasswordState: State<FindPasswordState> = _findPasswordState
//
//    fun onFindIdEvent(event: FindIdEvent) {
//        when (event) {
//            is FindIdEvent.EmailChanged -> {
//                _findIdState.value = _findIdState.value.copy(
//                    email = event.email,
//                    isEmailVerified = false
//                )
//            }
//            is FindIdEvent.VerificationCodeChanged -> {
//                _findIdState.value = _findIdState.value.copy(
//                    verificationCode = event.code
//                )
//            }
//            is FindIdEvent.SendVerification -> {
//                sendFindIdVerification()
//            }
//            is FindIdEvent.VerifyCode -> {
//                verifyFindIdCode()
//            }
//        }
//    }
//
//    private fun sendFindIdVerification() {
//        val email = _findIdState.value.email
//        if (email.isEmpty()) {
//            _findIdState.value = _findIdState.value.copy(error = "이메일을 입력해주세요.")
//            return
//        }
//
//        viewModelScope.launch {
//            _findIdState.value = _findIdState.value.copy(isLoading = true)
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//
//            _findIdState.value = _findIdState.value.copy(
//                isEmailVerificationSent = true,
//                isLoading = false,
//                error = null
//            )
//        }
//    }
//
//    private fun verifyFindIdCode() {
//        val code = _findIdState.value.verificationCode
//        if (code.isEmpty()) {
//            _findIdState.value = _findIdState.value.copy(error = "인증번호를 입력해주세요.")
//            return
//        }
//
//        viewModelScope.launch {
//            _findIdState.value = _findIdState.value.copy(isLoading = true)
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//
//            // 임시로 항상 성공하고 가상의 아이디를 반환
//            _findIdState.value = _findIdState.value.copy(
//                isEmailVerified = true,
//                foundUsername = "found_user123",
//                isLoading = false,
//                error = null
//            )
//        }
//    }
//
//    fun onFindPasswordEvent(event: FindPasswordEvent) {
//        when (event) {
//            is FindPasswordEvent.UsernameChanged -> {
//                _findPasswordState.value = _findPasswordState.value.copy(
//                    username = event.username
//                )
//            }
//            is FindPasswordEvent.EmailChanged -> {
//                _findPasswordState.value = _findPasswordState.value.copy(
//                    email = event.email,
//                    isEmailVerified = false
//                )
//            }
//            is FindPasswordEvent.VerificationCodeChanged -> {
//                _findPasswordState.value = _findPasswordState.value.copy(
//                    verificationCode = event.code
//                )
//            }
//            is FindPasswordEvent.NewPasswordChanged -> {
//                _findPasswordState.value = _findPasswordState.value.copy(
//                    newPassword = event.password
//                )
//            }
//            is FindPasswordEvent.ConfirmNewPasswordChanged -> {
//                _findPasswordState.value = _findPasswordState.value.copy(
//                    confirmNewPassword = event.password
//                )
//            }
//            is FindPasswordEvent.SendVerification -> {
//                sendFindPasswordVerification()
//            }
//            is FindPasswordEvent.VerifyCode -> {
//                verifyFindPasswordCode()
//            }
//            is FindPasswordEvent.SubmitNewPassword -> {
//                submitNewPassword()
//            }
//        }
//    }
//
//    private fun sendFindPasswordVerification() {
//        val state = _findPasswordState.value
//        if (state.email.isEmpty()) {
//            _findPasswordState.value = state.copy(error = "이메일을 입력해주세요.")
//            return
//        }
//        if (state.username.isEmpty()) {
//            _findPasswordState.value = state.copy(error = "아이디를 입력해주세요.")
//            return
//        }
//
//        viewModelScope.launch {
//            _findPasswordState.value = state.copy(isLoading = true)
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//
//            _findPasswordState.value = state.copy(
//                isEmailVerificationSent = true,
//                isLoading = false,
//                error = null
//            )
//        }
//    }
//
//    private fun verifyFindPasswordCode() {
//        val state = _findPasswordState.value
//        if (state.verificationCode.isEmpty()) {
//            _findPasswordState.value = state.copy(error = "인증번호를 입력해주세요.")
//            return
//        }
//
//        viewModelScope.launch {
//            _findPasswordState.value = state.copy(isLoading = true)
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//
//            _findPasswordState.value = state.copy(
//                isEmailVerified = true,
//                canChangePassword = true,
//                isLoading = false,
//                error = null
//            )
//        }
//    }
//
//    private fun submitNewPassword() {
//        val state = _findPasswordState.value
//        when {
//            state.newPassword.isEmpty() -> {
//                _findPasswordState.value = state.copy(error = "새 비밀번호를 입력해주세요.")
//                return
//            }
//            state.newPassword != state.confirmNewPassword -> {
//                _findPasswordState.value = state.copy(error = "비밀번호가 일치하지 않습니다.")
//                return
//            }
//        }
//
//        viewModelScope.launch {
//            _findPasswordState.value = state.copy(isLoading = true)
//            delay(1000) // 실제로는 API 호출로 대체될 부분
//
//            // 비밀번호 변경 성공 후 상태 초기화
//            _findPasswordState.value = FindPasswordState()
//            // TODO: 성공 메시지 표시 또는 로그인 화면으로 이동
//        }
//    }
//
//    // 상태 초기화 함수들
//    fun resetFindIdState() {
//        _findIdState.value = FindIdState()
//    }
//
//    fun resetFindPasswordState() {
//        _findPasswordState.value = FindPasswordState()
//    }
//}
package com.example.modapjt.domain.viewmodel

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
        viewModelScope.launch {
            val token = tokenManager.getTokenSync()
            _isLoggedIn.value = !token.isNullOrEmpty()
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

//    private fun login() {
//        val username = _loginState.value.username
//        val password = _loginState.value.password
//
//        if (username.isEmpty() || password.isEmpty()) {
//            _loginState.value = _loginState.value.copy(error = "아이디와 비밀번호를 입력해주세요.")
//            return
//        }
//
//        viewModelScope.launch {
//            try {
//                _loginState.value = _loginState.value.copy(isLoading = true)
//
//                val response = RetrofitInstance.getUserApi(tokenManager)
//                    .login(LoginRequest(username, password))
//
//                if (response.isSuccessful && response.body() == true) {
//                    // 헤더에서 토큰 가져오기
//                    val token = response.headers()["Authorization"]?.replace("Bearer ", "")
//
//                    if (!token.isNullOrEmpty()) {
//                        tokenManager.saveToken(token)
//                        _isLoggedIn.value = true
//                        _loginState.value = _loginState.value.copy(isLoading = false, error = null)
//                        onLoginSuccess()
//                    } else {
//                        _isLoggedIn.value = false
//                        _loginState.value = _loginState.value.copy(
//                            isLoading = false,
//                            error = "인증 토큰을 받지 못했습니다."
//                        )
//                    }
//                } else {
//                    _isLoggedIn.value = false
//                    _loginState.value = _loginState.value.copy(
//                        isLoading = false,
//                        error = "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다."
//                    )
//                }
//            } catch (e: Exception) {
//                _isLoggedIn.value = false
//                _loginState.value = _loginState.value.copy(
//                    isLoading = false,
//                    error = "네트워크 오류: ${e.message}"
//                )
//            }
//        }
//    }
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

                val response = RetrofitInstance.getUserApi(tokenManager)
                    .login(LoginRequest(username, password))

                if (response.isSuccessful && response.body() == true) {
                    // TokenInterceptor에서 자동으로 토큰을 저장함
                    _isLoggedIn.value = true
                    _loginState.value = _loginState.value.copy(isLoading = false, error = null)
                    onLoginSuccess()
                } else {
                    _isLoggedIn.value = false
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        error = "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다."
                    )
                }
            } catch (e: Exception) {
                _isLoggedIn.value = false
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    error = "네트워크 오류: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _isLoggedIn.value = false
        }
    }
}


class AuthViewModelFactory(
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}