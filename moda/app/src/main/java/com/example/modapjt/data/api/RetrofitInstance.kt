package com.example.modapjt.data.api
import com.example.modapjt.data.cookie.ApplicationCookieJar
import com.example.modapjt.data.storage.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//package com.example.modapjt.data.api
//
//import TokenInterceptor
//import com.example.modapjt.data.storage.TokenManager
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitInstance {
//    private const val BASE_URL = "http://10.0.2.2:8080" // localhost → 10.0.2.2 변경 (에뮬레이터 전용)
////    private const val BASE_URL = "https://i12a805.p.ssafy.io" // 우리 서버!
//
//    private lateinit var tokenManager: TokenManager
//
//    // TokenManager 초기화 함수
//    fun initialize(tokenManager: TokenManager) {
//        this.tokenManager = tokenManager
//    }
//
//    // 토큰을 포함한 기본 HTTP 클라이언트
//    private val client: OkHttpClient by lazy {
//        OkHttpClient.Builder()
//            .addInterceptor(TokenInterceptor(tokenManager)) // 토큰 인터셉터 추가
//            .build()
//    }
//
//    // 기본 Retrofit 인스턴스
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val authApi: AuthApiService by lazy {
//        retrofit.create(AuthApiService::class.java)
//    }
//
//
//    //
//    // API 서비스들 - 자동으로 토큰이 포함됨
//    val userApi: UserApiService by lazy {
//        retrofit.create(UserApiService::class.java)
//    }
//
//    val categoryApi: CategoryApiService by lazy {
//        retrofit.create(CategoryApiService::class.java)
//    }
//
//    val cardApi: CardApiService by lazy {
//        retrofit.create(CardApiService::class.java)
//    }
//
//    val searchApi: SearchApiService by lazy {
//        retrofit.create(SearchApiService::class.java)
//    }
//
//    val fcmTokenApi: FcmApiService by lazy {
//        retrofit.create(FcmApiService::class.java)
//    }
//
//
////    // 모든 API 서비스를 tokenManager를 사용하도록 수정
////    fun getUserApi(tokenManager: TokenManager): UserApiService {
////        return getRetrofit(tokenManager).create(UserApiService::class.java)
////    }
////
////    fun getCategoryApi(tokenManager: TokenManager): CategoryApiService {
////        return getRetrofit(tokenManager).create(CategoryApiService::class.java)
////    }
////
////    fun getCardApi(tokenManager: TokenManager): CardApiService {
////        return getRetrofit(tokenManager).create(CardApiService::class.java)
////    }
////
////    fun getSearchApi(tokenManager: TokenManager): SearchApiService {
////        return getRetrofit(tokenManager).create(SearchApiService::class.java)
////    }
////
////    fun getFcmTokenApi(tokenManager: TokenManager): FcmApiService {
////        return getRetrofit(tokenManager).create(FcmApiService::class.java)
////    }
////
////
////    val api: CategoryApiService by lazy {
////        Retrofit.Builder()
////            .baseUrl(BASE_URL)
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////            .create(CategoryApiService::class.java)
////    }
////
////    val cardApi: CardApiService by lazy {
////        Retrofit.Builder()
////            .baseUrl(BASE_URL)
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////            .create(CardApiService::class.java)
////    }
////
////    private val retrofit by lazy {
////        Retrofit.Builder()
////            .baseUrl(BASE_URL)
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////    }
////
////    val userApi: UserApiService by lazy {
////        retrofit.create(UserApiService::class.java)
////
////
////    }
////
////    // ✅ Search API 추가
////    val searchApi: SearchApiService by lazy {
////        retrofit.create(SearchApiService::class.java)
////    }
////
////    val fcmTokenApi: FcmApiService by lazy{
////        retrofit.create(FcmApiService::class.java)
////    }
//}
//
//
//// addConverterFactory : JSON 데이터를 Kotlin 객체로 변환
//
//
//
//
////package com.example.modapjt.data.api
////
////import com.example.modapjt.data.storage.TokenManager
////import okhttp3.OkHttpClient
////import retrofit2.Retrofit
////import retrofit2.converter.gson.GsonConverterFactory
////
////object RetrofitInstance {
////    private const val BASE_URL = "https://i12a805.p.ssafy.io" // ✅ 우리 서버 주소
////
////    // ✅ 토큰을 포함한 HTTP 클라이언트 생성
////    fun getRetrofit(tokenManager: TokenManager): Retrofit {
////        val client = OkHttpClient.Builder()
////            .addInterceptor(TokenInterceptor(tokenManager)) // ✅ 토큰 자동 추가
////            .build()
////
////        return Retrofit.Builder()
////            .baseUrl(BASE_URL)
////            .client(client) // ✅ 커스텀 클라이언트 적용
////            .addConverterFactory(GsonConverterFactory.create())
////            .build()
////    }
////
////    // ✅ API 서비스들 생성
////    fun getUserApi(tokenManager: TokenManager): UserApiService {
////        return getRetrofit(tokenManager).create(UserApiService::class.java)
////    }
////
////    fun getCategoryApi(tokenManager: TokenManager): CategoryApiService {
////        return getRetrofit(tokenManager).create(CategoryApiService::class.java)
////    }
////
////    fun getCardApi(tokenManager: TokenManager): CardApiService {
////        return getRetrofit(tokenManager).create(CardApiService::class.java)
////    }
////
////    fun getSearchApi(tokenManager: TokenManager): SearchApiService {
////        return getRetrofit(tokenManager).create(SearchApiService::class.java)
////    }
////
////    fun getFcmTokenApi(tokenManager: TokenManager): FcmApiService {
////        return getRetrofit(tokenManager).create(FcmApiService::class.java)
////    }
////}
object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080"
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
}