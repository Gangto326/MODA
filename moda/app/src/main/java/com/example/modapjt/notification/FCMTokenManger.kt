package com.example.modapjt.notification

object FCMTokenManager {
    private var cachedToken: String? = null

    fun setToken(token: String) {
        println("fcm " + token )
        cachedToken = token
    }

    fun getToken(): String? = cachedToken
}
