// CookieManager.kt
import android.webkit.CookieManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class AndroidCookieJar : CookieJar {
    private val cookieManager = CookieManager.getInstance()

    init {
        cookieManager.setAcceptCookie(true)
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookies.forEach { cookie ->
            cookieManager.setCookie(url.toString(), cookie.toString())
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookiesString = cookieManager.getCookie(url.toString())
        return if (cookiesString != null && cookiesString.isNotEmpty()) {
            cookiesString.split(";").mapNotNull { Cookie.parse(url, it.trim()) }
        } else {
            emptyList()
        }
    }
}
