
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.ui.theme.customTypography
import com.example.modapjt.R


// NewsSmall: 뉴스 컨텐츠를 카드 형태로 표시하는 컴포저블 함수
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsSmall(
    headline: String,
    description: String,
    imageUrl: String,
    isMine: Boolean,
    modifier: Modifier = Modifier,
    bookMark: Boolean,
    keywords: List<String>,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = if (!isMine) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(12.dp) // 테두리와 내용 사이 간격
    ) {
        // 상단 영역: 제목과 이미지를 가로로 배치
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
        ) {
            // 제목 + 내용을 감싸는 Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 뉴스 제목 텍스트
                Text(
                    text = headline,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
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

            // 썸네일 이미지
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        // 키워드와 아이콘 영역
        Box(modifier = Modifier.fillMaxWidth()) {
            // 키워드 FlowRow
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
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
                        .padding(end = 4.dp, bottom = 4.dp)
                        .size(20.dp)
                )
            }
        }
    }
}