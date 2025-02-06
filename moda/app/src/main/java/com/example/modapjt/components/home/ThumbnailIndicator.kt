package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ThumbnailIndicator(currentIndex: Int, totalItems: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(totalItems) { index ->
            val isSelected = index == currentIndex
            val indicatorColor = if (isSelected) Color(0xFFFFF176) else Color(0xFFEEEEEE)

            Box(
                modifier = Modifier
                    .size(10.dp)  // 너비와 높이를 동일하게 설정
                    .aspectRatio(1f)  // 강제 1:1 비율로 원형 유지
                    .background(color = indicatorColor, shape = CircleShape)
            )

            if (index != totalItems - 1) {
                Spacer(modifier = Modifier.width(4.dp))  // 인디케이터 간 간격 조절
            }
        }
    }
}
