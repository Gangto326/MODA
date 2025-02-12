package com.example.modapjt.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun TopThumbnail(
    imageUrl: String,
    title: String?,
    content: String?,
    currentIndex: Int, // ✅ 부모에서 전달받은 현재 페이지 인덱스
    totalItems: Int, // ✅ 부모에서 전달받은 전체 아이템 수
    onClick: () -> Unit
){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "Thumbnail Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ✅ 그라데이션 배경 추가
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, // 위쪽 완전 투명
                            Color.Black.copy(alpha = 0.3f), // 중간 살짝 어두움
                            Color.Black.copy(alpha = 0.8f) // 아래쪽 더 진하게
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp) // ✅ 패딩 조정
        ) {
            title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, // ✅ 두 줄 초과 방지
                    overflow = TextOverflow.Ellipsis // ✅ ... 처리
                )
            }
            content?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 2, // ✅ 두 줄 초과 방지
                    overflow = TextOverflow.Ellipsis // ✅ ... 처리
                )
            }
        }
        // ✅ 오른쪽 아래 페이지 인디케이터
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp) // 내부 패딩
        ) {
            Text(
                text = "${currentIndex + 1} / $totalItems", // ✅ 현재 페이지 / 전체 페이지
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
