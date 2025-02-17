package com.example.modapjt.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080/" // localhost → 10.0.2.2 변경 (에뮬레이터 전용)

    // 로깅 인터셉터 추가
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())  // String 응답을 위해 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 기존 API 서비스들은 그대로 유지
    val api: CategoryApiService by lazy {
        retrofit.create(CategoryApiService::class.java)
    }

    val cardApi: CardApiService by lazy {
        retrofit.create(CardApiService::class.java)
    }

    val apiService: SearchApiService by lazy {
        retrofit.create(SearchApiService::class.java)
    }

    val userApi: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    val searchApi: SearchApiService by lazy {
        retrofit.create(SearchApiService::class.java)
    }

    val fcmTokenApi: FcmApiService by lazy {
        retrofit.create(FcmApiService::class.java)
    }

    val signupApi: SignUpApiService by lazy {
        retrofit.create(SignUpApiService::class.java)
    }

    val findIdApi: FindIdApiService by lazy {
        retrofit.create(FindIdApiService::class.java)
    }

    val findPasswordApi: FindPasswordApiService by lazy {
        retrofit.create(FindPasswordApiService::class.java)
    }
}