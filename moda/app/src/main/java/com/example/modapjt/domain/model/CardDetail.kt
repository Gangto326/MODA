package com.example.modapjt.domain.model

data class CardDetail(
    val cardId: String,
    val categoryId: Int,
    val typeId: Int,
    val type: String,
    val title: String,
    val content: String,
    val thumbnailUrl: String?,
    val keywords: List<String>,
    val createdAt: String
)