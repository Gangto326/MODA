package com.example.modapjt.domain.model

data class Card(
    val cardId: String,
    val boardId: String,
    val typeId: Int,
    val type: String,
    val thumbnailContent: String,
    val thumbnailUrl: String,
    val createdAt: String
)
