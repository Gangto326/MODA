package com.example.modapjt

import android.content.Context
import java.net.CookieStore
import java.net.HttpCookie
import java.net.URI

class PersistentCookieStore(private val context: Context) : CookieStore {
    private val prefs = context.getSharedPreferences("cookie_store", Context.MODE_PRIVATE)
    private val cookieMap = mutableMapOf<URI, List<HttpCookie>>()

    init {
        // 저장된 쿠키 로드
        loadCookies()
    }

    private fun loadCookies() {
        prefs.all.forEach { (key, value) ->
            try {
                val cookies = value.toString().split(",,").mapNotNull { cookieStr ->
                    HttpCookie.parse(cookieStr).firstOrNull()
                }
                if (cookies.isNotEmpty()) {
                    cookieMap[URI(key)] = cookies
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveCookies() {
        prefs.edit().apply {
            clear()
            cookieMap.forEach { (uri, cookies) ->
                putString(uri.toString(), cookies.joinToString(",,") { it.toString() })
            }
            apply()
        }
    }

    override fun add(uri: URI?, cookie: HttpCookie?) {
        if (uri == null || cookie == null) return

        val cookies = cookieMap[uri]?.toMutableList() ?: mutableListOf()
        cookies.add(cookie)
        cookieMap[uri] = cookies
        saveCookies()
    }

    override fun get(uri: URI?): List<HttpCookie> {
        return if (uri == null) emptyList() else cookieMap[uri] ?: emptyList()
    }

    // 나머지 CookieStore 인터페이스 구현...
    override fun getCookies(): List<HttpCookie> = cookieMap.values.flatten()
    override fun getURIs(): List<URI> = cookieMap.keys.toList()
    override fun remove(uri: URI?, cookie: HttpCookie?): Boolean {
        if (uri == null || cookie == null) return false
        val cookies = cookieMap[uri]?.toMutableList() ?: return false
        val result = cookies.remove(cookie)
        if (result) {
            cookieMap[uri] = cookies
            saveCookies()
        }
        return result
    }
    override fun removeAll(): Boolean {
        cookieMap.clear()
        prefs.edit().clear().apply()
        return true
    }
}