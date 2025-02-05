package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.CircleShape

@Composable
fun TopThumbnail() {
    // 임시 색상 리스트로 대체
    val colors = listOf(
        Color.LightGray,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Cyan
    )

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentPage = remember { mutableStateOf(0) }

    // 자동 슬라이드 기능
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)  // 3초마다 자동 이동
            val nextIndex = (currentPage.value + 1) % colors.size
            coroutineScope.launch {
                listState.animateScrollToItem(nextIndex)
            }
            currentPage.value = nextIndex
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // 썸네일 슬라이드 (색상 박스)
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 10f)  // 16:10 비율 설정
        ) {
            items(colors.size) { index ->
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .aspectRatio(16 / 10f)
                        .background(colors[index])  // 색상으로 배경 설정
                )
            }
        }

        // 인디케이터 (현재 페이지 표시)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            colors.forEachIndexed { index, _ ->
                val indicatorColor = if (index == currentPage.value) Color.Gray else Color.LightGray
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .padding(4.dp)
                        .background(color = indicatorColor, shape = CircleShape)
                )
            }
        }
    }
}
