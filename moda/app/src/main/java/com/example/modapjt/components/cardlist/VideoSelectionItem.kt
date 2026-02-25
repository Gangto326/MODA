package com.example.modapjt.components.cardlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.app.ui.theme.customTypography
import com.example.modapjt.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoSelectionItem(
    videoId: String,
    title: String,
    isMine: Boolean,
    bookMark: Boolean,
    keywords: List<String>,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    thumbnailContent: String,
    onClick: () -> Unit = {}
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    // 화면 크기에 따른 텍스트 크기 계산
    val iconSize = (screenWidth * 0.03f).coerceIn(20.dp, 28.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                onClick = onClick,
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
            ),
        shape = RoundedCornerShape(8.dp),
        color = when {
//            isSelected -> Color.DarkGray.copy(alpha = 0.3f)  // 선택됐을 때 색상
            !isMine -> Color.Gray
            else -> Color.White
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            style = customTypography.headlineMedium,
                            color = Color(0xFF2B2826),
                            lineHeight = 20.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = thumbnailContent,
                            style = customTypography.headlineMedium,
                            color = Color(0xFF2B2826),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    AsyncImage(
                        model = "https://img.youtube.com/vi/$videoId/0.jpg",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    keywords.take(3).forEach { keyword ->
                        Text(
                            text = "# $keyword",
                            style = customTypography.bodySmall,
                            color = Color(0xFFBAADA4),
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                }
            }
        }
    }
}