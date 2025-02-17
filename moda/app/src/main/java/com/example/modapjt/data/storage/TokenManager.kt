package com.example.modapjt.data.storage

import android.content.Context
import android.content.SharedPreferences

/**
 * TokenManager는 앱 내에서 액세스 토큰과 리프레시 토큰을 저장, 조회, 삭제하는 역할을 담당하는 클래스입니다.
 * SharedPreferences를 활용하여 토큰을 안전하게 저장하며, 필요할 때 불러오거나 삭제할 수 있습니다.
 */
class TokenManager(context: Context) {
    // SharedPreferences를 이용하여 토큰을 저장할 공간 생성
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    /**
     * 액세스 토큰을 SharedPreferences에 저장하는 함수
     * @param token 저장할 액세스 토큰 값
     */
    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }

    /**
     * 저장된 액세스 토큰을 불러오는 함수
     * @return 액세스 토큰 문자열 (저장된 값이 없을 경우 null 반환)
     */
    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    /**
     * 리프레시 토큰을 SharedPreferences에 저장하는 함수
     * @param token 저장할 리프레시 토큰 값
     */
    fun saveRefreshToken(token: String) {
        prefs.edit().putString("refresh_token", token).apply()
    }

    /**
     * 저장된 리프레시 토큰을 불러오는 함수
     * @return 리프레시 토큰 문자열 (저장된 값이 없을 경우 null 반환)
     */
    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    /**
     * 저장된 액세스 토큰과 리프레시 토큰을 모두 삭제하는 함수 (로그아웃 시 사용)
     */
    fun clearTokens() {
        prefs.edit().remove("access_token").remove("refresh_token").apply()
    }
}
