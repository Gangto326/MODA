// NetworkModule.kt or wherever you configure OkHttp
import com.example.modapjt.data.api.TokenInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class NetworkModule {
    fun provideOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(AndroidCookieJar())  // 쿠키 자동 관리 설정
            .addInterceptor(tokenInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}