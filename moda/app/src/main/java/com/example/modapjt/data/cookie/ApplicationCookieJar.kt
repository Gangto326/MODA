package com.example.modapjt.data.cookie

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


object ApplicationCookieJar : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()
    private const val TAG = "CookieJar"

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        Log.d(TAG, "Saving cookies for ${url.host}: $cookies")
        cookieStore[url.host] = cookies
        Log.d(TAG, "Current cookie store: $cookieStore") // 저장 후 상태 확인
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = cookieStore[url.host] ?: emptyList()
        Log.d(TAG, "Loading cookies for ${url.host}: $cookies")
        Log.d(TAG, "Current cookie store: $cookieStore") // 로드 시 상태 확인
        return cookies
    }
}