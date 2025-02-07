//package com.example.modapjt.data.repository
//
//import android.util.Log
//import com.example.modapjt.data.api.RetrofitInstance
//import com.example.modapjt.data.dto.response.toDomain
//import com.example.modapjt.domain.model.Card
//import com.example.modapjt.domain.model.CardDetail
//
//class CardRepository {
//    private val api = RetrofitInstance.api
//
//    /**
//     * 카드 목록 조회
//     * 특정 보드의 모든 카드를 가져옵니다.
//     */
//    suspend fun getCardList(
//        userId: String,
//        boardId: String
//    ): Result<List<Card>> {
//        return try {
//            val response = api.getCardList(userId, boardId)
//
//            // 응답 로그 기록
//            Log.d("CardRepository", "API Response: ${response.body()}")
//
//            if (response.isSuccessful) {
//                val cardResponse = response.body()
//                if (cardResponse != null) {
//                    val cards = cardResponse.toDomain() // CardResponse → List<Card> 변환
//                    Log.d("CardRepository", "Converted Cards: $cards")
//                    Result.success(cards)
//                } else {
//                    Result.failure(Exception("Empty response"))
//                }
//            } else {
//                Result.failure(Exception("Error: ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            Log.e("CardRepository", "Error fetching card list", e)
//            Result.failure(e)
//        }
//    }
//
//    /**
//     * 카드 상세 정보 조회
//     * 특정 카드의 상세 정보를 가져옵니다.
//     */
//    suspend fun getCardDetail(
//        userId: String,
//        cardId: String
//    ): Result<CardDetail> {
//        return try {
//            val response = api.getCardDetail(cardId, userId)
//
//            // 응답 로그 기록
//            Log.d("CardRepository", "API Response: ${response.body()}")
//
//            if (response.isSuccessful) {
//                val cardDetail = response.body()
//                if (cardDetail != null) {
//                    val detail = cardDetail.toDomain() // CardDetailDTO → CardDetail 변환
//                    Log.d("CardRepository", "Converted CardDetail: $detail")
//                    Result.success(detail)
//                } else {
//                    Result.failure(Exception("Empty response"))
//                }
//            } else {
//                Result.failure(Exception("Error: ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            Log.e("CardRepository", "Error fetching card detail", e)
//            Result.failure(e)
//        }
//    }
//}
////package com.example.modapjt.data.repository
////
////import android.util.Log
////import com.example.modapjt.data.api.RetrofitInstance
////import com.example.modapjt.data.dto.response.toDomain
////import com.example.modapjt.domain.model.Card
////
////class CardRepository {
////    private val api = RetrofitInstance.api
////
////    suspend fun getCardList(
////        userId: String,
////        boardId: String
////    ): Result<List<Card>> {
////        return try {
////            val response = api.getCardList(userId, boardId)
////
////            Log.d("CardRepository", "API Response: ${response.body()}") // 서버 응답 확인 로그
////
////            if (response.isSuccessful) {
////                val cardResponse = response.body()
////                if (cardResponse != null) {
////                    val cards = cardResponse.toDomain() // CardResponse → List<Card> 변환
////                    Log.d("CardRepository", "Converted Cards: $cards") // 변환된 데이터 확인
////                    Result.success(cards)
////                } else {
////                    Result.failure(Exception("Empty response"))
////                }
////            } else {
////                Result.failure(Exception("Error: ${response.message()}"))
////            }
////        } catch (e: Exception) {
////            Log.e("CardRepository", "Error fetching card list", e)
////            Result.failure(e)
////        }
////    }
////}
