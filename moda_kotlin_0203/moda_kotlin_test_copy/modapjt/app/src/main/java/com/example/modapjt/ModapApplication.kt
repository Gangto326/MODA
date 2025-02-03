//package com.example.modapjt
//
//import android.app.Application
//import com.example.modapjt.data.AppDatabase
//import com.example.modapjt.data.CaptureRepository
//
//class ModapApplication : Application() {
//    // 데이터베이스와 리포지토리를 지연 초기화로 생성
//    val database by lazy { AppDatabase.getDatabase(this) }
//    val repository by lazy { CaptureRepository(database.captureDao()) }
//}




//package com.example.modapjt
//
//import android.app.Application
//import androidx.lifecycle.ViewModelStore
//import androidx.lifecycle.ViewModelStoreOwner
//
//class ModapApplication : Application(), ViewModelStoreOwner {
//    private val appViewModelStore = ViewModelStore()
//
//    override val viewModelStore: ViewModelStore
//        get() = appViewModelStore
//}





package com.example.modapjt

import android.app.Application
import com.example.modapjt.data.AppDatabase
import com.example.modapjt.data.CaptureRepository

class ModapApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CaptureRepository(database.captureDao()) }
}
