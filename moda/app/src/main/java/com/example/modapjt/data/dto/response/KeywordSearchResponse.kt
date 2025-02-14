package com.example.modapjt.data.dto.response

import com.google.gson.annotations.SerializedName

// ✅ 키워드 검색 API 응답 데이터
data class KeywordSearchResponse(
    @SerializedName("cardId") val cardId: String,
    @SerializedName("categoryId") val categoryId: Int?,
    @SerializedName("typeId") val typeId: Int?,
    @SerializedName("type") val type: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("thumbnailContent") val thumbnailContent: String?,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("keywords") val keywords: List<String>?,
    @SerializedName("excludedKeywords") val excludedKeywords: List<String>?,
    @SerializedName("isMine") val isMine: Boolean?,
    @SerializedName("bookmark") val bookmark: Boolean?,
    @SerializedName("score") val score: Int?
)
