package com.example.modapjt.domain.model

import com.example.modapjt.data.dto.response.ThumbnailSection

data class Card(
    val cardId: String,
    val boardId: String,
    val typeId: Int,
    val type: String,
    val title: String, // 추가
    val thumbnailContent: List<ThumbnailSection>,  // String에서 List로 변경
    val thumbnailUrl: String,
    val createdAt: String
)