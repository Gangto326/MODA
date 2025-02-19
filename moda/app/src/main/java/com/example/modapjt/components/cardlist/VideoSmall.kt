
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.ui.theme.customTypography


// VideoSmall: 동영상 컨텐츠를 가로로 표시하는 컴포저블 함수
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoSmall(
    videoId: String,
    title: String,
    isMine: Boolean,
    bookMark: Boolean,
    keywords: List<String>,
    modifier: Modifier = Modifier,
    thumbnailContent: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
//            .padding(vertical = 4.dp) // 비디오 컨텐츠 하나 위아래 패딩
            .clickable(
                onClick = onClick,
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                )
    ) {
        // 🔹 썸네일 영역 (왼쪽)
        Box(
            modifier = Modifier
                .width (135.dp)
                .aspectRatio(16f/9f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (!isMine) MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.tertiary)
        ) {
            AsyncImage(
                model = "https://img.youtube.com/vi/$videoId/0.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // 🔹 제목 + 채널명 상단 정렬, 키워드 하단 정렬
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), // 🔥 키워드가 항상 하단 정렬되도록 Column을 전체 크기로 확장
        ) {
            // ✅ 제목과 채널명을 상단 고정
            Column(
                modifier = Modifier.fillMaxWidth(), // ✅ 제목+채널명이 전체 가로를 차지하도록 설정
                verticalArrangement = Arrangement.Top // ✅ 제목과 채널명을 상단 정렬
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = thumbnailContent,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 🔹 키워드가 항상 하단에 위치하도록 설정
            Spacer(modifier = Modifier.weight(1f)) // ✅ 키워드를 밀어내는 역할

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start), // ✅ 키워드를 왼쪽 정렬
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                keywords.forEach { keyword ->
                    Text(
                        text = "# $keyword",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = customTypography.bodySmall
                    )
                }
            }
        }

//        // 🔹 즐겨찾기 아이콘 (오른쪽 하단 정렬)
//        if (bookMark) {
//            Icon(
//                imageVector = Icons.Filled.Star,
//                contentDescription = "즐겨찾기",
//                tint = Color(0xFFFFD700),
//                modifier = Modifier
//                    .size(20.dp)
//                    .align(Alignment.Bottom)
//                    .padding(end = 8.dp)
//            )
//        }
    }
}
