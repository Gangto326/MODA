package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

@Composable
fun WeeklyKeyword(keyword: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50))  // 주황빛 노랑색 배경
            .padding(horizontal = 20.dp, vertical = 10.dp),  // 이게 도대체 뭐에 쓰느거지?
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = keyword,
            fontSize = 14.sp,  // 글자 크기 증가
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary  // 검정 글자
        )
    }
}
