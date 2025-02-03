package com.example.modapjt.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.modapjt.domain.model.Board
import com.example.modapjt.domain.model.Card
import java.time.LocalDateTime

class ModaRepository {

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
