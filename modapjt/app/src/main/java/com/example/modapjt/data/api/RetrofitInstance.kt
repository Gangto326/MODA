package com.example.modapjt.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.example.com/" // API 서버 주소 입력

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환기
            .build()
            .create(ApiService::class.java)
    }
}

// addConverterFactory : JSON 데이터를 Kotlin 객체로 변환