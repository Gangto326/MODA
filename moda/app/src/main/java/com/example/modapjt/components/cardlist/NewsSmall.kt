import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewsSmall(
    headline: String,  // 뉴스 제목
    keywords: List<String>,  // 핵심 키워드 (최대 3개)
    imageUrl: String,  // 썸네일 이미지 URL
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // ✅ 클릭 이벤트 추가
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // ✅ 카드 스타일 적용
            .background(Color.White) // ✅ 뉴스는 깔끔한 흰 배경 적용
            .clickable(onClick = onClick) // ✅ 클릭 가능하도록 추가
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ✅ 뉴스 텍스트 영역
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = headline,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold // ✅ 제목 Bold 스타일 적용
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ✅ 핵심 키워드 최대 3개 표시
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                keywords.take(3).forEach { keyword ->
                    Text(
                        text = "# " + keyword,
                        fontSize = 12.sp,
                        color = Color.Blue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp)) // ✅ 텍스트와 이미지 사이 간격

        // ✅ 썸네일 이미지 (오른쪽 배치)
        Box(
            modifier = Modifier
                .size(70.dp) // ✅ 정사각형 크기 유지
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray), // ✅ 이미지 영역 (예제)
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "이미지",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}
