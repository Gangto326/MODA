package com.example.modapjt.data.repository

import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.Card
import java.io.IOException

// 서버에서 카드 데이터 가져오는 역할
class CardRepository {
    private val api = RetrofitInstance.cardApi

    // getCards() : categoryId를 받아서 해당 카테고리에 맞는 카드 리스트를 가져옴
    suspend fun getCards(userId: String, categoryId: Int, page: Int, size: Int): Result<List<Card>> {
        return try {
            println("[CardRepository] API 요청: userId=$userId, categoryId=$categoryId, page=$page, size=$size")

            val response = api.getCardList(userId, categoryId, page, size)
            println("[CardRepository] API 응답 코드: ${response.code()}, 메시지: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                println("[CardRepository] API 응답 성공, 카드 개수: ${body?.content?.size ?: 0}")
                Result.success(body?.content?.map { it.toDomain() } ?: emptyList()) // content에서 리스트 추출
            } else {
                println("[CardRepository] API 응답 실패: ${response.errorBody()?.string()}")
                Result.failure(Exception("카드 리스트 불러오기 실패 - ${response.message()}"))
            }
        } catch (e: IOException) {
            println("[CardRepository] 네트워크 오류 발생: ${e.message}")
            Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
        } catch (e: Exception) {
            println("[CardRepository] API 요청 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }
}
