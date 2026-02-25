package com.example.modapjt.domain.model

// 앱 내부에서 사용하는 데이터 모델
data class Card(
    val cardId: String,
    val categoryId: Int,
    val typeId: Int,
    val type: String,
    val title: String,
    val thumbnailContent: String?,
    val thumbnailUrl: String?,
    val keywords: List<String>,
//    val createdAt: String,
    val excludedKeywords: List<String>?,
    val isMine: Boolean,
    val score: Float?,
    val bookMark: Boolean
)