package com.example.modapjt.data.api

import android.util.Log
import com.example.modapjt.data.storage.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class TokenInterceptor(private val tokenManager: TokenManager) : Interceptor {
    // 로그를 위한 태그
    companion object {
        private const val TAG = "TokenInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // 원본 요청에 엑세스 토큰 추가
        val originalRequest = chain.request()
        val accessToken = tokenManager.getAccessToken()

        // 원본 요청 정보 로깅
        Log.d(TAG, """
        Original Request:
        URL: ${originalRequest.url}
        Method: ${originalRequest.method}
        Headers: ${originalRequest.headers}
    """.trimIndent())


        Log.d(TAG, "Original AccessToken: $accessToken")

        // 토큰이 있으면 헤더에 추가
        val requestWithToken = if (!accessToken.isNullOrEmpty()) {
            Log.d(TAG, "-------------Adding token to request: $accessToken")
            originalRequest.newBuilder()
                .addHeader("Authorization", accessToken)  // 이미 "Bearer " 포함되어 있으므로 그대로 사용
                .build()
        } else {
            Log.d(TAG, "No token available, proceeding with original request")
            originalRequest
        }

        // 첫 번째 요청 실행
        var response = chain.proceed(requestWithToken)
        Log.d(TAG, "Response code: ${response.code}")
//        Log.d(TAG, "----- ${response.body}")
        // 응답 body 내용 확인
        response.body?.let { body ->
            val bodyString = body.string()
            Log.d(TAG, "Response body: $bodyString")

            // body를 읽은 후에는 새로운 response를 만들어야 합니다
            // 그렇지 않으면 body가 이미 소비되어 다음 사용에서 오류가 발생할 수 있습니다
            response = response.newBuilder()
                .body(bodyString.toResponseBody(body.contentType()))
                .build()
        }

        // 401 응답(토큰 만료)인 경우 & 500 에러도 갱신 시도
        if (response.code == 401) {
            Log.d(TAG, "Received 401, attempting token refresh")

//            response.close() // 원본 응답 닫기
            // (대신) -> 원본 응답의 body를 먼저 소비하고 닫기
            response.body?.close()

            // Refresh 토큰으로 새 액세스 토큰 요청
//            val refreshRequest = originalRequest.newBuilder()
//                .url("http://10.0.2.2:8080/api/auth/refresh")
//                .method("POST", RequestBody.create(null, ByteArray(0))) // 빈 body 추가
//                .build()
            val refreshRequest = Request.Builder()
                .url("http://10.0.2.2:8080/api/auth/refresh")
                .get()
                .build()

            Log.d(TAG, "Sending refresh request")

            // 리프레시 요청 실행
            val refreshResponse = chain.proceed(refreshRequest)
            Log.d(TAG, "Refresh response code: ${refreshResponse.code}")

            try {
                if (refreshResponse.isSuccessful) {
                    val newAccessToken = refreshResponse.header("Authorization")
                    Log.d(TAG, "Got new access token: $newAccessToken")

                    if (!newAccessToken.isNullOrEmpty()) {
                        tokenManager.saveAccessToken(newAccessToken)

                        // 원본 요청 재시도
                        val newRequest = originalRequest.newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", newAccessToken)
                            .build()

                        // 리프레시 응답 닫기
                        refreshResponse.close()

                        // 새 토큰으로 원본 요청 재시도
                        return chain.proceed(newRequest)
                    }
                }
                Log.e(TAG, "Refresh token failed: ${refreshResponse.code}")
                refreshResponse.close()
                tokenManager.clearTokens()
            } catch (e: Exception) {
                Log.e(TAG, "Error during refresh", e)
                refreshResponse.close()
                tokenManager.clearTokens()
            }
        }
        return response
    }
}
