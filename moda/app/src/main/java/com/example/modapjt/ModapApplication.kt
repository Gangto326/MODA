package com.example.modapjt

import android.app.Application
import com.example.modapjt.data.AppDatabase
import com.example.modapjt.data.CaptureRepository

class ModapApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CaptureRepository(database.captureDao()) }
}
