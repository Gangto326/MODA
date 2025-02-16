package com.example.modapjt.data.api

import com.example.modapjt.data.storage.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://localhost:8080/" // API 서버 주소 입력

    private const val BASE_URL = "http://10.0.2.2:8080" // localhost → 10.0.2.2 변경 (에뮬레이터 전용)
//    private const val BASE_URL = "https://i12a805.p.ssafy.io" // 우리 서버!

//    private const val BASE_URL = "http://0.0.0.0:8080"  // 내 PC의 IP 주소로 변경



    // ✅ 토큰을 포함한 HTTP 클라이언트 생성
    fun getRetrofit(tokenManager: TokenManager): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(tokenManager)) // ✅ 토큰 자동 추가
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // ✅ 커스텀 클라이언트 적용
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 모든 API 서비스를 tokenManager를 사용하도록 수정
    fun getUserApi(tokenManager: TokenManager): UserApiService {
        return getRetrofit(tokenManager).create(UserApiService::class.java)
    }

    fun getCategoryApi(tokenManager: TokenManager): CategoryApiService {
        return getRetrofit(tokenManager).create(CategoryApiService::class.java)
    }

    fun getCardApi(tokenManager: TokenManager): CardApiService {
        return getRetrofit(tokenManager).create(CardApiService::class.java)
    }

    fun getSearchApi(tokenManager: TokenManager): SearchApiService {
        return getRetrofit(tokenManager).create(SearchApiService::class.java)
    }

    fun getFcmTokenApi(tokenManager: TokenManager): FcmApiService {
        return getRetrofit(tokenManager).create(FcmApiService::class.java)
    }


    val api: CategoryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryApiService::class.java)
    }

    val cardApi: CardApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CardApiService::class.java)
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)


    }

    // ✅ Search API 추가
    val searchApi: SearchApiService by lazy {
        retrofit.create(SearchApiService::class.java)
    }

    val fcmTokenApi: FcmApiService by lazy{
        retrofit.create(FcmApiService::class.java)
    }
}


// addConverterFactory : JSON 데이터를 Kotlin 객체로 변환




//package com.example.modapjt.data.api
//
//import com.example.modapjt.data.storage.TokenManager
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitInstance {
//    private const val BASE_URL = "https://i12a805.p.ssafy.io" // ✅ 우리 서버 주소
//
//    // ✅ 토큰을 포함한 HTTP 클라이언트 생성
//    fun getRetrofit(tokenManager: TokenManager): Retrofit {
//        val client = OkHttpClient.Builder()
//            .addInterceptor(TokenInterceptor(tokenManager)) // ✅ 토큰 자동 추가
//            .build()
//
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client) // ✅ 커스텀 클라이언트 적용
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    // ✅ API 서비스들 생성
//    fun getUserApi(tokenManager: TokenManager): UserApiService {
//        return getRetrofit(tokenManager).create(UserApiService::class.java)
//    }
//
//    fun getCategoryApi(tokenManager: TokenManager): CategoryApiService {
//        return getRetrofit(tokenManager).create(CategoryApiService::class.java)
//    }
//
//    fun getCardApi(tokenManager: TokenManager): CardApiService {
//        return getRetrofit(tokenManager).create(CardApiService::class.java)
//    }
//
//    fun getSearchApi(tokenManager: TokenManager): SearchApiService {
//        return getRetrofit(tokenManager).create(SearchApiService::class.java)
//    }
//
//    fun getFcmTokenApi(tokenManager: TokenManager): FcmApiService {
//        return getRetrofit(tokenManager).create(FcmApiService::class.java)
//    }
//}
