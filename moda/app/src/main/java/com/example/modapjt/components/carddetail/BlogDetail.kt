
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.ui.theme.customTypography
import com.example.modapjt.R
import com.example.modapjt.components.carddetail.ImageSlider
import com.example.modapjt.domain.model.CardDetail
import com.example.modapjt.domain.viewmodel.SearchViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BlogDetailScreen(cardDetail: CardDetail) {
    val searchViewModel: SearchViewModel = viewModel()
    val uriHandler = LocalUriHandler.current
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)

    var showImage by remember { mutableStateOf(true) } // 이미지 표시 여부 상태

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    // 키워드 List
    val limitedKeywords = cardDetail.keywords

    // 화면 크기에 따른 동적 패딩 계산
    val horizontalPadding = (screenWidth * 0.04f).dp  // 화면 너비의 4%
    val verticalPadding = (screenWidth * 0.03f).dp    // 화면 너비의 3%

    // 화면 크기에 따른 글자 크기 스케일 계산
    val fontScale = when {
        screenWidth > 600 -> 1.0f  // 태블릿
        screenWidth > 400 -> 0.8f  // 일반 폰
        else -> 0.6f              // 작은 폰
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔘 이미지 토글 버튼
//        Button(
//            onClick = { showImage = !showImage },
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Text(if (showImage) "이미지 숨기기" else "이미지 보기")
//        }


        // 🖼 이미지 슬라이더 (토글에 따라 표시)
        if (showImage && cardDetail.subContents.isNotEmpty()) {
            ImageSlider(imageUrls = cardDetail.subContents)
        }


        // 📜 본문 내용
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding)
        ) {
            item {
                // 카테고리와 날짜
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = when (cardDetail.categoryId) {
                            1 -> "전체"
                            2 -> "트렌드"
                            3 -> "오락"
                            4 -> "금융"
                            5 -> "여행"
                            6 -> "음식"
                            7 -> "IT"
                            8 -> "디자인"
                            9 -> "사회"
                            10 -> "건강"
                            else -> "기타"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = LocalDateTime.parse(cardDetail.createdAt)
                            .format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            // 제목
            item {
                Text(
                    text = cardDetail.title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize * fontScale,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 30.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .wrapContentWidth(Alignment.Start),  // 컨텐츠는 중앙, 텍스트는 왼쪽 정렬
                )
                Spacer(modifier = Modifier.height(20.dp))
            }



            // 🔥 키워드 간격 적용
            item {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp), // 키워드 사이 간격 설정
                    verticalArrangement = Arrangement.spacedBy(4.dp) // 여러 줄일 경우 간격 조정
                ) {
                    limitedKeywords.take(3).forEach { keyword ->  // 최대 3개의 키워드만 표시
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .border(1.dp, Color(0xFFB8ACA5), RoundedCornerShape(50)) // 테두리 추가
                                .padding(horizontal = 14.dp) // 키워드 패딩
                                .clip(RoundedCornerShape(50)), // 원형 모양으로 둥글게 처리
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = keyword, // 해시태그 형식
//                                style = customTypography.bodySmall,
                                style = customTypography.bodySmall.copy(
                                    lineHeight = 16.sp,  // 라인 높이 설정
                                    textAlign = TextAlign.Center,  // 텍스트 정렬
                                ),
                                color = Color(0xFFBAADA4),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
//                                modifier = Modifier.offset(y = 1.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = Color(0xFFF1F1F1), thickness = 6.dp, modifier = Modifier.padding(horizontal = 0.dp))
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
                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = Color(0xFFF1F1F1), thickness = 6.dp, modifier = Modifier.padding(horizontal = 0.dp))
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
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
