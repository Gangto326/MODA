package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.Card
import com.google.gson.annotations.SerializedName

// 개별 카드 정보를 담는 DTO
data class CardDTO(
    @SerializedName("cardId") val cardId: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("typeId") val typeId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("thumbnailContent") val thumbnailContent: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("createdAt") val createdAt: String
)

// DTO → Domain 변환 함수
fun CardDTO.toDomain(): Card {
    return Card(
        cardId = this.cardId,
        boardId = this.boardId,
        typeId = this.typeId,
        type = this.type,
        thumbnailContent = this.thumbnailContent ?: "", // 기본값 제공
        thumbnailUrl = this.thumbnailUrl ?: "", // 기본값 제공
        createdAt = this.createdAt
    )
}

// 서버 응답에서 thumbnailContent나 thumbnailUrl이 항상 존재하는 값이라면?
// 방법 1) nullable 타입 유지 → Card.kt에서 그대로 String? 사용
// null일 경우 기본적으로 빈 문자열("")을 사용해야 한다면?
// 방법 2) 기본값 제공 → toDomain()에서 ?: "" 추가
