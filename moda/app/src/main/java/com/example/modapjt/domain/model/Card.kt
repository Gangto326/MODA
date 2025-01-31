package com.example.modapjt.domain.model

import java.time.LocalDateTime

data class Card(
    val cardId: String,
    val boardId: String,
    val typeId: Int,
    val urlHash: String?,
    val title: String,
    val content: String,
    val embedding: List<Float>?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
    val isView: Boolean, // 카드리스트에서 상세요약 보여질 카드

    // 비즈니스 로직
//    fun isDeleted(): Boolean {
//    return deletedAt != null
//}

//fun getPreviewContent(): String {
//    return content.take(100) + if (content.length > 100) "..." else ""
//}

//fun getDuration(): String {
//    return when {
//        updatedAt != null -> "수정됨: ${updatedAt.format()}"
//        else -> "작성됨: ${createdAt.format()}"
//    }
//}
)

//enum class CardType {
//    TEXT, IMAGE, URL, VIDEO
//}