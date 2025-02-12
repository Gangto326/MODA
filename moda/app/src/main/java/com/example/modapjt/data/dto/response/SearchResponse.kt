package com.example.modapjt.data.dto.response

import com.google.gson.annotations.SerializedName

// ✅ Search API 응답 데이터
data class SearchResponse(
    @SerializedName("images") val images: List<SearchItem>?,
    @SerializedName("keywords") val keywords: List<SearchItem>?,
    @SerializedName("forgotten") val forgotten: List<SearchItem>?,
    @SerializedName("thumbnails") val thumbnails: List<SearchItem>?,
    @SerializedName("todays") val todays: List<SearchItem>?,
    @SerializedName("videos") val videos: List<SearchItem>?
)

// ✅ 공통 데이터 모델
data class SearchItem(
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
