package com.example.modapjt.data.dto.response

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.modapjt.domain.model.Board
import java.sql.Timestamp
import java.time.LocalDateTime

data class BoardDTO(
    val board_id: String,               // UUID 값
    val user_id: String,                // 보드 소유자 ID
    val title: String,                  // 보드 제목
    val is_public: Boolean,             // 공개 여부
    val position: Int,                  // 보드 순서
    val created_at: Timestamp,          // 생성 시간
    val cards: List<CardDTO>?
)
//{
//    // DTO를 Domain 모델로 변환
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun toDomain() = Board(
//        boardId = board_id,
//        userId = user_id,
//        title = title,
//        isPublic = is_public,
//        position = position,
//        createdAt = LocalDateTime.parse(created_at.toString()),
//        cards = cards?.map { it.toDomain() } ?: emptyList(),
//        isView = true
//    )
//}