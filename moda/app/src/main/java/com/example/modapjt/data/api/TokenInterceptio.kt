package com.example.modapjt.data.api

import com.example.modapjt.data.storage.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 기존 토큰이 있다면 요청에 추가
        val originalRequest = chain.request()
        val token = tokenManager.getTokenSync()
        println("TokenInterceptor - 현재 토큰: $token")  // 토큰 값 확인

        val modifiedRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        // 요청 실행
        val response = chain.proceed(modifiedRequest)

        // 로그인 응답에서 새 토큰 확인
        if (originalRequest.url.encodedPath.endsWith("/login")) {
            response.header("Authorization")?.let { authHeader ->
                val newToken = authHeader.replace("Bearer ", "")
                tokenManager.saveToken(newToken)
            }
        }

        return response
    }
}
