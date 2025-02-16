package com.example.modapjt.data.api

import retrofit2.Call
import retrofit2.http.POST

//interface AuthApiService {
//    @POST("api/auth/refresh")
//    fun refreshToken(@Header("Authorization") refreshToken: String): Call<RefreshTokenResponse>
//}
//
//data class RefreshTokenResponse(val accessToken: String)




interface AuthApiService {
//    @POST("/api/auth/refresh")
//    fun refreshToken(@Header("Cookie") refreshToken: String): Call<TokenResponse>
    @POST("api/auth/refresh")
    fun refreshToken(): Call<Unit>  // 응답 바디가 없고 헤더만 확인하면 되므로 Unit
}

data class TokenResponse(
    val accessToken: String
)