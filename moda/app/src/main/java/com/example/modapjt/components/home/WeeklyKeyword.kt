package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment

@Composable
fun WeeklyKeyword(keyword: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFCC80), shape = RoundedCornerShape(50))  // 주황빛 노랑색 배경
            .padding(horizontal = 14.dp, vertical = 6.dp),  // 크기 조금 키우기
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = keyword,
            fontSize = 14.sp,  // 글자 크기 증가
            color = Color.White  // 흰색 글자
        )
    }
}
