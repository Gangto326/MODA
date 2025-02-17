package com.example.modapjt.components.cardlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ScrollToTopButton(scrollState: LazyListState) {
    val showButton = remember { derivedStateOf { scrollState.firstVisibleItemIndex > 5 } }
    val coroutineScope = rememberCoroutineScope()  // Compose용 CoroutineScope

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedVisibility(
            visible = showButton.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp) // 크기 지정
                    .background(Color.White, shape = CircleShape) // 배경을 원형으로
                    .border(2.dp, Color.LightGray, CircleShape) // 테두리 추가
                    .clickable {
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(0)
                        }
                    },
                contentAlignment = Alignment.Center // 아이콘 중앙 정렬
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "맨 위로 가기",
                    tint = Color.Black // 아이콘 색상을 검정으로
                )
            }
        }
    }
}
