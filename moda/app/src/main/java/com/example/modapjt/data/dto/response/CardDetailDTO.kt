package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.CardDetail

data class CardDetailDTO(
    val cardId: String,
    val categoryId: Int,
    val typeId: Int,
    val type: String,
    val originalUrl: String,
    val title: String,
    val content: String?,
    val thumbnailUrl: String?,
    val keywords: List<String>?,
    val subContents: List<String>?,
    val isMine: Boolean,
    val bookmark: Boolean,
    val createdAt: String
)

fun CardDetailDTO.toDomain(): CardDetail {
    return CardDetail(
        cardId = this.cardId,
        categoryId = this.categoryId,
        typeId = this.typeId,
        type = this.type,
        originalUrl = this.originalUrl,
        title = this.title,
        content = this.content ?: "",
        thumbnailUrl = this.thumbnailUrl,
        keywords = this.keywords ?: emptyList(),
        subContents = this.subContents ?: emptyList(),
        isMine = this.isMine,
        bookmark = this.bookmark,
        createdAt = this.createdAt
    )
}
