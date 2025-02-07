package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.Card

data class CardDTO(
    val cardId: String,
    val categoryId: Int,
    val typeId: Int,
    val type: String,
    val title: String,
    val thumbnailContent: String?,
    val thumbnailUrl: String?,
    val keywords: List<String>?,
    val createdAt: String
)

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
        createdAt = this.createdAt
    )
}

