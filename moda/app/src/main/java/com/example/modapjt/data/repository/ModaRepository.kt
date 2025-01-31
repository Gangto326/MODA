//// ModaRepository.kt
//package com.example.modapjt.data.repository
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import com.example.modapjt.data.api.RetrofitInstance
//import com.example.modapjt.domain.model.Board
//import com.example.modapjt.domain.model.Card
//import java.time.LocalDateTime
//
//class ModaRepository {
//    private val api = RetrofitInstance.api
//
//    // 보드 리스트 조회
////    suspend fun getBoardList(token: String, nickName: String): Result<List<Board>> {
////        return try {
////            val response = api.getBoardList(nickName, token)
////            if (response.isSuccessful) {
////                val boards = response.body()?.data?.map { it.toDomain() } ?: emptyList()
////                Result.success(boards)
////            } else {
////                Result.failure(Exception(response.message()))
////            }
////        } catch (e: Exception) {
////            Result.failure(e)
////        }
////    }
//
//    // 테스트용 더미 데이터
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getDummyBoardList(): List<Board> {
//        return listOf(
//            Board(
//                boardId = "1",
//                userId = "user1",
//                title = "침착맨 보드",
//                isPublic = true,
//                position = 1,
//                createdAt = LocalDateTime.now(),
//                cards = getDummyCards(),
//                isView = true
//            ),
//            Board(
//                boardId = "2",
//                userId = "user1",
//                title = "침착 보드",
//                isPublic = true,
//                position = 1,
//                createdAt = LocalDateTime.now(),
//                cards = getDummyCards(),
//                isView = true
//            )
//            // ... 더 많은 더미 데이터
//        )
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getDummyCards(): List<Card> {
//        return listOf(
//            Card(
//                cardId = "1",
//                boardId = "1",
//                typeId = 1,
//                urlHash = null,
//                title = "흑백만화만 본 사람이 맞혀보겠습니다",
//                content = "영상 내용 요약...",
//                embedding = null,
//                createdAt = LocalDateTime.now(),
//                updatedAt = null,
//                deletedAt = null,
//                isView = true
//            )
//        )
//    }
//}

package com.example.modapjt.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.modapjt.domain.model.Board
import com.example.modapjt.domain.model.Card
import java.time.LocalDateTime

class ModaRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDummyBoardList(): List<Board> {
        return listOf(
            Board(
                boardId = "1",
                userId = "user1",
                title = "게임 보드",
                isPublic = true,
                position = 1,
                createdAt = LocalDateTime.now(),
                cards = getDummyCards("1"),
                isView = true
            ),
            Board(
                boardId = "3",
                userId = "user1",
                title = "침착맨 보드",
                isPublic = true,
                position = 1,
                createdAt = LocalDateTime.now(),
                cards = getDummyCards("3"),
                isView = true
            ),
            Board(
                boardId = "2",
                userId = "user2",
                title = "요리 보드",
                isPublic = false,
                position = 2,
                createdAt = LocalDateTime.now(),
                cards = getDummyCards("2"),
                isView = false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDummyCards(boardId: String): List<Card> {
        val allCards = listOf(
            Card(
                cardId = "1",
                boardId = "1",
                typeId = 1,
                urlHash = "https://www.youtube.com/watch?v=X3RNpmurMVc",
                title = "T1 : 프로의 세계는 냉정합니다",
                content = "티원 경기를 분석해보았습니다",
                embedding = null,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null,
                isView = true
            ),
            Card(
                cardId = "2",
                boardId = "1",
                typeId = 1,
                urlHash = "https://www.youtube.com/watch?v=2r0HTRMXgJ8",
                title = "T1 vs KT | 매치 16 하이라이트",
                content = "LIVE & LIVE VOD",
                embedding = null,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null,
                isView = false
            ),
            Card(
                cardId = "3",
                boardId = "2",
                typeId = 2,
                urlHash = null,
                title = "오늘의 요리 레시피",
                content = "맛있는 요리 만드는 법",
                embedding = null,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null,
                isView = true,
            )
        )
        return allCards.filter { it.boardId == boardId }
    }

    //추가
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCardById(cardId: String): Card? {
        val allCards = listOf(
            Card(
                cardId = "1",
                boardId = "1",
                typeId = 1,
                urlHash = "https://www.youtube.com/watch?v=X3RNpmurMVc",
                title = "T1 : 프로의 세계는 냉정합니다",
                content = "티원 경기를 분석해보았습니다",
                embedding = null,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null,
                isView = true
            ),
            Card(
                cardId = "2",
                boardId = "1",
                typeId = 1,
                urlHash = "https://www.youtube.com/watch?v=2r0HTRMXgJ8",
                title = "T1 vs KT | 매치 16 하이라이트",
                content = "LIVE & LIVE VOD",
                embedding = null,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null,
                isView = false
            ),
            Card(
                cardId = "3",
                boardId = "2",
                typeId = 2,
                urlHash = null,
                title = "오늘의 요리 레시피",
                content = "맛있는 요리 만드는 법",
                embedding = null,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                deletedAt = null,
                isView = true
            )
        )
        return allCards.find { it.cardId == cardId }
    }


}
