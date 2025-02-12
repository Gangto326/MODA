
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
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
    val uriHandler = LocalUriHandler.current // URL 처리를 위한 핸들러

    // 날짜 포맷 변경 (년-월-일)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()  // 전체 화면 크기 사용
            .padding(16.dp) // 전체 컨텐츠에 패딩 적용
    ) {
        // 썸네일 이미지 표시
//        item {
//            cardDetail.thumbnailUrl?.let {
//                Image(
//                    painter = rememberAsyncImagePainter(it),
//                    contentDescription = "썸네일",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                )
//            }
//        }
        item {
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
            }
        }

        // 블로그 글 제목
        item {
            Text(
                text = cardDetail.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // 관련 키워드들을 쉼표로 구분하여 표시
        item {
            // 키워드 3개까지만 표시
            val limitedKeywords = cardDetail.keywords.take(3).joinToString(", ")
            Text(
                text = "키워드: $limitedKeywords",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // 마크다운 형식의 본문 내용을 렌더링
        item {
            MarkdownText(
                markdown = cardDetail.content,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp) // 컨텐츠 구분을 위한 구분선
        }

        // 원본 글 링크
        item {
            // URL을 버튼으로 변경
            Button(
                onClick = { uriHandler.openUri(cardDetail.originalUrl) },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("원문 보기")
            }
        }

        // 작성 날짜 정보
        item {
            Text(
                text = "생성 날짜: $formattedDate",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}