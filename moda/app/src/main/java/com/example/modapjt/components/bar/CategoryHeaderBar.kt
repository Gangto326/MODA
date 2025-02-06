package com.example.modapjt.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CategoryHeaderBar(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)  // 높이 설정
            .background(Color.White),  // 배경색 설정
        contentAlignment = Alignment.Center
    ) {
        // 왼쪽 뒤로가기 버튼
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, // ← 아이콘
                    contentDescription = "뒤로가기",
                    tint = Color.Black
                )
            }
        }

        // 중앙 타이틀 (카테고리)
        Text(
            text = "카테고리",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

//@Composable
//fun SearchBar2() { // onBackClick: () -> Unit
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp)
//            .background(color = Color.White)
//            .padding(horizontal = 16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // 왼쪽 뒤로가기 버튼
//        IconButton(onClick = {}) { // onBackClick
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "뒤로가기",
//                tint = Color.Black
//            )
//        }
//
//        // 중앙 정렬을 위한 Spacer
//        Spacer(modifier = Modifier.weight(1f))
//
//        // 중앙 텍스트 (카테고리)
//        Text(
//            text = "음식",  // 원하는 카테고리 이름을 넣으면 됨
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//
//        // 오른쪽 여백을 맞추기 위한 Spacer
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}