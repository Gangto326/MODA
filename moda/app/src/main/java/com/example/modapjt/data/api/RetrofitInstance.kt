package com.example.modapjt.data.api

import com.example.modapjt.data.cookie.ApplicationCookieJar
import com.example.modapjt.data.storage.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://localhost:8080/" // API 서버 주소 입력

//    private const val BASE_URL = "http://10.0.2.2:8080" // localhost → 10.0.2.2 변경 (에뮬레이터 전용)
    private const val BASE_URL = "https://i12a805.p.ssafy.io" // 우리 서버!
//    private const val BASE_URL = "http://0.0.0.0:8080"  // 내 PC의 IP 주소로 변경
    private lateinit var tokenManager: TokenManager

    // TokenManager 초기화 함수
    fun initialize(tokenManager: TokenManager) {
        this.tokenManager = tokenManager
    }

    // 토큰을 포함한 HTTP 클라이언트 생성을 위한 함수
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)  // 쿠키 처리를 위해 필요
            .followSslRedirects(true)  // HTTPS 리다이렉트 허용
            .cookieJar(ApplicationCookieJar)  // 쿠키 자동 관리 추가
            .addInterceptor(TokenInterceptor(tokenManager))
            .build()
    }
    // Retrofit 인스턴스 생성을 위한 함수
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API 서비스들 - lazy 프로퍼티로 정의
    val authApi: AuthApiService by lazy {
        createRetrofit().create(AuthApiService::class.java)
    }

    val userApi: UserApiService by lazy {
        createRetrofit().create(UserApiService::class.java)
    }

    val categoryApi: CategoryApiService by lazy {
        createRetrofit().create(CategoryApiService::class.java)
    }

    val cardApi: CardApiService by lazy {
        createRetrofit().create(CardApiService::class.java)
    }

    val searchApi: SearchApiService by lazy {
        createRetrofit().create(SearchApiService::class.java)
    }

    val fcmTokenApi: FcmApiService by lazy {
        createRetrofit().create(FcmApiService::class.java)
    }

    //회원가입 API 추가
    val signupApi: SignUpApiService by lazy{
        createRetrofit().create(SignUpApiService::class.java)
    }

    // 아이디 찾기 API
    val findIdApi: FindIdApiService by lazy {
        createRetrofit().create(FindIdApiService::class.java)
    }

    // 비밀번호 찾기 API
    val findPasswordApi: FindPasswordApiService by lazy {
        createRetrofit().create(FindPasswordApiService::class.java)
    }

//    val api: CategoryApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(CategoryApiService::class.java)
//    }
//
//    val cardApi: CardApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(CardApiService::class.java)
//    }
//
//    val apiService: SearchApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(SearchApiService::class.java)
//    }
//
//    private val retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val userApi: UserApiService by lazy {
//        retrofit.create(UserApiService::class.java)
//
//
//    }
//
//    // ✅ Search API 추가
//    val searchApi: SearchApiService by lazy {
//        retrofit.create(SearchApiService::class.java)
//    }
//
//    val fcmTokenApi: FcmApiService by lazy{
//        retrofit.create(FcmApiService::class.java)
//    }
}


// addConverterFactory : JSON 데이터를 Kotlin 객체로 변환