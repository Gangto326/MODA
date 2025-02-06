package com.example.modapjt.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)  // 검색바 높이 설정
            .background(Color(0xFFFFF9C4)),  // 파스텔 톤 연한 노랑 (#FFF9C4)
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "검색바",  // 텍스트 변경
            color = Color(0xFF795548),  // 부드러운 브라운 톤으로 텍스트 색상 변경
            fontSize = 16.sp,  // 글씨 크기 조정
            fontWeight = FontWeight.Normal  // 폰트 굵기 조정
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
    SearchBar()
}
