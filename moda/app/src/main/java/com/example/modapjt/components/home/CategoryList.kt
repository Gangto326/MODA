package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryItem(name: String) {
    Column(
        modifier = Modifier
            .padding(4.dp)  // 아이템 주변 패딩
            .size(64.dp),   // 전체 아이템 크기 (정사각형)
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 회색 정사각형 박스
        Box(
            modifier = Modifier
                .size(48.dp)  // 박스 크기
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp))  // 둥근 모서리 회색 박스
        )

        Spacer(modifier = Modifier.height(4.dp))  // 아이콘과 텍스트 간격

        // 카테고리 이름 표시
        Text(
            text = name,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
