package com.example.modapjt.notification

object FCMTokenManager {
    private var cachedToken: String? = null

    fun setToken(token: String) {
        cachedToken = token
    }

    fun getToken(): String? = cachedToken
}