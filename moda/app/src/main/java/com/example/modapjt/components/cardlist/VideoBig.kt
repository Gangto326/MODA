
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.ui.theme.customTypography
import com.example.modapjt.components.video.YouTubePlayer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoBig(
    videoId: String,
    title: String,
    isMine: Boolean,
    thumbnailContent: String,
    keywords: List<String>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isTopVideo: Boolean // 파라미터 이름을 수정
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick), // 클릭 가능하도록 설정
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!isMine) Color.Gray else Color.White
        ),
        // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // YouTubePlayer를 영상 썸네일 위치에 추가
            YouTubePlayer(
                videoId = videoId,
                isTopVideo = isTopVideo, // 파라미터 전달
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f) // 영상 비율 16:9로 설정
                    .clip(RoundedCornerShape(8.dp)) // 모서리 둥글게 설정
            )

            // 제목 텍스트
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B2826),
                modifier = Modifier.padding(start = 4.dp, end = 4.dp,top = 12.dp),
                maxLines = 2, // 최대 2줄까지 표시
                overflow = TextOverflow.Ellipsis, // 2줄 이상일 경우 "..."로 표시
            )


            // 썸네일 콘텐츠 설명
            Text(
                text = thumbnailContent,
                fontSize = 14.sp,
                maxLines = 1,
                color = Color(0xFF665F5B),
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 2.dp)

            )

            // 채널명과 키워드를 가로로 정렬
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(vertical = 8.dp), // 패딩 추가
                horizontalArrangement = Arrangement.Absolute.Left // 양쪽 정렬
            ) {
//                // 채널명 Text
//                Text(
//                    text = "채널명", // 채널명을 적당히 넣기
//                    color = Color(0xFF2B2826),
//                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal)
//                )

                // 🔥 키워드 간격 적용
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 10.dp), // 키워드 위아래 패딩
                    horizontalArrangement = Arrangement.spacedBy(10.dp), // 키워드 사이 간격 설정
                    verticalArrangement = Arrangement.spacedBy(6.dp) // 여러 줄일 경우 간격 조정
                ) {
                    keywords.take(3).forEach { keyword ->  // 최대 3개의 키워드만 표시
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color(0xFFB8ACA5), RoundedCornerShape(50)) // 테두리 추가
                                .padding(horizontal = 14.dp, vertical = 6.dp) // 키워드 패딩
                                .clip(RoundedCornerShape(50)), // 원형 모양으로 둥글게 처리
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = keyword, // 해시태그 형식
                                style = customTypography.bodySmall,
                                color = Color(0xFFBAADA4),
                                fontSize = 12.sp
                            )
                        }
                    }

                }
            }

            // 구분선 추가
//            Divider(color = Color.Gray, thickness = 2.dp) // 구분선 추가
        }
    }
}
