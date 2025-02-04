package com.example.modapjt.domain.model

import com.example.modapjt.data.dto.response.BlogPost

data class CardDetail(
    val cardId: String,
    val boardId: String,
    val typeId: Int,
    val type: String,
    val title: String,
    val content: List<BlogPost>,  // String에서 List<ContentSection>으로 변경
    val createdAt: String
)