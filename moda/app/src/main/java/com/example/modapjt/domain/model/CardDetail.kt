package com.example.modapjt.domain.model

// 카드 상세 정보를 나타내는 도메인 모델
data class CardDetail(
    val cardId: String,
    val categoryId: Int,
    val typeId: Int,
    val type: String,
    val title: String,
    val content: String,
    val thumbnailUrl: String?,
    val keywords: List<String>,
    val originalUrl: String,
    val subContents: List<String>,
    val isMine: Boolean,
    val bookmark: Boolean,
    val createdAt: String
)
