
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.domain.model.CardDetail
import com.example.modapjt.domain.viewmodel.SearchViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 뉴스 기사의 상세 내용을 보여주는 화면 컴포저블
 * 제목, 썸네일, 본문 등을 세로로 배치
 *
 * @param cardDetail 표시할 뉴스 기사의 상세 정보를 담은 객체
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsDetailScreen(cardDetail: CardDetail) {
    val searchViewModel: SearchViewModel = viewModel()
    val uriHandler = LocalUriHandler.current

    // 날짜 포맷 변경 (년-월-일)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                // 썸네일 이미지가 있는 경우에만 표시
//        cardDetail.thumbnailUrl?.let {
//            Image(
//                painter = rememberAsyncImagePainter(it),
//                contentDescription = "썸네일",
//                modifier = Modifier.height(200.dp)
//            )
//        }
                // 썸네일 이미지 표시
                cardDetail.thumbnailUrl?.let { url ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        var isLoading by remember { mutableStateOf(true) }
                        var isError by remember { mutableStateOf(false) }

                        Image(
                            painter = rememberAsyncImagePainter(
                                model = url,
                                onState = { state ->
                                    isLoading = state is AsyncImagePainter.State.Loading
                                    isError = state is AsyncImagePainter.State.Error
                                }
                            ),
                            contentDescription = "썸네일",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // 로딩 중 표시
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        // 에러 표시
                        if (isError) {
                            Text(
                                text = "이미지를 불러올 수 없습니다",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 뉴스 제목
                Text(
                    text = cardDetail.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 뉴스 관련 키워드
                val limitedKeywords = cardDetail.keywords
                Text(
                    text = "키워드: ${limitedKeywords.take(3).joinToString(", ")}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // 뉴스 본문
//                Text(
//                    text = cardDetail.content,
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.padding(vertical = 8.dp)
//                )
                MarkdownText(
                    markdown = cardDetail.content,
                    modifier = Modifier.padding(vertical = 8.dp),
                    keywords = limitedKeywords,
                    onKeywordClick = { keyword ->
                        searchViewModel.onKeywordClick(keyword)
                    }
                )



                // 원본 뉴스 링크 -> URL 버튼
                Button(
                    onClick = { uriHandler.openUri(cardDetail.originalUrl) },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("원문 보기")
                }

                // 뉴스 작성 날짜
                Text(
                    text = "생성 날짜: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}