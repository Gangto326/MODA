package com.example.modapjt.components.card

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.modapjt.R
import com.example.modapjt.components.video.YouTubePlayer
import com.example.modapjt.domain.model.Card
import com.example.modapjt.utils.extractYouTubeVideoId


//@Composable
//fun CardItem(
//    card: Card,
//    navController: NavController
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable {
//                // 카드 클릭 시 해당 카드의 상세 화면으로 이동
//                navController.navigate("card/${card.cardId}")
//            }
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            // 첫 번째 섹션만 미리보기로 표시
//            // 카드 제목 추가 (상단)
//            Text(
//                text = card.title, // 카드 제목
//                style = MaterialTheme.typography.titleMedium, // 제목 스타일 적용
//                color = MaterialTheme.colorScheme.primary, // 색상 적용
//                modifier = Modifier.padding(bottom = 8.dp) // 여백 추가
//            )
//            card.thumbnailContent.firstOrNull()?.let { section ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = section.key,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = Color.Blue
//                    )
//                    Text(
////                        text = formatTimestamp(section.value),
//                        text = (section.value), // 썸네일 본문
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//            }
//
//            // 섹션 개수 표시
//            if (card.thumbnailContent.size > 1) {
//                Text(
//                    text = "+ ${card.thumbnailContent.size - 1}개의 타임스탬프",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.secondary
//                )
//            }
//
//            if (card.type == "YOUTUBE" && card.thumbnailUrl.isNotEmpty()) {
//                val videoId = extractYouTubeVideoId(card.thumbnailUrl)
//                Log.d("DEBUG", "Extracted Video ID: $videoId")
//                if (videoId != null) {  // null이 아닐 때만 플레이어 표시
//                    Spacer(modifier = Modifier.height(8.dp))
//                    YouTubePlayer(
//                        videoId = videoId,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                    )
//                } else {
//                    Log.d("DEBUG", "YouTube Video ID is null, not rendering YouTubePlayer")
//                }
//            }
//        }
//    }
//}

//@Composable
//fun CardItem(
//    card: Card,
//    navController: NavController
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable {
//                navController.navigate("card/${card.cardId}")
//            }
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            // 카드 제목
//            Text(
//                text = card.title,
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            when (card.type) {
//                "YOUTUBE" -> {
//                    // 유튜브 카드: 제목 + 유튜브 영상
//                    val videoId = extractYouTubeVideoId(card.thumbnailUrl)
//                    if (videoId != null) {
//                        Spacer(modifier = Modifier.height(8.dp))
//                        YouTubePlayer(
//                            videoId = videoId,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(200.dp)  // 기존 120dp → 200dp로 크기 조정
//                        )
//                    }
//                }
//
//                "BLOG" -> {
//                    // 블로그 카드: 제목 + 블로그 썸네일 + 썸네일 본문(section.value)
////                    if (card.thumbnailUrl.isNotEmpty()) {
////                        Spacer(modifier = Modifier.height(8.dp))
////                        AsyncImage(
////                            model = card.thumbnailUrl,
////                            contentDescription = "블로그 썸네일",
////                            modifier = Modifier
////                                .fillMaxWidth()
////                                .height(200.dp)  // 기존과 동일하게 200dp 설정
////                        )
////                    }
//
//                    // 블로그 썸네일 본문(section.value) 표시
//                    card.thumbnailContent.firstOrNull()?.let { section ->
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            text = section.value,
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
@Composable
fun CardItem(
    card: Card,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                navController.navigate("card/${card.cardId}")
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 카드 제목 + 아이콘
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                // 카드 유형에 따라 아이콘 변경
                val iconResId = when (card.type) {
                    "BLOG" -> R.drawable.blog  // 블로그 아이콘
                    "NEWS" -> R.drawable.news  // 뉴스 아이콘
                    "YOUTUBE" -> R.drawable.youtube  // 유튜브 아이콘
                    else -> null
                }

                // 아이콘이 있으면 표시
                iconResId?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = "${card.type} 아이콘",
                        modifier = Modifier
                            .size(50.dp) // 작은 아이콘 크기
                            .padding(end = 8.dp)
                    )
                }

                // 카드 제목
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f) // 제목이 남은 공간 차지하도록 설정
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (card.type) {
                "YOUTUBE" -> {
                    val videoId = extractYouTubeVideoId(card.thumbnailUrl)
                    if (videoId != null) {
                        YouTubePlayer(
                            videoId = videoId,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }

                "BLOG" -> {
//                    if (card.thumbnailUrl.isNotEmpty()) {
//                        AsyncImage(
//                            model = card.thumbnailUrl,
//                            contentDescription = "블로그 썸네일",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(200.dp)
//                        )
//                    }
                    card.thumbnailContent.firstOrNull()?.let { section ->
                        Text(
                            text = section.value,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}





//private fun formatTimestamp(value: Double): String {
//    val minutes = (value / 60).toInt()
//    val seconds = (value % 60).toInt()
//    return "%02d:%02d".format(minutes, seconds)
//}


//package com.example.modapjt.components.card
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.modapjt.domain.model.Card
//
//@Composable
//fun CardItem(card: Card, navController: NavController) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable { navController.navigate("card/${card.cardId}") } // 클릭 시 카드 상세 페이지 이동
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(
//                text = card.thumbnailUrl ?: "썸네일 없음",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = card.thumbnailContent ?: "내용 없음",
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//    }
//}





//
//@Composable
//fun CardItem(card: Card, navController: NavController) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable { navController.navigate("card/${card.cardId}") }
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "제목: ${card.title}")  // 카드 제목 출력
//            Text(text = "내용: ${card.content}")  // 카드 내용 출력
////            Text(
////                text = card.title,
////                style = MaterialTheme.typography.titleMedium,
////                fontWeight = FontWeight.Bold
////            )
//
//            if (card.isView) { // isView가 true일 때 임베딩된 콘텐츠 표시
//                Spacer(modifier = Modifier.height(8.dp))
////                // 임베딩된 내용 예시 (이미지, 텍스트 등)
////                if (card.embedType == "image") {
////                    Image(
////                        painter = painterResource(id = card.embedContentId),
////                        contentDescription = null,
////                        modifier = Modifier.fillMaxWidth(),
////                        contentScale = ContentScale.Crop
////                    )
////                } else if (card.embedType == "video") {
////                    // 비디오 임베딩 예시
////                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.Gray)) {
////                        // 여기 비디오 플레이어 임베딩을 구현할 수 있습니다.
////                    }
////                } else {
////                    // 텍스트 콘텐츠 임베딩
////                    Text(
////                        text = card.embedContentText,
////                        style = MaterialTheme.typography.bodyMedium
////                    )
////                }
//
//                Text(
//                    text = "isView가 true인 경우",
//                )
//            }
//        }
//    }
//}
//




//@Composable
//fun CardItem(card: Card, navController: NavController) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable { navController.navigate("card/${card.cardId}") } // 클릭 시 상세 페이지 이동
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            // 카드 제목
//            Text(
//                text = "제목 :: ${card.title}",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            // 카드 내용
//            Text(
//                text = "내용: ${card.content}",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(top = 4.dp)
//            )
//
//            // isView가 true일 때 콘텐츠 표시 (유튜브 or 블로그)
//            if (card.isView) {
//                Spacer(modifier = Modifier.height(8.dp))
//
//                when (card.typeId) {
//                    1 -> { // typeId == 1 (유튜브)
//                        val videoId = extractYouTubeVideoId(card.urlHash)
//                        if (!videoId.isNullOrEmpty()) {
//                            YouTubePlayer(videoId = videoId, modifier = Modifier.height(200.dp))
//                        }
//                    }
//                    2 -> { // typeId == 2 (블로그)
//                        Text(
//                            text = "아직 테스트",
//                            style = MaterialTheme.typography.bodyMedium,
//                            modifier = Modifier
//                                .background(Color.LightGray)
//                                .padding(8.dp)
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//    }
//}