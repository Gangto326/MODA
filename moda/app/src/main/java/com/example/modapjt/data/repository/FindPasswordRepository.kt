package com.example.modapjt.data.repository

import com.example.modapjt.data.api.FindPasswordApiService
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.request.EmailVerificationRequest
import com.example.modapjt.data.dto.request.ResetPasswordRequest
import com.example.modapjt.data.dto.request.VerifyPasswordChangeRequest
import com.example.modapjt.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FindPasswordRepository {
    private val api: FindPasswordApiService = RetrofitInstance.findPasswordApi

    // 이메일 인증번호 발송
    fun sendVerificationEmail(email: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.sendVerificationEmail(EmailVerificationRequest(email))
            emit(Resource.Success(response))
        } catch (e: IOException) {
            emit(Resource.Error("네트워크 오류가 발생했습니다."))
        } catch (e: Exception) {
            emit(Resource.Error("알 수 없는 오류가 발생했습니다."))
        }
    }

    // 인증번호 확인
    fun verifyPasswordChangeCode(
        email: String,
        userId: String,
        code: String
    ): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.verifyPasswordChangeCode(
                VerifyPasswordChangeRequest(email, userId, code)
            )
            emit(Resource.Success(response))
        } catch (e: IOException) {
            emit(Resource.Error("네트워크 오류가 발생했습니다."))
        } catch (e: Exception) {
            emit(Resource.Error("알 수 없는 오류가 발생했습니다."))
        }
    }

    // 비밀번호 재설정
    fun resetPassword(
        email: String,
        userId: String,
        newPassword: String,
        newPasswordConfirm: String
    ): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.resetPassword(
                ResetPasswordRequest(email, newPassword, newPasswordConfirm, userId)
            )
            emit(Resource.Success(response))
        } catch (e: IOException) {
            emit(Resource.Error("네트워크 오류가 발생했습니다."))
        } catch (e: Exception) {
            emit(Resource.Error("알 수 없는 오류가 발생했습니다."))
        }
    }
}