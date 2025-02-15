package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@Composable
fun ThumbnailIndicator(currentIndex: Int, totalItems: Int) {
    val defaultSize = 6.dp
    val selectedSize = 6.dp
    val spacing = 10.dp // ✅ 간격 조정

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(totalItems) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentIndex) selectedSize else defaultSize) // 크기 조정
                    .background(
                        if (index == currentIndex) Color(0xFFFFCD69) else Color(0xFFD9D9D9),
                        CircleShape
                    )
            )
            if (index < totalItems - 1) {
                Spacer(modifier = Modifier.width(spacing)) // ✅ 간격 일정하게 유지
            }
        }
    }
}
