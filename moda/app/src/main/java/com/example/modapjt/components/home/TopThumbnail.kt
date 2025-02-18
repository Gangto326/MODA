package com.example.modapjt.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
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
) {

    Box(
        modifier = Modifier
            .fillMaxWidth() // 화면 너비에 맞게
            .aspectRatio(16 / 9f) // 16:9 비율로 높이 자동 조정
            .clickable(
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
            ) { onClick() } // 클릭 이벤트 처리
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "Thumbnail Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth // 이미지가 가로 크기에 맞게 꽉 채워짐
        )

        // 그라데이션 배경
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, // 위쪽은 투명
                            Color.Black.copy(alpha = 0.3f), // 중간 부분은 살짝 어두운 색
                            Color.Black.copy(alpha = 0.8f) // 아래쪽은 더 어두운 색
                        )
                    )
                )
        )


        // 제목과 내용이 포함된 Column
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp) // 좌우 가로 패딩 추가
        ) {
            // 제목
            title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 18.sp, // 제목 폰트 사이즈
                    lineHeight = 20.sp, // 행간을 줄여서 간격을 조정
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, // 두 줄로 제한
                    overflow = TextOverflow.Ellipsis, // ... 처리
                            style = TextStyle(
                            shadow = Shadow( // 🔥 그림자 효과 적용
                                color = Color.Black.copy(alpha = 0.8f),
                                blurRadius = 4f
                            )
                            )
                )
            }

            // 🔹 Spacer를 사용한 간격 추가 (대체 가능)
            Spacer(modifier = Modifier.height(4.dp)) // ✅ 제목과 내용 사이 4dp 간격

            // 내용과 투명 박스를 가로로 정렬 (Row 사용)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp) // 위쪽 여백 추가
            ) {
                // 내용은 왼쪽에 위치하고 남은 공간을 차지하도록 설정
                content?.let {
                    Text(
                        text = it,
                        color = Color(0xFFECECEC),
                        fontSize = 12.sp,
                        lineHeight = 16.sp, // 행간을 줄여서 간격을 조정
                        maxLines = 2, // 두 줄로 제한
                        overflow = TextOverflow.Ellipsis, // ... 처리
                        modifier = Modifier.weight(1f), // 내용이 왼쪽에 차지하도록 설정
                        style = TextStyle(
                            shadow = Shadow( // 🔥 그림자 효과 적용
                                color = Color.Black.copy(alpha = 0.8f),
                                blurRadius = 4f
                            )
                        )
                    )
                }

                // 투명 박스는 오른쪽에 고정된 크기만 차지
                Box(
                    modifier = Modifier
                        .height(30.dp) // 페이지 인디케이터 크기와 동일하게 설정
                        .width(50.dp) // 가로 크기 넓히기
                        .background(
                            Color.White.copy(alpha = 0.0f),
                            RoundedCornerShape(6.dp)
                        ) // 투명 배경
                        .padding(horizontal = 12.dp, vertical = 4.dp) // 내부 패딩
                )
            }

        }

        // 페이지 인디케이터
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .background(color = Color(0xFF665F5B).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 0.dp) // 내부 패딩
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 현재 페이지를 굵게 표시
                Text(
                    text = "${currentIndex + 1}",
                    color = Color(0xFFECECEC),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold, // 현재 페이지 숫자만 굵게 설정
                    textAlign = TextAlign.Center
                )

                // 구분을 위한 슬래시
                Text(
                    text = " / ",
                    color = Color(0xFFECECEC),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium, // 나머지 부분은 기본 폰트 굵기
                    textAlign = TextAlign.Center
                )

                // 전체 페이지 수
                Text(
                    text = "$totalItems",
                    color = Color(0xFFECECEC),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium, // 전체 페이지 숫자는 굵기 그대로
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}