package com.example.modapjt.components.cardlist

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app.ui.theme.customTypography

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsSelectionItem(
    title: String,
    description: String,
    imageUrl: String,
    isMine: Boolean,
    keywords: List<String>,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        color = when {
            isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            !isMine -> Color.Gray.copy(alpha = 0.1f)
            else -> Color.White
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 상단 영역: 제목과 이미지를 가로로 배치
            Row(
                verticalAlignment = Alignment.Top, // 상단 맞춤
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
            ) {
                // 🔥 제목 + 키워드를 감싸는 Column
                Column(
                    modifier = Modifier.weight(1f) // 🔥 남은 공간 모두 차지해서 왼쪽 정렬
                ) {
                    // 뉴스 제목 텍스트
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
//                    style = customTypography.titleMedium ,
                        maxLines = 2, // 최대 2줄까지 표시
                        overflow = TextOverflow.Ellipsis, // 넘치는 텍스트는 ...으로 표시
                    )

                    Text(
                        text = description,
                        style = customTypography.bodyMedium,
                        color = Color(0xFF797069),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                // 썸네일 이미지
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // 이미지 비율 유지하며 채우기
                    modifier = Modifier
                        .size(80.dp) // 70dp x 70dp 크기
                        .clip(RoundedCornerShape(8.dp))// 이미지 모서리 둥글게
                )
            }

            // 하단 영역: 키워드와 즐겨찾기 아이콘을 가로로 배치 (🔥 키워드가 제목 아래로 이동했으므로 삭제 가능)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp), // 상단 여백 추가
                horizontalArrangement = Arrangement.SpaceBetween, // 요소들을 양끝으로 정렬
                verticalAlignment = Alignment.CenterVertically // 세로 방향 중앙 정렬
            ) {
                // 키워드 목록 (최대 3개)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp) // 키워드 간 간격
                ) {
                    keywords.take(3).forEach { keyword ->
                        Text(
                            text = "# $keyword",
//                        fontSize = 12.sp,
                            style = customTypography.bodySmall,
                            color = Color(0xFFBAADA4), // 키워드는 파란색으로 표시( 고민 )
                        )
                    }
                }
            }
        }
    }
}