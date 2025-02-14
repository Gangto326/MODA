package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.Card

data class CardDTO(
    val cardId: String,
    val categoryId: Int,
    val typeId: Int, // 1: 이미지, 2: 동영상, 3: 블로그, 4: 뉴스
    val type: String,
    val title: String,
    val thumbnailContent: String?,
    val thumbnailUrl: String?,
    val keywords: List<String>?,
    val excludedKeywords: List<String>?,
    val isMine: Boolean,
    val score: Float?,
    val bookmark: Boolean
)

// DTO → 도메인 모델 변환 함수
fun CardDTO.toDomain(): Card {
    return Card(
        cardId = this.cardId,
        categoryId = this.categoryId,
        typeId = this.typeId,
        type = this.type,
        title = this.title,
        thumbnailContent = this.thumbnailContent,
        thumbnailUrl = this.thumbnailUrl,
        keywords = this.keywords ?: emptyList(),
        excludedKeywords = this.excludedKeywords ?: emptyList(),
        isMine = this.isMine,
        score = this.score,
        bookMark = this.bookmark
    )
}
