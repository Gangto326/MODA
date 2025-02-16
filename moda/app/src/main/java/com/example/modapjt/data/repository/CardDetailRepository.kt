package com.example.modapjt.data.repository

import android.util.Log
import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.CardDetail
import java.io.IOException

class CardDetailRepository {
    private val api = RetrofitInstance.cardApi

    // 카드 상세페이지 정보 가져오기 (api/card/{cardId})
    suspend fun getCardDetail(cardId: String): Result<CardDetail> {  // ✅ 반환 타입 변경 (List<Card> → CardDetail)
        return try {
            println("[CardDetailRepository] 카드 상세정보 API 요청: cardId=$cardId")

            val response = api.getCardDetail(cardId)  // ✅ API 호출 시 cardId를 String으로 변경
            println("[CardDetailRepository] 카드 상세정보 응답 코드: ${response.code()}, 메시지: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                println("[CardDetailRepository] 카드 상세정보 응답 데이터: $body")

                if (body != null) {
                    Result.success(body.toDomain())  // ✅ 변환 후 반환 (List<Card> → CardDetail)
                } else {
                    Result.failure(Exception("카드 상세정보를 불러올 수 없습니다."))
                }
            } else {
                Log.d("[CardDetailRepository] 카드 상세정보 응답 실패:", "${response.errorBody()?.string()}")
                Result.failure(Exception("카드 상세정보 불러오기 실패 - ${response.message()}"))
            }
        } catch (e: IOException) {
            println("[CardDetailRepository] 네트워크 오류 발생: ${e.message}")
            Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
        } catch (e: Exception) {
            println("[CardDetailRepository] 카드 상세정보 API 요청 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }
}
