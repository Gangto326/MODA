
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.ui.theme.customTypography


// VideoSmall: 동영상 컨텐츠를 가로로 표시하는 컴포저블 함수
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoSmall(
    videoId: String, // YouTube 동영상 ID
    title: String, // 동영상 제목
    isMine: Boolean, // 내가 저장한 동영상 여부
    bookMark: Boolean, // 즐겨찾기 여부
    keywords: List<String>,// 동영상 관련 키워드 목록
    modifier: Modifier = Modifier,
    thumbnailContent: String,
    onClick: () -> Unit = {}// 클릭 시 실행될 동작
) {
    // 메인 컨테이너: 가로 방향으로 컨텐츠 배치
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // 위아래 여백 추가
            .clickable(onClick = onClick)  // 클릭 가능하도록 설정
    ) {
        // 썸네일 영역 (왼쪽)
        Box(
            modifier = Modifier
                .size(width = 135.dp, height = (140 * 9 / 16).dp) // 16:9 비율 적용
                .clip(RoundedCornerShape(8.dp)) // ✅ 8dp 둥근 모서리 적용
//                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)) // ✅ 둥근 테두리 적용
                .background(if (!isMine) Color.White.copy(alpha = 0.8f) else Color.Black) // 배경색 유지
        ) {
            // YouTube 썸네일 이미지
            AsyncImage(
                model = "https://img.youtube.com/vi/$videoId/0.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }


        // 썸네일과 텍스트 사이 간격
        Spacer(modifier = Modifier.width(14.dp))

        // 제목과 키워드 영역
        Column(
            modifier = Modifier
                .weight(1f), // 남은 공간 모두 차지
//                .align(Alignment.CenterVertically) // 세로 방향 중앙 정렬

            verticalArrangement = Arrangement.Top // ✅ 제목을 항상 맨 위로 정렬
        ) {
            // 동영상 제목 : 맨 위에 고정
            Text(
                text = title,
//                fontSize = 16.sp,
                maxLines = 2, // 최대 2줄까지 표시
                overflow = TextOverflow.Ellipsis, // 넘치는 텍스트는 ...으로 표시
                style = customTypography.headlineMedium


            )
            // 동영상 채널명
            Text(
                text = thumbnailContent,
                fontSize = 14.sp,
                maxLines = 2, // 최대 2줄까지 표시
                overflow = TextOverflow.Ellipsis // 넘치는 텍스트는 ...으로 표시
            )

            // 키워드 목록 (최대 3개, 공백으로 구분)
//            Text(
//                text = keywords.take(3).joinToString(" "), // 키워드를 공백으로 연결
//                fontSize = 12.sp,
//                color = Color.Gray, // 회색으로 표시
//                modifier = Modifier.padding(top = 4.dp)
//            )

            Spacer(modifier = Modifier.padding(top = 4.dp)) // 제목과 키워드 간 간격 조정

            // 키워드 목록을 한 줄에 배치하되, 넘칠 경우 자동 줄바꿈
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp), // 해시태그 간 간격
                verticalArrangement = Arrangement.spacedBy(4.dp) // 여러 줄일 경우 줄 간격
            ) {
                keywords.forEach { keyword ->
                    Text(
                        text = "# $keyword",

//                        fontSize = 12.sp,
                        color = Color(0xFFBAADA4),
                        style = customTypography.bodySmall
//                                style = TextStyle(lineHeight = 14.sp) // 줄 간격 최소화
                    )
                }
            }


        }

        // 즐겨찾기 아이콘 (오른쪽)
        if (bookMark) {
            Icon(
                imageVector = Icons.Filled.Star, // 별 모양 아이콘
                contentDescription = "즐겨찾기",
                tint = Color(0xFFFFD700), // 노란색 별표
                modifier = Modifier
                    .size(20.dp) // 아이콘 크기
                    .align(Alignment.Bottom) // 아래쪽 정렬
                    .padding(end = 8.dp) // 오른쪽 여백
            )
        }
    }
}
