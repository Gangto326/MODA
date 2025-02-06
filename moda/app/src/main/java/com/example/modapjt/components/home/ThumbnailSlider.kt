package com.example.modapjt.components.home

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay

@Composable
fun ThumbnailSlider() {
    val colors = listOf(
        Color(0xFFFFCDD2),  // 연한 빨강
        Color(0xFFC8E6C9),  // 연한 초록
        Color(0xFFBBDEFB),  // 연한 파랑
        Color(0xFFFFF9C4),  // 연한 노랑
        Color(0xFFD1C4E9)   // 연한 보라
    )

    var currentIndex by remember { mutableStateOf(0) }

    // 자동 슬라이드 기능
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)  // 2초마다 자동 슬라이드
            currentIndex = (currentIndex + 1) % colors.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 0) {
                        // 오른쪽으로 드래그 → 이전 썸네일
                        currentIndex = if (currentIndex == 0) colors.lastIndex else currentIndex - 1
                    } else {
                        // 왼쪽으로 드래그 → 다음 썸네일
                        currentIndex = (currentIndex + 1) % colors.size
                    }
                }
            }
    ) {
        // 썸네일 표시
        TopThumbnail(color = colors[currentIndex])

        // 하단 인디케이터 표시
        ThumbnailIndicator(currentIndex = currentIndex, totalItems = colors.size)
    }
}
