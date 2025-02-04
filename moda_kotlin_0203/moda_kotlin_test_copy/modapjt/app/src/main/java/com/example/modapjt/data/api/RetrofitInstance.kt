package com.example.modapjt.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://localhost:8080/" // API 서버 주소 입력
    private const val BASE_URL = "http://10.0.2.2:8080" // localhost → 10.0.2.2 변경 (에뮬레이터 전용)
//    private const val BASE_URL = "http://0.0.0.0:8080"  // 내 PC의 IP 주소로 변경



    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환기
            .build()
            .create(ApiService::class.java) // 실제 API 요청을 보낼 수 있는 객체 생성
    }
}

// addConverterFactory : JSON 데이터를 Kotlin 객체로 변환