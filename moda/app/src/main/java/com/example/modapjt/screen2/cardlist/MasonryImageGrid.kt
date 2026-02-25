package com.example.modapjt.screen2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage



@Composable
fun MasonryImageGrid(
    imageUrls: List<String>,
    isMineList: List<Boolean>,
    cardIdList: List<String>,  // ✅ 카드 ID 추가
    onImageClick: (String) -> Unit
) {
    val columnCount = 2  // ✅ 2열 Masonry 스타일

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // ✅ 좌우 패딩 적용
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (columnIndex in 0 until columnCount) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val filteredItems = imageUrls.zip(cardIdList) // ✅ 이미지와 카드 ID를 매칭
                    .filterIndexed { index, _ -> index % columnCount == columnIndex } // ✅ 컬럼 필터링

                filteredItems.forEach { (imageUrl, cardId) -> // ✅ 원래 카드 ID와 매칭됨
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth, // ✅ 가로에 맞추고, 높이는 원본 비율 유지
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium) // ✅ 둥근 모서리 적용
                            .clickable(
                                indication = null, // 클릭 효과 제거
                                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                            ) {
                                onImageClick(cardId) // ✅ 올바른 `cardId` 전달
                            }
                    )
                }
            }
        }
    }
}
