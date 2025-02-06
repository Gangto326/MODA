//package com.example.modapjt.components.card
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.modapjt.domain.model.Card
//import com.example.modapjt.components.video.YouTubePlayer
//import com.example.modapjt.utils.extractYouTubeVideoId
//
//@Composable
//fun CardDetail(card: Card) {
//    Column(modifier = Modifier.padding(16.dp)) {
//        // 카드 제목
//        Text(text = "제목: ${card.thumbnailContent ?: "제목 없음"}", style = MaterialTheme.typography.titleMedium)
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // 카드 내용
//        Text(text = "내용: ${card.thumbnailContent ?: "내용 없음"}")
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 유튜브 영상 처리 (카드 타입이 YOUTUBE이면)
//        if (card.type == "YOUTUBE") {
//            val videoId = extractYouTubeVideoId(card.thumbnailUrl)
//            if (videoId != null) {
//                YouTubePlayer(
//                    videoId = videoId,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                        .padding(top = 16.dp)
//                )
//            } else {
//                Text(text = "유튜브 링크 아님 -> 다른 방식 표현하기 ! ", color = MaterialTheme.colorScheme.error)
//            }
//        }
//    }
//}


//package com.example.modapjt.components.card
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.modapjt.domain.model.Card
//import com.example.modapjt.components.video.YouTubePlayer
//
//@Composable
//fun CardDetail(card: Card) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            // 제목과 타임스탬프 목록
//            card.thumbnailContent.forEach { section ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // 섹션 제목 (key)
//                    Text(
//                        text = section.key,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    // 타임스탬프 (value)
//                    Text(
//                        text = formatTimestamp(section.value),
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//                Divider(modifier = Modifier.padding(vertical = 4.dp))
//            }
//
//            // YouTube 비디오 플레이어
//            if (card.type == "YOUTUBE" && card.thumbnailUrl.isNotEmpty()) {
//                Spacer(modifier = Modifier.height(16.dp))
//                YouTubePlayer(
//                    videoId = extractYouTubeVideoId(card.thumbnailUrl),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                )
//            }
//        }
//    }
//}
//
//private fun formatTimestamp(value: Double): String {
//    val minutes = (value / 60).toInt()
//    val seconds = (value % 60).toInt()
//    return "%02d:%02d".format(minutes, seconds)
//}

// components/card/CardDetail.kt
package com.example.modapjt.components.cardlist

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.modapjt.data.dto.response.BlogPost
import com.example.modapjt.domain.model.CardDetail

//@Composable
//fun CardDetail(cardDetail: CardDetail) {  // Card 대신 CardDetail 타입 받도록 수정
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            // 제목
//            Text(
//                text = cardDetail.title,
//                style = MaterialTheme.typography.headlineMedium
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // content 섹션들 표시
//            cardDetail.content.forEach { section ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    // 섹션 제목 (key)
//                    Text(
//                        text = section.key,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    // 타임스탬프 (value)
//                    Text(
//                        text = formatTimestamp(section.value),
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//                Divider(modifier = Modifier.padding(vertical = 4.dp))
//            }
//
//            // YouTube 비디오 플레이어
////            if (cardDetail.type == "YOUTUBE") {
////                Spacer(modifier = Modifier.height(16.dp))
////                YouTubePlayer(
////                    videoId = extractYouTubeVideoId(cardDetail.title),  // or appropriate URL field
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .height(200.dp)
////                )
////            }
//            if (cardDetail.type == "YOUTUBE") {
//                val videoId = extractYouTubeVideoId(cardDetail.title)
//                if (videoId != null) {  // null이 아닐 때만 플레이어 표시
//                    Spacer(modifier = Modifier.height(8.dp))
//                    YouTubePlayer(
//                        videoId = videoId,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(120.dp)
//                    )
//                }
//            }
//        }
//    }
//}




//@Composable
//fun CardDetail(cardDetail: CardDetail) {
//    Log.d("CardDetail", "CardDetail Content: ${cardDetail.content}") // 디버깅 추가
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        // ✅ 제목 (굵게 + 큰 글씨)
//        Text(
//            text = cardDetail.title,
//            style = MaterialTheme.typography.headlineSmall,
//            fontWeight = FontWeight.Bold,
////            color = MaterialTheme.colorScheme.primary,
//            color = Color.Gray,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // ✅ LazyColumn()으로 블로그 게시글 목록 표시
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            items(cardDetail.content) { post ->
//                BlogPostCard(post)
//            }
//        }
//    }
//}
//
//@Composable
//fun BlogPostCard(post: BlogPost) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant
//        )
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            // ✅ 게시글 제목
//            Text(
//                text = post.title,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold,
//                color = MaterialTheme.colorScheme.onSurface
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//
//            // ✅ 게시글 내용
//            Text(
//                text = post.content,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//    }
//}

@Composable
fun CardDetail(cardDetail: CardDetail) {
    Log.d("CardDetail", "CardDetail Content: ${cardDetail.content}") // 디버깅 추가

    Column(modifier = Modifier
        .fillMaxSize() // ✅ 화면 전체 크기로 설정
        .padding(16.dp)) {

        // ✅ 제목 (굵게 + 큰 글씨)
        Text(
            text = cardDetail.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        // ✅ 전체 요약 내용을 하나의 Card()로 감싸기
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // ✅ 카드가 화면에서 스크롤 가능하도록 weight 사용
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // ✅ 스크롤 가능하도록 내부 padding 적용
            ) {
                items(cardDetail.content) { post ->
                    BlogPostCard(post)
                }
            }
        }
    }
}

@Composable
fun BlogPostCard(post: BlogPost) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) { // ✅ 개별 카드 제거하고 Column만 유지
        // ✅ 게시글 제목
        Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        // ✅ 게시글 내용
        Text(
            text = post.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}




private fun formatTimestamp(value: Double): String {
    val minutes = (value / 60).toInt()
    val seconds = (value % 60).toInt()
    return "%02d:%02d".format(minutes, seconds)
}


