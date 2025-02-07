package com.example.modapjt.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://localhost:8080/" // API 서버 주소 입력
    private const val BASE_URL = "http://10.0.2.2:8080" // localhost → 10.0.2.2 변경 (에뮬레이터 전용)
//    private const val BASE_URL = "http://192.168.0.38:8080"  // 내 PC의 IP 주소로 변경

    val api: CategoryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryApiService::class.java)
    }
}


// addConverterFactory : JSON 데이터를 Kotlin 객체로 변환