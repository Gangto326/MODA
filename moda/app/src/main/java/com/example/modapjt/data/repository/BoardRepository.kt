package com.example.modapjt.data.repository

import android.util.Log
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.Board

class BoardRepository {
    private val api = RetrofitInstance.api

    suspend fun getBoardList(userId: String): Result<List<Board>> {
        return try {
            val response = api.getBoardList(userId)

            // 서버 응답 확인용 로그 추가
            Log.d("BoardRepository", "API Response: ${response.body()}")

            if (response.isSuccessful) {
                val boards = response.body()?.map { it.toDomain() } ?: emptyList()

                Log.d("BoardRepository", "Converted Boards: $boards") // 변환된 데이터 확인
                Result.success(boards)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("BoardRepository", "Error fetching board list", e)
            Result.failure(e)
        }
    }
}
