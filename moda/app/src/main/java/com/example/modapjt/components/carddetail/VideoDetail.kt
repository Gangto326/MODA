


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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modapjt.components.video.YouTubePlayerTime
import com.example.modapjt.domain.model.CardDetail
import com.example.modapjt.utils.extractYouTubeVideoId
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VideoDetailScreen(cardDetail: CardDetail) {
    // 상태 및 기본 설정
    var player by remember { mutableStateOf<YouTubePlayer?>(null) }
    val uriHandler = LocalUriHandler.current
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)

    // 타임라인 데이터 추출 및 디버깅
    println("원본 subContents: ${cardDetail.subContents}")

    val timelineData = cardDetail.subContents
        .onEach { println("처리 전 데이터: $it") }
        .mapNotNull {
            val result = it.toFloatOrNull()
            println("변환 결과: $result")
            result
        }
        .filter { it > 0 }
        .also { println("최종 타임라인 데이터: $it") }

    // 메인 레이아웃
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // YouTube 플레이어 섹션
        val videoId = extractYouTubeVideoId(cardDetail.originalUrl)
        if (!videoId.isNullOrEmpty()) {
            YouTubePlayerTime(
                videoId = videoId,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onPlayerReady = { youtubePlayer ->
                    player = youtubePlayer
                }
            )
        }

        // 콘텐츠 레이아웃
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            // 왼쪽: 비디오 정보 섹션
            LazyColumn(
                modifier = Modifier
                    .weight(2.5f)  // 비율 수정
                    .fillMaxHeight()
                    .padding(end = 8.dp)
            ) {
                item {
                    // 제목
                    Text(
                        text = cardDetail.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // 채널명
                    Text(
                        text = cardDetail.keywords.firstOrNull() ?: "-", // 리스트가 비어있으면 빈 문자열 반환
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 동영상 관련 키워드
                    
                    // 키워드

                    val limitedKeywords = cardDetail.keywords.take(3).joinToString(", ")
                    Text(
                        text = "키워드: $limitedKeywords",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // 비디오 설명
                    MarkdownText(
                        markdown = cardDetail.content,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // 원본 비디오 링크
                    Button(
                        onClick = { uriHandler.openUri(cardDetail.originalUrl) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text("원본 영상 보기", fontSize = 12.sp)
                    }

                    // 날짜 정보
                    Text(
                        text = "생성 날짜: $formattedDate",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // 오른쪽: 타임라인 섹션
            Column(
                modifier = Modifier
                    .weight(0.7f)  // 비율 수정
                    .fillMaxHeight()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "타임라인",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // 타임라인 버튼 리스트 (스크롤 가능)
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(timelineData.size) { index ->
                        val timeStamp = timelineData[index]
                        val formattedTime = formatTimeStamp(timeStamp)

                        AssistChip(
                            onClick = {
                                println("클릭된 시간: $formattedTime")
                                player?.seekTo(timeStamp)
                            },
                            label = {
                                Text(
                                    text = formattedTime,
                                    fontSize = 10.sp,
                                    color = Color.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Visible,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 2.dp)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// 시간 포맷팅 유틸리티 함수
private fun formatTimeStamp(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val remainingSeconds = (seconds % 60).toInt()
    return String.format("%02d:%02d", minutes, remainingSeconds)
}