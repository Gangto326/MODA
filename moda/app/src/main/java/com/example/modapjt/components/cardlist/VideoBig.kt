import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.customTypography
import com.example.modapjt.R
import com.example.modapjt.components.video.YouTubePlayer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoBig(
    videoId: String,
    title: String,
    isMine: Boolean,
    thumbnailContent: String,
    isSelected: Boolean = false,
    keywords: List<String>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isTopVideo: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = 1.dp,
                color = if (!isMine) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp) // 테두리와 내용 사이 간격
    ) {
        // YouTubePlayer 컴포넌트
        YouTubePlayer(
            videoId = videoId,
            isTopVideo = isTopVideo,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
        )

        // 제목 텍스트
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 12.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        // 썸네일 콘텐츠 설명
        Text(
            text = thumbnailContent,
            fontSize = 14.sp,
            maxLines = 1,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
        )

        // 키워드와 아이콘 영역
        Box(modifier = Modifier.fillMaxWidth()) {
            // 키워드 FlowRow
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
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