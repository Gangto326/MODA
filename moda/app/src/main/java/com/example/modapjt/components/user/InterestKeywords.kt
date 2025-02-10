package com.example.modapjt.components.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InterestKeywords(keywords: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()) // 가로 스크롤 가능하게 설정
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        keywords.forEach { keyword ->
            KeywordChip(keyword, onClick = {
                //온클릭
            })
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun KeywordChip(keyword: String, onClick: (String) -> Unit) { // ✅ 클릭 이벤트 추가
    Box(
        modifier = Modifier
            .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(20.dp))
            .clickable { onClick(keyword) } // ✅ 클릭 이벤트 추가
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = keyword,
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

