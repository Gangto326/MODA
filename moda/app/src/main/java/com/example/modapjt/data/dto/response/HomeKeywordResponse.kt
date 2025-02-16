package com.example.modapjt.data.dto.response

import com.google.gson.annotations.SerializedName

data class HomeKeywordResponse(
    @SerializedName("topKeywords") val topKeywords: List<String>,
    @SerializedName("creator") val creator: String,
    @SerializedName("categories") val categories: Map<Long, Boolean>
)
