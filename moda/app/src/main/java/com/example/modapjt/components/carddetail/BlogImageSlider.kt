package com.example.modapjt.components.carddetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(imageUrls: List<String>) {
    val pagerState = rememberPagerState { imageUrls.size }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)) {

        // 이미지 슬라이더
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = rememberAsyncImagePainter(imageUrls[page]),
                contentDescription = "블로그 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 좌측 화살표 (이전 이미지)
        if (pagerState.currentPage > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .size(50.dp) // 터치 영역
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        indication = null, // 터치 효과 제거
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left), // 좌측 화살표 아이콘
                    contentDescription = "이전 이미지",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp).alpha(0.5f) // 아이콘 크기 설정
                )
            }
        }

        // 우측 화살표 (다음 이미지)
        if (pagerState.currentPage < imageUrls.size - 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(50.dp) // 터치 영역
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        indication = null, // 터치 효과 제거
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right), // 우측 화살표 아이콘
                    contentDescription = "다음 이미지",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(32.dp) // 아이콘 크기 설정
                        .alpha(0.5f)
                )
            }
        }

        // 인디케이터 (현재 페이지 / 전체 페이지)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(5.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = "${pagerState.currentPage + 1} / ${imageUrls.size}",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
