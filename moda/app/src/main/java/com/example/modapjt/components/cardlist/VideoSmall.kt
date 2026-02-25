
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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.res.painterResource
import com.example.modapjt.R


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
            .border(
                width = 1.dp,
                color = if (!isMine) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp) // 테두리와 내용 사이 간격
            .clickable(
                onClick = onClick,
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
            )
    ) {
        // 🔹 썸네일 영역 (왼쪽)
        Box(
            modifier = Modifier
                .width(135.dp)
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

        // 오른쪽 정보 영역
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // 제목과 채널명
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

            // 키워드와 아이콘 영역
            Box(modifier = Modifier.fillMaxWidth()) {
                // 키워드 FlowRow
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    keywords.take(3).forEach { keyword ->
                        Text(
                            text = "# $keyword",
                            style = customTypography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                        )
                    }
                    // 남의 글(isMine=false)인 경우, 빈 공간 추가 (아이콘 공간 확보)
                    if (!isMine) {
                        Spacer(modifier = Modifier.width(30.dp))
                    }
                }

                // 남의 글(isMine=false)인 경우에만 오른쪽 아래에 아이콘 표시
                if (!isMine) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_other_people),
                        contentDescription = "Other's content",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 8.dp, bottom = 8.dp)
                            .size(20.dp)
                    )
                }
            }
        }
    }
}