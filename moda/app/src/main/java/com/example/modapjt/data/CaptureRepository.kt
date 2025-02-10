package com.example.modapjt.data

import kotlinx.coroutines.flow.Flow

class CaptureRepository(private val captureDao: CaptureDao) {
    val allCaptures: Flow<List<CaptureEntity>> = captureDao.getAllCaptures()

    suspend fun insert(url: String) {
        captureDao.insert(CaptureEntity(url = url))
    }

    suspend fun delete(capture: CaptureEntity) {
        captureDao.delete(capture)
    }

    suspend fun deleteAll() {
        captureDao.deleteAll()
    }
}