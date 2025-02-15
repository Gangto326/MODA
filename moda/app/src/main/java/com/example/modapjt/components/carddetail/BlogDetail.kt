
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.components.carddetail.ImageSlider
import com.example.modapjt.domain.model.CardDetail
import com.example.modapjt.domain.viewmodel.SearchViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BlogDetailScreen(cardDetail: CardDetail) {
    val searchViewModel: SearchViewModel = viewModel()
    val uriHandler = LocalUriHandler.current
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)

    var showImage by remember { mutableStateOf(true) } // 이미지 표시 여부 상태

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔘 이미지 토글 버튼
        Button(
            onClick = { showImage = !showImage },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (showImage) "이미지 숨기기" else "이미지 보기")
        }


        // 🖼 이미지 슬라이더 (토글에 따라 표시)
        if (showImage && cardDetail.subContents.isNotEmpty()) {
            ImageSlider(imageUrls = cardDetail.subContents)
        }


        // 📜 본문 내용
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = cardDetail.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // 키워드 List
            val limitedKeywords = cardDetail.keywords
            item {
                Text(
                    text = "키워드: ${limitedKeywords.take(3).joinToString(", ")}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                MarkdownText(
                    markdown = cardDetail.content,
                    modifier = Modifier.padding(vertical = 8.dp),
                    keywords = limitedKeywords,
                    onKeywordClick = { keyword ->
                        searchViewModel.onKeywordClick(keyword)
                    }
                )
                Divider(color = Color.Gray, thickness = 1.dp)
            }

            item {
                Button(
                    onClick = { uriHandler.openUri(cardDetail.originalUrl) },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("원문 보기")
                }
            }

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
