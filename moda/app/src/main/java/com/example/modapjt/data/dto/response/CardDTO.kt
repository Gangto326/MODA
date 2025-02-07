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
    val createdAt: String
)


// 서버 응답에 맞는 새로운 DTO 생성 (content 속성을 포함)
data class CardApiResponse(
    val content: List<CardDTO>, // content 필드를 추가
    val sliceInfo: SliceInfo
)

data class SliceInfo(
    val currentPage: Int,
    val pageSize: Int,
    val hasNext: Boolean
)

// DTO -> 도메인 모델 변환 함수
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

