package com.example.modapjt.domain.model

data class Card(
    val cardId: String,
    val categoryId: Int,
    val typeId: Int,
    val type: String,
    val title: String,
    val thumbnailContent: String?,
    val thumbnailUrl: String?,
    val keywords: List<String>,
    val createdAt: String
)