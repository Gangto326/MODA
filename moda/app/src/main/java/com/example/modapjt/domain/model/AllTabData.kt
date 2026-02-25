package com.example.modapjt.domain.model

import com.example.modapjt.data.dto.response.TopScore

data class AllTabData(
    val cards: List<Card>,
    val topScores: List<TopScore>
)