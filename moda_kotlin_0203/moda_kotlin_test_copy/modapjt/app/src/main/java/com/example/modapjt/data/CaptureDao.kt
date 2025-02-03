package com.example.modapjt.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CaptureDao {
    @Insert
    suspend fun insert(capture: CaptureEntity)

    @Query("SELECT * FROM captures ORDER BY timestamp DESC")
    fun getAllCaptures(): Flow<List<CaptureEntity>>

    @Delete
    suspend fun delete(capture: CaptureEntity)

    @Query("DELETE FROM captures")
    suspend fun deleteAll()
}