import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
fun NewsBig(
    title: String,
    keywords: List<String>,
    description: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    isMine: Boolean,
    isSelected: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.3f)
                !isMine -> MaterialTheme.colorScheme.onSecondary
                else -> MaterialTheme.colorScheme.tertiary
            }
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            // 상단 헤더: 뉴스 아이콘 + 제목
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
//                Icon(
//                    imageVector = Icons.Default.MailOutline,
//                    contentDescription = "뉴스",
//                    modifier = Modifier.size(32.dp),
//                    tint = Color.Gray
//                )
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    lineHeight = 20.sp
                )
            }
            // 뉴스 설명 (요약)
            Text(
                text = description,
                style = customTypography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 20.sp, // 설명의 행간 설정
                maxLines = 3, // 최대 2줄까지만 표시
                overflow = TextOverflow.Ellipsis, // 길면 ...으로 생략
                modifier = Modifier.padding(top = 8.dp) // 위쪽 여백 추가
            )

            // 키워드 FlowRow
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                keywords.take(3).forEach { keyword ->
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSecondary,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = keyword,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            style = customTypography.bodySmall
                        )
                    }
                }
            }

            // 뉴스 이미지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}