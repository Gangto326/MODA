
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember

@Composable
fun ImageGrid(
    imageUrls: List<String>,  // 이미지 리스트
    isMine: Boolean,  // 내가 저장한 이미지 여부
    bookMarks: List<Boolean>, // 즐겨찾기 여부 리스트
    onClick: (Int) -> Unit = {} // 클릭 이벤트 (index 전달)
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageSize = screenWidth / 3  // 한 줄에 3개씩 배치 (화면 1/3 크기)

    // ✅ 3개씩 그룹으로 묶어서 줄 단위로 배치
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp) // 🔥 행 간 간격 10dp
    ) {
        imageUrls.chunked(3).forEachIndexed { rowIndex, rowImages ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // 🔥 이미지 간 가로 간격 8dp
            ) {
                rowImages.forEachIndexed { index, imageUrl ->
                    ImageSmall(
                        imageUrl = imageUrl,
                        isMine = isMine,
                        bookMark = bookMarks[rowIndex * 3 + index],
                        onClick = { onClick(rowIndex * 3 + index) },
                        modifier = Modifier.weight(1f) // 🔥 동일한 크기 유지
                    )
                }

                // 3개 미만일 경우 빈 `Spacer` 추가하여 정렬 유지
                repeat(3 - rowImages.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// ✅ 단일 이미지 카드 (즐겨찾기 아이콘 + 그림자 추가)
@Composable
fun ImageSmall(
    imageUrl: String,
    modifier: Modifier = Modifier,
    isMine: Boolean,
    bookMark: Boolean,
    onClick: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageSize = screenWidth / 3  // 한 줄에 3개씩 배치

    Box(
        modifier = modifier
            .size(imageSize)
            .clip(RoundedCornerShape(8.dp)) // 🔥 모서리 둥글게 처리
            .background(if (!isMine) Color.Gray else Color.White)
            .clickable(
                onClick = onClick,
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                )
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // ✅ 즐겨찾기 아이콘 (내 이미지이고 즐겨찾기된 경우에만 표시)
        if (bookMark && isMine) {
            Icon(
                imageVector = Icons.Filled.Star, // 별 모양 아이콘
                contentDescription = "즐겨찾기됨",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(30.dp) // 아이콘 크기
                    .align(Alignment.TopEnd) // 우측 하단에 배치
                    .padding(6.dp) // 여백 추가
            )
        }
    }
}
