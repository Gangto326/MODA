
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.example.modapjt.components.carddetail.ImageSlider
import com.example.modapjt.domain.model.CardDetail
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 블로그 글의 상세 내용을 보여주는 화면 컴포저블
 * LazyColumn을 사용하여 긴 컨텐츠를 스크롤 가능하게 표시
 *
 * @param cardDetail 표시할 블로그 글의 상세 정보를 담은 객체
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BlogDetailScreen(cardDetail: CardDetail) {
    val uriHandler = LocalUriHandler.current
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 상단 고정 이미지 슬라이더
        cardDetail.subContents.takeIf { it.isNotEmpty() }?.let { images ->
            ImageSlider(imageUrls = images)
        }

        // 스크롤 가능한 콘텐츠
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 블로그 글 제목
            item {
                Text(
                    text = cardDetail.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // 관련 키워드
            item {
                val limitedKeywords = cardDetail.keywords.take(3).joinToString(", ")
                Text(
                    text = "키워드: $limitedKeywords",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // 본문 내용
            item {
                MarkdownText(
                    markdown = cardDetail.content,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Divider(color = Color.Gray, thickness = 1.dp)
            }

            // 원본 글 링크
            item {
                Button(
                    onClick = { uriHandler.openUri(cardDetail.originalUrl) },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("원문 보기")
                }
            }

            // 작성 날짜
            item {
                Text(
                    text = "생성 날짜: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}