package com.example.modapjt.notification

import android.util.Log
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.request.FCMTokenRequest
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val api = RetrofitInstance.fcmTokenApi
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override fun onCreate() {
        super.onCreate()
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token") // 로그 추가
        sendRegistrationTokenToServer(token)
    }

    private fun sendRegistrationTokenToServer(token: String) {
        serviceScope.launch {
            try {
                Log.d("FCM", "Sending token to server: $token") // 요청 시작 로그
                api.postToken(
                    request = FCMTokenRequest(token)
                )
                Log.d("FCM", "Token sent successfully") // 성공 로그
            } catch (e: Exception) {
                Log.e("FCM", "토큰 전송 실패: ${e.message}", e) // 더 자세한 에러 로그
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}