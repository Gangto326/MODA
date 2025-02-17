package com.example.modapjt

import android.app.Application
import com.example.modapjt.data.AppDatabase
import com.example.modapjt.data.CaptureRepository
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.storage.TokenManager

//class ModapApplication : Application() {
//    val database by lazy { AppDatabase.getDatabase(this) }
//    val repository by lazy { CaptureRepository(database.captureDao()) }
//}
// -> 수정
class ModapApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // TokenManager 초기화
        val tokenManager = TokenManager(applicationContext)

        // RetrofitInstance 초기화
        RetrofitInstance.initialize(tokenManager)
    }
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CaptureRepository(database.captureDao()) }
}