package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomThumbnail(backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(width = 120.dp, height = 200.dp)  // 세로로 긴 직사각형
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))  // 각 썸네일 배경 색상과 둥근 모서리
    )
}

