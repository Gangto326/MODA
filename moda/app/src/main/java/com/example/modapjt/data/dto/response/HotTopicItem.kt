package com.example.modapjt.data.dto.response

import com.google.gson.annotations.SerializedName

data class HotTopicItem(
    @SerializedName("rank") val rank: Int,
    @SerializedName("topic") val topic: String,
    @SerializedName("change") val change: Int
)
