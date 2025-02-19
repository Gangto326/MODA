////
////import android.os.Build
////import androidx.annotation.RequiresApi
////import androidx.compose.foundation.layout.Box
////import androidx.compose.foundation.layout.Column
////import androidx.compose.foundation.layout.fillMaxSize
////import androidx.compose.foundation.layout.fillMaxWidth
////import androidx.compose.foundation.layout.height
////import androidx.compose.foundation.layout.padding
////import androidx.compose.foundation.lazy.LazyColumn
////import androidx.compose.material3.Button
////import androidx.compose.material3.MaterialTheme
////import androidx.compose.material3.Text
////import androidx.compose.runtime.Composable
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.platform.LocalUriHandler
////import androidx.compose.ui.unit.dp
////import com.example.modapjt.components.video.YouTubePlayer
////import com.example.modapjt.domain.model.CardDetail
////import com.example.modapjt.utils.extractYouTubeVideoId
////import java.time.LocalDateTime
////import java.time.format.DateTimeFormatter
////
/////**
//// * 동영상 컨텐츠의 상세 내용을 보여주는 화면 컴포저블
//// * 유튜브 플레이어와 영상 관련 정보를 표시
//// *
//// * @param cardDetail 표시할 동영상 컨텐츠의 상세 정보를 담은 객체
//// */
////@RequiresApi(Build.VERSION_CODES.O)
////@Composable
////fun VideoDetailScreen(cardDetail: CardDetail) {
////
////    val uriHandler = LocalUriHandler.current
////    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
////    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)
////
////    Box(modifier = Modifier.fillMaxSize()) {
////        // 고정된 영상 플레이어
////        Column {
////            // 유튜브 URL에서 비디오 ID를 추출하여 플레이어 표시
////            val videoId = extractYouTubeVideoId(cardDetail.originalUrl)
////            if (!videoId.isNullOrEmpty()) {
////                YouTubePlayer(
////                    videoId = videoId,
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .height(200.dp)
////                )
////            }
////
////            // 스크롤 가능한 콘텐츠
////            LazyColumn(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(16.dp)
////            ) {
////                item {
////                    // 동영상 제목
////                    Text(
////                        text = cardDetail.title,
////                        style = MaterialTheme.typography.headlineMedium,
////                        modifier = Modifier.padding(bottom = 8.dp)
////                    )
////
////                    // 동영상 관련 키워드
////                    val limitedKeywords = cardDetail.keywords.take(3).joinToString(", ")
////                    Text(
////                        text = "키워드: $limitedKeywords",
////                        style = MaterialTheme.typography.bodyLarge,
////                        modifier = Modifier.padding(vertical = 4.dp)
////                    )
////
////                    // 동영상 설명
//////                    Text(
//////                        text = cardDetail.content,
//////                        style = MaterialTheme.typography.bodyLarge,
//////                        modifier = Modifier.padding(vertical = 8.dp)
//////                    )
////                    MarkdownText(
////                        markdown = cardDetail.content,
////                        modifier = Modifier.padding(vertical = 8.dp)
////                    )
////
////                    // 원본 동영상 링크 -> URL 버튼
////                    Button(
////                        onClick = { uriHandler.openUri(cardDetail.originalUrl) },
////                        modifier = Modifier.padding(vertical = 8.dp)
////                    ) {
////                        Text("원본 영상 보기")
////                    }
////
////
////                    // 동영상 타임라인 정보
////                    Text(
////                        text = "타임라인: ${cardDetail.subContents.joinToString(", ")}",
////                        style = MaterialTheme.typography.bodyLarge,
////                        modifier = Modifier.padding(vertical = 8.dp)
////                    )
////
////                    // 동영상 업로드 날짜
////                    Text(
////                        text = "생성 날짜: $formattedDate",
////                        style = MaterialTheme.typography.bodySmall,
////                        modifier = Modifier.padding(top = 8.dp)
////                    )
////                }
////            }
////        }
////    }
////}
//
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ExperimentalLayoutApi
//import androidx.compose.foundation.layout.FlowRow
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.AssistChip
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalUriHandler
//import androidx.compose.ui.unit.dp
//import com.example.modapjt.components.video.YouTubePlayerTime
//import com.example.modapjt.domain.model.CardDetail
//import com.example.modapjt.utils.extractYouTubeVideoId
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//
///**
// * 동영상 컨텐츠의 상세 내용을 보여주는 화면 컴포저블
// * 유튜브 플레이어와 영상 관련 정보를 표시
// *
// * @param cardDetail 표시할 동영상 컨텐츠의 상세 정보를 담은 객체
// */
//@OptIn(ExperimentalLayoutApi::class)
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun VideoDetailScreen(cardDetail: CardDetail) {
//    var player by remember { mutableStateOf<YouTubePlayer?>(null) }
//    val uriHandler = LocalUriHandler.current
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)
//
//    // 타임라인 데이터 추출 (3번째 요소부터 마지막까지)
//    val timelineData = cardDetail.subContents
//        .drop(2)  // 첫 두 개의 항목 제외
//        .mapNotNull { it.toFloatOrNull() }  // 숫자로 변환 가능한 것만 필터링
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column {
//            val videoId = extractYouTubeVideoId(cardDetail.originalUrl)
//            if (!videoId.isNullOrEmpty()) {
//                YouTubePlayerTime(
//                    videoId = videoId,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp),
//                    onPlayerReady = { youtubePlayer ->
//                        player = youtubePlayer
//                    }
//                )
//            }
//
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                item {
//                    // 동영상 제목
//                    Text(
//                        text = cardDetail.title,
//                        style = MaterialTheme.typography.headlineMedium,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//
//                    // 동영상 관련 키워드
//                    val limitedKeywords = cardDetail.keywords.take(3).joinToString(", ")
//                    Text(
//                        text = "키워드: $limitedKeywords",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    )
//
//                    // 동영상 설명
//                    MarkdownText(
//                        markdown = cardDetail.content,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//
//                    // 원본 동영상 링크 -> URL 버튼
//                    Button(
//                        onClick = { uriHandler.openUri(cardDetail.originalUrl) },
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    ) {
//                        Text("원본 영상 보기")
//                    }
//
//                    // 타임라인 섹션
//                    Text(
//                        text = "타임라인:",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//
//                    FlowRow(
//                        modifier = Modifier.padding(vertical = 4.dp),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        timelineData.forEach { timeStamp ->
//                            AssistChip(
//                                onClick = {
//                                    player?.seekTo(timeStamp.toFloat())
//                                },
//                                label = {
//                                    Text(formatTimeStamp(timeStamp))
//                                }
//                            )
//                        }
//                    }
//
//                    // 동영상 업로드 날짜
//                    Text(
//                        text = "생성 날짜: $formattedDate",
//                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
//// 시간을 포맷팅하는 함수
//private fun formatTimeStamp(seconds: Float): String {
//    val minutes = (seconds / 60).toInt()
//    val remainingSeconds = (seconds % 60).toInt()
//    return String.format("%02d:%02d", minutes, remainingSeconds)
//}

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.R
import com.example.modapjt.components.video.YouTubePlayerTime
import com.example.modapjt.domain.model.CardDetail
import com.example.modapjt.domain.viewmodel.SearchViewModel
import com.example.modapjt.utils.extractYouTubeVideoId
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// 헤더 정보를 저장하는 데이터 클래스
private data class HeaderInfo(
    val text: String,
    val lineIndex: Int,
    val timeStamp: Float,
    val offset: Int = 0  // 스크롤 오프셋 추가
)


@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VideoDetailScreen(cardDetail: CardDetail, navController: NavController) {
    val searchViewModel: SearchViewModel = viewModel()
    var player by remember { mutableStateOf<YouTubePlayer?>(null) }
    var showTimeline by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // 타임라인 데이터와 마크다운 헤더 매칭
    val timelineHeaders = remember(cardDetail.content) {
        val timeStamps = cardDetail.subContents
            .mapNotNull { it.toFloatOrNull() }
            .filter { it > 0 }

        // timeStamps가 비어있으면 빈 리스트 반환
        if (timeStamps.isEmpty()) {
            return@remember emptyList()
        }

        val contentLines = cardDetail.content.split("\n")
        var currentOffset = 0

        contentLines.mapIndexedNotNull { index, line ->
            if (line.trimStart().startsWith("#")) {
                val timeStamp = timeStamps.getOrNull(index % timeStamps.size) ?: return@mapIndexedNotNull null
                HeaderInfo(
                    text = line.trimStart('#').trim(),
                    lineIndex = index,
                    timeStamp = timeStamp,
                    offset = currentOffset
                )
            } else {
                currentOffset += line.length + 1  // +1 for newline
                null
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // 타임라인 오버레이 배경
        if (showTimeline) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null, // 클릭 효과 제거
                        interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                    ) { showTimeline = false }
            )
        }


        Column(modifier = Modifier.fillMaxSize()) {
            // YouTube 플레이어 (고정)
            val videoId = extractYouTubeVideoId(cardDetail.originalUrl)
            if (!videoId.isNullOrEmpty()) {
                YouTubePlayerTime(
                    videoId = videoId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    onPlayerReady = { youtubePlayer ->
                        player = youtubePlayer
                    }
                )
            }

            // 스크롤 가능한 콘텐츠
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
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
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                text = LocalDateTime.parse(cardDetail.createdAt)
                                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }

                        // 제목
                        Text(
                            text = cardDetail.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 채널 정보
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            cardDetail.subContents.getOrNull(1)?.let { imageUrl ->
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = imageUrl,
                                        error = painterResource(id = R.drawable.icon_round)
                                    ),
                                    contentDescription = "Channel Thumbnail",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Text(
                                text = cardDetail.keywords.firstOrNull() ?: "-",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        // 키워드와 공유/타임라인 버튼
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            FlowRow(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Start,
                                maxItemsInEachRow = 3
                            ) {
                                cardDetail.keywords.take(3).forEach { keyword ->
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f)),
                                        color = Color.Transparent,
                                        modifier = Modifier
                                            .padding(end = 8.dp, bottom = 8.dp)
                                            .clickable (
                                                indication = null, // 클릭 효과 제거
                                                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                                            ) {
                                                if (keyword.isNotBlank()) {
                                                    navController.navigate("newSearchCardListScreen/$keyword")
                                                }
                                            }
                                    ) {
                                        Text(
                                            text = keyword,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }

                                }
                            }

                            // 타임라인 토글 버튼 (헤더가 있을 때만 표시)
                            if (timelineHeaders.isNotEmpty()) {
                                IconButton(onClick = { showTimeline = !showTimeline }) {
                                    Icon(
                                        imageVector = Icons.Default.List,
                                        contentDescription = "Timeline",
                                        tint = if (showTimeline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }

                            IconButton(onClick = { uriHandler.openUri(cardDetail.originalUrl) }) {
                                Icon(
                                   painter = painterResource(R.drawable.ic_s_origin_link),
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }

                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )

                        // 본문 내용
                        MarkdownText(
                            markdown = cardDetail.content,
                            modifier = Modifier.padding(vertical = 16.dp),
                            keywords = cardDetail.keywords,
                            onKeywordClick = { keyword ->
                                searchViewModel.onKeywordClick(keyword)
                            }
                        )
                    }
                }
            }
        }

        // 타임라인 오버레이
        if (showTimeline) {
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(250.dp)
                    .padding(end = 8.dp)
                    .clickable(
                        enabled = false,
                        indication = null, // 클릭 효과 제거
                        interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                    ) {},
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "목차",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )

                    timelineHeaders.forEach { header ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable (
                                    indication = null, // 클릭 효과 제거
                                    interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                                ) {
                                    player?.seekTo(header.timeStamp)
                                    scope.launch {
                                        // 스크롤 위치 계산 개선
                                        scrollState.scrollToItem(0, header.offset)
                                    }
                                    showTimeline = false
                                }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatTimeStamp(header.timeStamp),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.width(48.dp)
                            )
                            Text(
                                text = header.text,
                                fontSize = 14.sp,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSecondary,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun formatTimeStamp(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val remainingSeconds = (seconds % 60).toInt()
    return String.format("%02d:%02d", minutes, remainingSeconds)
}