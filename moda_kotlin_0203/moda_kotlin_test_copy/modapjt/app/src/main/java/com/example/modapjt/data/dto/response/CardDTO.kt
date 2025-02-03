package com.example.modapjt.data.dto.response

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.modapjt.domain.model.Card
import java.sql.Timestamp
import java.time.LocalDateTime

// 서버에서 받아올 데이터 구조 정의
data class CardDTO(
    val card_id: String,                // UUID 값
    val board_id: String,               // 소속 보드 ID
    val type_id: Int,                   // 카드 타입
    val url_hash: String,              // URL 해시값
    val title: String,                  // 카드 제목
    val content: String,                // 카드 내용
    val thumbnail_content: String,
    val thumbnail_url: String,
    val embedding: List<Float>?,        // 벡터 임베딩
    val view_count: Int,
    val created_at: Timestamp,          // 생성 시간
    val updated_at: Timestamp,         // 수정 시간
    val deleted_at: Timestamp,         // 삭제 시간
)
{
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain() = Card(
        cardId = card_id,
        boardId = board_id,
        typeId = type_id,
        urlHash = url_hash,
        title = title,
        content = content,
        embedding = embedding,
        createdAt = LocalDateTime.parse(created_at.toString()),
        updatedAt = updated_at.let { LocalDateTime.parse(it.toString()) },
        deletedAt = deleted_at.let { LocalDateTime.parse(it.toString()) },
        isView = true
    )
}
