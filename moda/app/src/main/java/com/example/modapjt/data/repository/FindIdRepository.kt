package com.example.modapjt.data.repository

import com.example.modapjt.data.api.FindIdApiService
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.request.EmailVerificationRequest
import com.example.modapjt.data.dto.request.FindUserIdRequest
import com.example.modapjt.utils.Resource
import java.io.IOException

class FindIdRepository {
    private val findIdApiService: FindIdApiService = RetrofitInstance.findIdApi

    suspend fun sendEmailVerification(email: String): Resource<Boolean> {
        return try {
            val response = findIdApiService.sendEmailVerification(
                EmailVerificationRequest(email = email)
            )
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error("네트워크 오류가 발생했습니다.")
        } catch (e: Exception) {
            Resource.Error("인증번호 전송에 실패했습니다.")
        }
    }

    suspend fun findUserId(email: String, code: String): Resource<String> {
        return try {
            val response = findIdApiService.findUserId(
                FindUserIdRequest(
                    email = email,
                    code = code
                )
            )
            if (response == "잘못된 입력입니다") {
                Resource.Error("가입된 아이디가 없습니다.")
            } else {
                Resource.Success(response)
            }
        } catch (e: IOException) {
            Resource.Error("네트워크 오류가 발생했습니다.")
        } catch (e: Exception) {
            Resource.Error("아이디 찾기에 실패했습니다.")
        }
    }
}