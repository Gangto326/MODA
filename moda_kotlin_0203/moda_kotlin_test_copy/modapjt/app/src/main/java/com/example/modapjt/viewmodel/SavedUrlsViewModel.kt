package com.example.modapjt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.modapjt.ModapApplication
import com.example.modapjt.data.CaptureEntity
import kotlinx.coroutines.flow.Flow

class SavedUrlsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as ModapApplication).repository
    val allCaptures: Flow<List<CaptureEntity>> = repository.allCaptures
}