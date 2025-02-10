package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.Card
import com.google.gson.annotations.SerializedName

// 서버 응답 구조 반영
data class CardResponse(
    @SerializedName("content") val cards: List<CardDTO>, // `content` 필드에 카드 리스트 포함
    @SerializedName("sliceInfo") val sliceInfo: SliceInfoDTO // 페이징 정보 (옵션)
)

// 페이징 정보 DTO (필요한 경우)
data class SliceInfoDTO(
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("hasNext") val hasNext: Boolean
)

// CardResponse → List<Card> 변환 함수
fun CardResponse.toDomain(): List<Card> {
    return cards.map { it.toDomain() }
}
