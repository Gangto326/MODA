import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.app.ui.theme.customTypography
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlogSmall(
    title: String,
    description: String,
    imageUrl: String,
    isMine: Boolean,
    keywords: List<String>,
    bookMark: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(if (!isMine) Color.Gray.copy(alpha = 0.2f) else Color.White)
            .clickable(
                onClick = onClick,
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                )
    ) {
        Row(
            verticalAlignment = Alignment.Top // 이미지 상단에 맞춤
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = customTypography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = customTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp)) // 🔥 제목과 이미지 사이 간격 추가

            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        // 🔥 키워드 간격 적용
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp), // ✅ 키워드 사이 간격 설정
            verticalArrangement = Arrangement.spacedBy(4.dp) // ✅ 여러 줄일 경우 간격 조정
        ) {
            keywords.take(3).forEach { keyword ->
                Text(
                    text = "# $keyword",
                    style = customTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }

//        if (bookMark && isMine) {
//            Icon(
//                imageVector = Icons.Filled.Star,
//                contentDescription = "즐겨찾기됨",
//                tint = Color(0xFFFFCD69),
//                modifier = Modifier.size(20.dp)
//            )
//        }
    }
}
