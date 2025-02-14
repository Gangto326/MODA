
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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

@Composable
fun BlogBig(
    title: String, // 블로그 제목
    description: String, // 블로그 설명 (요약)
    imageUrl: String, // 블로그 썸네일 이미지 URL
    modifier: Modifier = Modifier,
    isMine: Boolean,
    onClick: () -> Unit = {} // 클릭 시 실행할 동작
) {
    // 카드 UI (터치 가능)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick), // 클릭 이벤트 추가
        shape = RoundedCornerShape(12.dp), // 카드 모서리를 둥글게 설정
        colors = CardDefaults.cardColors(
            containerColor = if (!isMine) Color.Gray else Color.White // ✅ 배경색 적용
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 그림자 효과 추가
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // 내부 여백 추가
        ) {
            // 상단 영역 (블로그 플랫폼 아이콘 + 제목)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)  // 아이콘과 텍스트 간격 조정
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox, // 아이콘 (예제: 블로그 아이콘)
                    contentDescription = "블로그",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
                Text(
                    text = title, // 블로그 제목
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium,
                    style = customTypography.headlineMedium,
                    maxLines = 1, // 한 줄까지만 표시
                    overflow = TextOverflow.Ellipsis, // 길면 ...으로 생략
                    modifier = Modifier.weight(1f) // 남은 공간을 최대한 차지
                )
            }
            // 블로그 설명 (요약)
            Text(
                text = description,
//                fontSize = 14.sp,
                style = customTypography.bodyMedium,
                color = Color.Gray,
                maxLines = 2, // 최대 2줄까지만 표시
                overflow = TextOverflow.Ellipsis, // 길면 ...으로 생략
                modifier = Modifier.padding(top = 8.dp) // 위쪽 여백 추가
            )
            // 블로그 썸네일 이미지 (비율 유지)
            AsyncImage(
                model = imageUrl, // 썸네일 이미지 URL
                contentDescription = null,
                contentScale = ContentScale.Crop, // 이미지를 크롭하여 꽉 차게 표시
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp) // 위쪽 여백 추가
                    .aspectRatio(16f / 9f) // 16:9 비율 유지
                    .clip(RoundedCornerShape(8.dp)) // 모서리 둥글게 처리
            )
        }
    }
}