package com.example.modapjt.data.storage

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        println("TokenManager - 토큰 저장: $token")
        prefs.edit().putString("auth_token", token).apply()
    }

//    fun getTokenSync(): String? {
//        return prefs.getString("auth_token", null)
//    }
    fun getTokenSync(): String? {
        val token = prefs.getString("auth_token", null)
        println("TokenManager - 토큰 조회: $token")
        return token
    }

    fun clearToken() {
        println("TokenManager - 토큰 삭제")
        prefs.edit().remove("auth_token").apply()
    }
}
