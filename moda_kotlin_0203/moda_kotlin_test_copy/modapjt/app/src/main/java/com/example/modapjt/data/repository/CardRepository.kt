package com.example.modapjt.data.repository

import android.util.Log
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.Card

class CardRepository {
    private val api = RetrofitInstance.api

    suspend fun getCardList(
        userId: String,
        boardId: String
    ): Result<List<Card>> {
        return try {
            val response = api.getCardList(userId, boardId)

            Log.d("CardRepository", "API Response: ${response.body()}") // 서버 응답 확인 로그

            if (response.isSuccessful) {
                val cardResponse = response.body()
                if (cardResponse != null) {
                    val cards = cardResponse.toDomain() // CardResponse → List<Card> 변환
                    Log.d("CardRepository", "Converted Cards: $cards") // 변환된 데이터 확인
                    Result.success(cards)
                } else {
                    Result.failure(Exception("Empty response"))
                }
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("CardRepository", "Error fetching card list", e)
            Result.failure(e)
        }
    }
}
