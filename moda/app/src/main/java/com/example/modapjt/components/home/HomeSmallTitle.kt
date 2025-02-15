package com.example.modapjt.components.home

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.* // fillMaxWidth(), width() 등 포함
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomeSmallTitle(
    title: String,
    description: String,
    modifier: Modifier = Modifier // 기본 Modifier 제공
) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // fillMaxWidth 적용
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(modifier), // 기존 modifier 추가
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF2B2826)
        )
        Spacer(modifier = Modifier.width(6.dp)) // width 오류 해결됨
        Text(
            text = description,
            fontSize = 10.sp,
            color = Color(0xFFBAADA4)
        )
    }
//    Spacer(modifier = Modifier.height(8.dp))

}
