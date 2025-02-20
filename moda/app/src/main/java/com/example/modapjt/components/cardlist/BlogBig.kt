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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlogBig(
    title: String, // 블로그 제목
    description: String, // 블로그 설명 (요약)
    imageUrl: String, // 블로그 썸네일 이미지 URL
    modifier: Modifier = Modifier,
    isMine: Boolean,
    isSelected: Boolean = false,  // isSelected 파라미터 추가
    keywords: List<String>,
    onClick: () -> Unit = {} // 클릭 시 실행할 동작
) {
    // 카드 대신 테두리가 있는 Column으로 변경
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .border(
                width = 1.dp,
                color = if (!isMine) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp) // 테두리와 내용 사이 간격
            .clickable(onClick = onClick)
    ) {
        // 상단 영역 (블로그 플랫폼 아이콘 + 제목)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)  // 아이콘과 텍스트 간격 조정
        ) {
            Text(
                text = title, // 블로그 제목
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2, // 두 줄까지만 표시
                color = MaterialTheme.colorScheme.onPrimary,
                overflow = TextOverflow.Ellipsis, // 길면 ...으로 생략
                modifier = Modifier.weight(1f), // 남은 공간을 최대한 차지
                lineHeight = 20.sp // 제목의 행간 설정
            )
        }

        // 블로그 설명 (요약)
        Text(
            text = description,
            style = customTypography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            lineHeight = 20.sp, // 설명의 행간 설정
            maxLines = 3, // 최대 3줄까지만 표시
            overflow = TextOverflow.Ellipsis, // 길면 ...으로 생략
            modifier = Modifier.padding(top = 8.dp) // 위쪽 여백 추가
        )

        // 블로그 썸네일 이미지 (비율 유지, 테두리 추가)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp) // 여백 조정
                .clip(RoundedCornerShape(8.dp)) // 모서리 둥글게 처리
                .border(1.dp, MaterialTheme.colorScheme.onSecondary, RoundedCornerShape(8.dp)) // 회색 테두리 추가
        ) {
            AsyncImage(
                model = imageUrl, // 썸네일 이미지 URL
                contentDescription = null,
                contentScale = ContentScale.Crop, // 이미지를 크롭하여 꽉 차게 표시
                modifier = Modifier.fillMaxWidth()
            )
        }

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