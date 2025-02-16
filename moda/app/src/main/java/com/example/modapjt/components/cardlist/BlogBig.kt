import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlogBig(
    title: String, // 블로그 제목
    description: String, // 블로그 설명 (요약)
    imageUrl: String, // 블로그 썸네일 이미지 URL
    modifier: Modifier = Modifier,
    isMine: Boolean,
    keywords: List<String>,
    onClick: () -> Unit = {} // 클릭 시 실행할 동작
) {
    // 카드 UI (터치 가능)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clickable(onClick = onClick), // 클릭 이벤트 추가
        colors = CardDefaults.cardColors(
            containerColor = if (!isMine) Color.Gray else Color.White // ✅ 배경색 적용
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // 상단 영역 (블로그 플랫폼 아이콘 + 제목)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)  // 아이콘과 텍스트 간격 조정
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox, // 아이콘 (예제: 블로그 아이콘)
                    contentDescription = "블로그",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
                Text(
                    text = title, // 블로그 제목
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, // 한 줄까지만 표시
                    overflow = TextOverflow.Ellipsis, // 길면 ...으로 생략
                    modifier = Modifier.weight(1f), // 남은 공간을 최대한 차지
                    lineHeight = 20.sp // 제목의 행간 설정
                )
            }
            // 블로그 설명 (요약)
            Text(
                text = description,
                style = customTypography.bodyMedium,
                color = Color(0xFF665F5B),
                lineHeight = 20.sp, // 설명의 행간 설정
                maxLines = 3, // 최대 2줄까지만 표시
                overflow = TextOverflow.Ellipsis, // 길면 ...으로 생략
                modifier = Modifier.padding(top = 8.dp) // 위쪽 여백 추가
            )

            // 🔥 키워드 간격 적용
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp), // 키워드 사이 간격 설정
                verticalArrangement = Arrangement.spacedBy(4.dp) // 여러 줄일 경우 간격 조정
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

            // 블로그 썸네일 이미지 (비율 유지, 테두리 추가)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp) // 위쪽 여백 추가
                    .clip(RoundedCornerShape(8.dp)) // 모서리 둥글게 처리
                    .border(1.dp, Color(0xFFF4F1ED), RoundedCornerShape(8.dp)) // 회색 테두리 추가
            ) {
                AsyncImage(
                    model = imageUrl, // 썸네일 이미지 URL
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // 이미지를 크롭하여 꽉 차게 표시
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
