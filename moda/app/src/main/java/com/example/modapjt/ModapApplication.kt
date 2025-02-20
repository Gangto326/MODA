package com.example.modapjt

import android.app.Application
import com.example.modapjt.data.AppDatabase
import com.example.modapjt.data.CaptureRepository
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.storage.TokenManager
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

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

        CookieManager(PersistentCookieStore(applicationContext), CookiePolicy.ACCEPT_ALL).also {
            CookieHandler.setDefault(it)
        }

        // RetrofitInstance 초기화
        RetrofitInstance.initialize(tokenManager)
    }
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CaptureRepository(database.captureDao()) }
}