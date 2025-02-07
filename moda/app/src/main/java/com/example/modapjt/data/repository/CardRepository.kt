package com.example.modapjt.data.repository

import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.Card
import com.example.modapjt.domain.model.CardDetail

class CardRepository {
    private val api = RetrofitInstance.cardApi

    suspend fun getCards(userId: String, categoryId: Int, page: Int, size: Int): Result<List<Card>> {
        return try {
            val response = api.getCardList(userId, categoryId, page, size)
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("카드 리스트 불러오기 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCardDetail(cardId: String, userId: String): Result<CardDetail> {
        return try {
            val response = api.getCardDetail(cardId, userId)
            if (response.isSuccessful) {
                Result.success(response.body()?.toDomain() ?: throw Exception("카드 데이터 없음"))
            } else {
                Result.failure(Exception("카드 상세 불러오기 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
