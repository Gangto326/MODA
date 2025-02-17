package com.example.modapjt.data.repository

import android.util.Log
import com.example.modapjt.data.api.SignUpApiService
import com.example.modapjt.data.dto.request.EmailVerificationRequest
import com.example.modapjt.data.dto.request.SignUpRequest
import com.example.modapjt.data.dto.request.UserNameRequest
import com.example.modapjt.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignUpRepository(private val api: SignUpApiService) {

    suspend fun checkUsername(username: String): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val request = UserNameRequest(username)
                val response = api.checkUsername(request)
                // true = 중복된 아이디, false = 사용 가능한 아이디
                Resource.Success(response)  // 응답 그대로 반환
            } catch (e: Exception) {
                Resource.Error(e.message ?: "아이디 중복 확인 중 오류가 발생했습니다.")
            }
        }
    }

    suspend fun sendEmailVerification(email: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val request = EmailVerificationRequest(email)
                val success = api.sendEmailVerification(request)
                if (success) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error("이메일 인증 코드 전송에 실패했습니다.")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "이메일 인증 코드 전송 중 오류가 발생했습니다.")
            }
        }
    }

    suspend fun verifyEmail(email: String, code: String): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val request = EmailVerificationRequest(email, code)
                val success = api.verifyEmailCode(request)
                Resource.Success(success)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "이메일 인증 코드 확인 중 오류가 발생했습니다.")
            }
        }
    }

    suspend fun signUp(
        email: String,
        username: String,
        password: String,
        nickname: String
    ): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val request = SignUpRequest(
                    email = email,
                    userName = username,  // API 스펙과 정확히 일치
                    password = password,
                    nickname = nickname
                )

                // 요청 데이터 로깅
                Log.d("SignUpRepository", "SignUp request: $request")

                val response = api.signUp(request)
                Log.d("SignUpRepository", "SignUp response: $response")

                if (response.success) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error(response.message)
                }
            } catch (e: Exception) {
                Log.e("SignUpRepository", "SignUp error", e)
                Resource.Error(e.message ?: "회원가입 중 오류가 발생했습니다.")
            }
        }
    }
}
