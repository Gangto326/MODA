package com.example.modapjt

import android.app.Application
import android.util.Log
import com.example.modapjt.data.AppDatabase
import com.example.modapjt.data.CaptureRepository
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.storage.TokenManager
import com.example.modapjt.notification.FCMTokenManager
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

//class ModapApplication : Application() {
//    val database by lazy { AppDatabase.getDatabase(this) }
//    val repository by lazy { CaptureRepository(database.captureDao()) }
//}
// -> 수정
class ModapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // TokenManager 초기화
        val tokenManager = TokenManager(applicationContext)

        // RetrofitInstance 초기화
        RetrofitInstance.initialize(tokenManager)

        initializeFCMToken()
    }
    private fun initializeFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    FCMTokenManager.setToken(token)  // FCMTokenManager에 토큰 저장
                    Log.d("FCM", "Token initialized: $token")
                } else {
                    Log.e("FCM", "Failed to initialize token: ${task.exception}")
                }
            }
    }
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CaptureRepository(database.captureDao()) }
}