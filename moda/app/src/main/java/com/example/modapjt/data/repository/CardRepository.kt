package com.example.modapjt.data.repository

import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.request.BookmarkRequest
import com.example.modapjt.data.dto.request.CardRequest
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.Card
import java.io.IOException

// 서버에서 카드 데이터 가져오는 역할
class CardRepository {
    private val api = RetrofitInstance.cardApi

    // ✅ 전체탭 카드 리스트 가져오기 - 페이징 없음
    suspend fun getAllTabCards(query: String, categoryId: Int): Result<List<Card>> {
        return try {
            val response = api.getAllTabCardList(query, categoryId)
            println("[CardRepository] 전체탭 응답 코드: ${response.code()}, 메시지: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                println("[CardRepository] 전체탭 응답 데이터: $body")
                Result.success(body?.toDomain() ?: emptyList())
            } else {
                Result.failure(Exception("전체탭 카드 리스트 불러오기 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // ✅ 특정탭 카드 리스트 가져오기 - 페이징 포함
    suspend fun getTabCards(
        query: String,
        categoryId: Int,
        typeId: Int,
        page: Int,
        sortDirection: String
    ): Result<Pair<List<Card>, Boolean>> {
        return try {
            println("[CardRepository] 특정탭 API 요청: categoryId=$categoryId, typeId=$typeId, sortDirection=$sortDirection")

            val response = api.getTabCardList(
                query = query,
                categoryId = categoryId,
                typeId = typeId,
                page = page,
                sortDirection = sortDirection
            )

            if (response.isSuccessful) {
                val body = response.body()
                val cards = body?.content?.map { it.toDomain() } ?: emptyList()
                val hasNext = body?.sliceInfo?.hasNext ?: false
                Result.success(Pair(cards, hasNext))
            } else {
                Result.failure(Exception("특정탭 카드 리스트 불러오기 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 카드 추가 기능
    suspend fun createCard(url: String): Result<Boolean> {
        return try {
            println("[CardRepository] 카드 추가 요청: url=$url")

            val response = api.createCard(CardRequest(url))
            println("[CardRepository] 카드 추가 응답 코드: ${response.code()}, 메시지: ${response.message()}")

            if (response.isSuccessful) {
                Result.success(response.body() ?: false)
            } else {
                Result.failure(Exception("카드 추가 실패: ${response.message()}"))
            }
        } catch (e: IOException) {
            println("[CardRepository] 네트워크 오류 발생: ${e.message}")
            Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
        } catch (e: Exception) {
            println("[CardRepository] 카드 추가 요청 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 즐겨찾기 기능
    suspend fun toggleBookmark(cardId: String, isBookmark: Boolean): Result<Boolean> {
        return try {
            val response = api.toggleBookmark(
                BookmarkRequest(
                    cardId = cardId,
                    isBookmark = isBookmark
                )
            )

            if (response.isSuccessful) {
                Result.success(response.body() ?: false)
            } else {
                Result.failure(Exception("북마크 토글 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 카드 삭제 기능 추가
    suspend fun deleteCard(cardIds: List<String>): Result<Boolean> {
        return try {
            val cardIdString = cardIds.joinToString(",") // "id1,id2,id3" 형태로 변환
            println("[CardRepository] 카드 삭제 요청: cardIds=$cardIdString")

            val response = api.deleteCard(cardIdString)
            println("[CardRepository] 카드 삭제 응답 코드: ${response.code()}, 메시지: ${response.message()}")

            if (response.isSuccessful) {
                Result.success(response.body() ?: false)
            } else {
                Result.failure(Exception("카드 삭제 실패: ${response.message()}"))
            }
        } catch (e: IOException) {
            println("[CardRepository] 네트워크 오류 발생: ${e.message}")
            Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
        } catch (e: Exception) {
            println("[CardRepository] 카드 삭제 요청 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // ✅ 즐겨찾기 전체탭 카드 리스트 가져오기 - 페이징 없음
    suspend fun getAllTabBookMarkCards(): Result<List<Card>> {
        return try {
            val response = api.getAllTabBookMarkCardList()

            if (response.isSuccessful) {
                val body = response.body()
                Result.success(body?.toDomain() ?: emptyList())
            } else {
                Result.failure(Exception("즐겨찾기 전체탭 불러오기 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    // ✅ 즐겨찾기 특정탭 카드 리스트 가져오기 - 페이징 포함
    suspend fun getTabBookMarkCards(
        typeId: Int,
        page: Int,
        size: Int = 15,
        sortDirection: String
    ): Result<Pair<List<Card>, Boolean>> {
        return try {
            val response = api.getTabBookMarkCardList(
                typeId = typeId,
                page = page,
                size = size,
                sortDirection = sortDirection
            )

            if (response.isSuccessful) {
                val body = response.body()
                val cards = body?.content?.map { it.toDomain() } ?: emptyList()
                val hasNext = body?.sliceInfo?.hasNext ?: false
                Result.success(Pair(cards, hasNext))
            } else {
                Result.failure(Exception("즐겨찾기 특정탭 불러오기 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
