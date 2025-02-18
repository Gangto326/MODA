package com.example.modapjt.data.repository

import com.example.modapjt.data.api.RetrofitInstance
import com.example.modapjt.data.dto.request.BookmarkRequest
import com.example.modapjt.data.dto.request.CardRequest
import com.example.modapjt.data.dto.response.AllTabCardApiResponse
import com.example.modapjt.data.dto.response.toDomain
import com.example.modapjt.domain.model.Card
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

// 서버에서 카드 데이터 가져오는 역할
class CardRepository {
    private val api = RetrofitInstance.cardApi

//    // ✅ 전체탭 카드 리스트 가져오기 (api/search/main)
//    suspend fun getAllTabCards(userId: String, query: String, categoryId: Int): Result<List<Card>> {
//        return try {
//            println("[CardRepository] 전체탭 API 요청: userId=$userId, categoryId=$categoryId, query=$query")
//
//            val response = api.getAllTabCardList(userId, query, categoryId)
//            println("[CardRepository] 전체탭 응답 코드: ${response.code()}, 메시지: ${response.message()}")
//
//            if (response.isSuccessful) {
//                val body = response.body()
//                println("[CardRepository] 전체탭 응답 데이터: $body")
//
//                Result.success(body?.toDomain() ?: emptyList()) // ✅ 전체탭 응답 변환 적용
//            } else {
//                println("[CardRepository] 전체탭 응답 실패: ${response.errorBody()?.string()}")
//                Result.failure(Exception("전체탭 카드 리스트 불러오기 실패 - ${response.message()}"))
//            }
//        } catch (e: IOException) {
//            println("[CardRepository] 네트워크 오류 발생: ${e.message}")
//            Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
//        } catch (e: Exception) {
//            println("[CardRepository] 전체탭 API 요청 예외 발생: ${e.message}")
//            Result.failure(e)
//        }
//    }
    // ✅ 전체탭 카드 리스트 가져오기 - 페이징 없음
    suspend fun getAllTabCards(query: String, categoryId: Int): Result<AllTabCardApiResponse> {
        return try {
            val response = api.getAllTabCardList(query, categoryId)
            println("[CardRepository] 전체탭 응답 코드: ${response.code()}, 메시지: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                println("[CardRepository] 전체탭 응답 데이터: $body")
                Result.success(body ?: AllTabCardApiResponse(null, null))
            } else {
                Result.failure(Exception("전체탭 카드 리스트 불러오기 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


//    // ✅ 특정탭(이미지, 블로그, 뉴스, 영상) 카드 리스트 가져오기 (api/search)
//    suspend fun getTabCards(userId: String, query: String, categoryId: Int, typeId: Int, sortDirection: String): Result<List<Card>> {
//        return try {
//            println("[CardRepository] 특정탭 API 요청: userId=$userId, categoryId=$categoryId, typeId=$typeId, sortDirection=$sortDirection")
//
////            val response = api.getTabCardList(userId, query, categoryId, typeId)
//            val response = api.getTabCardList(userId, query, categoryId, typeId, sortDirection = sortDirection) // sortDirection 추가됨
//
//            println("[CardRepository] 특정탭 응답 코드: ${response.code()}, 메시지: ${response.message()}")
//
//            if (response.isSuccessful) {
//                val body = response.body()
//                println("[CardRepository] 특정탭 응답 데이터: $body")
//
//                Result.success(body?.toDomain() ?: emptyList()) // ✅ 특정탭 응답 변환 적용
//            } else {
//                println("[CardRepository] 특정탭 응답 실패: ${response.errorBody()?.string()}")
//                Result.failure(Exception("특정탭 카드 리스트 불러오기 실패 - ${response.message()}"))
//            }
//        } catch (e: IOException) {
//            println("[CardRepository] 네트워크 오류 발생: ${e.message}")
//            Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
//        } catch (e: Exception) {
//            println("[CardRepository] 특정탭 API 요청 예외 발생: ${e.message}")
//            Result.failure(e)
//        }
//    }
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

//    // 즐겨찾기 전체탭 카드 리스트 가져오기 (api/search/bookmark)
//    suspend fun getAllTabBookMarkCards(userId: String): Result<List<Card>> {
//        return try {
//            println("[CardRepository] 전체탭 API 요청: userId=$userId")
//
//            val response = api.getAllTabBookMarkCardList(userId)
//            println("[CardRepository] 전체탭 응답 코드: ${response.code()}, 메시지: ${response.message()}")
//
//            if (response.isSuccessful) {
//                val body = response.body()
//                println("[CardRepository] 전체탭 응답 데이터: $body")
//
//                Result.success(body?.toDomain() ?: emptyList()) // ✅ 전체탭 응답 변환 적용
//            } else {
//                println("[CardRepository] 전체탭 응답 실패: ${response.errorBody()?.string()}")
//                Result.failure(Exception("전체탭 카드 리스트 불러오기 실패 - ${response.message()}"))
//            }
//        } catch (e: IOException) {
//            println("[CardRepository] 네트워크 오류 발생: ${e.message}")
//            Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
//        } catch (e: Exception) {
//            println("[CardRepository] 전체탭 API 요청 예외 발생: ${e.message}")
//            Result.failure(e)
//        }
//    }
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



//    // 즐겨찾기 특정탭(이미지, 블로그, 뉴스, 영상) 카드 리스트 가져오기 (api/search)
//    suspend fun getTabBookMarkCards(userId: String, typeId: Int, page: Int, size: Int, sortDirection: String): Result<List<Card>> {
//        return try {
//            println("[CardRepository] 특정탭 API 요청: userId=$userId, typeId=$typeId, page=$page, size=$size, sortDirection=$sortDirection")
//
//            val response = api.getTabBookMarkCardList(userId, typeId, page, size, sortDirection = sortDirection) // sortDirection 추가됨
//
//            println("[CardRepository] 특정탭 응답 코드: ${response.code()}, 메시지: ${response.message()}")
//
//            if (response.isSuccessful) {
//                val body = response.body()
//                println("[CardRepository] 특정탭 응답 데이터: $body")
//
//                Result.success(body?.toDomain() ?: emptyList()) // ✅ 특정탭 응답 변환 적용
//            } else {
//                println("[CardRepository] 특정탭 응답 실패: ${response.errorBody()?.string()}")
//                Result.failure(Exception("특정탭 카드 리스트 불러오기 실패 - ${response.message()}"))
//            }
//        } catch (e: IOException) {
//            println("[CardRepository] 네트워크 오류 발생: ${e.message}")
//            Result.failure(Exception("네트워크 오류 발생: ${e.message}"))
//        } catch (e: Exception) {
//            println("[CardRepository] 특정탭 API 요청 예외 발생: ${e.message}")
//            Result.failure(e)
//        }
//    }
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

    // 이미지 카드 저장
    suspend fun createCardWithImage(imageFile: File): Result<Boolean> {
        return try {
            // 이미지 파일을 MultipartBody.Part로 변환
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("files", imageFile.name, requestFile)

            val response = api.createCardWithImage(listOf(imagePart))

            if (response.isSuccessful) {
                Result.success(response.body() ?: false)
            } else {
                Result.failure(Exception("이미지 저장 실패: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
