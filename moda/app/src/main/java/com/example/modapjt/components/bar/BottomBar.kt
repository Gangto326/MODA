package com.example.modapjt.components.bar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun BottomBar() {
    Box(
        modifier = Modifier.fillMaxSize(),  // 크기는 홈에서 관리
        contentAlignment = Alignment.Center  // 내용 중앙 정렬
    ) {
        Text(
            text = "하단 바 고정",
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

