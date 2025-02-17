package com.example.modapjt.data.dto.response

import com.example.modapjt.domain.model.AllTabData
import com.example.modapjt.domain.model.Card
import com.google.gson.annotations.SerializedName

// ✅ 전체탭 (api/search/main) 응답용 DTO
data class AllTabCardApiResponse(
    val contentResults: Map<String, List<CardDTO>>?, // ✅ IMG, BLOG, NEWS, VIDEO 데이터 포함
    val topScores: List<TopScore>?
)

// ✅ 특정탭 (api/search) 응답용 DTO
data class TabCardApiResponse(
    @SerializedName("content") val content: List<CardDTO>?, // ✅ 특정탭은 `content` 필드 사용
    @SerializedName("sliceInfo") val sliceInfo: SliceInfoDTO?
)

// ✅ 공통 모델
data class TopScore(
    val contentType: String,
    val score: Float?
)

// ✅ 페이징 정보 DTO
data class SliceInfoDTO(
    val currentPage: Int,
    val pageSize: Int,
    val hasNext: Boolean
)

// ✅ 특정탭(CardResponse) → List<Card> 변환 함수
fun TabCardApiResponse.toDomain(): List<Card> {
    return content?.map { it.toDomain() } ?: emptyList()
}

// ✅ 전체탭(AllTabCardApiResponse) → List<Card> 변환 함수
fun AllTabCardApiResponse.toDomain(): List<Card> {
    val images = contentResults?.get("IMG")?.map { it.toDomain() } ?: emptyList()
    val blogs = contentResults?.get("BLOG")?.map { it.toDomain() } ?: emptyList()
    val news = contentResults?.get("NEWS")?.map { it.toDomain() } ?: emptyList()
    val videos = contentResults?.get("VIDEO")?.map { it.toDomain() } ?: emptyList()
    return images + blogs + news + videos
}

// TopScore 변환을 위한 새로운 확장 함수
fun AllTabCardApiResponse.toTopScores(): List<TopScore> {
    return topScores?.map { it.toDomain() } ?: emptyList()
}

fun TopScore.toDomain(): TopScore {
    return TopScore(
        contentType = this.contentType,
        score = this.score
    )
}
