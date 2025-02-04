package com.example.modapjt.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modapjt.domain.model.Card
import com.example.modapjt.components.video.YouTubePlayer
import com.example.modapjt.utils.extractYouTubeVideoId

@Composable
fun CardDetail(card: Card) {
    Column(modifier = Modifier.padding(16.dp)) {
        // 카드 제목
        Text(text = "제목: ${card.thumbnailContent ?: "제목 없음"}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // 카드 내용
        Text(text = "내용: ${card.thumbnailContent ?: "내용 없음"}")
        Spacer(modifier = Modifier.height(16.dp))

        // 유튜브 영상 처리 (카드 타입이 YOUTUBE이면)
        if (card.type == "YOUTUBE") {
            val videoId = extractYouTubeVideoId(card.thumbnailUrl)
            if (videoId != null) {
                YouTubePlayer(
                    videoId = videoId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp)
                )
            } else {
                Text(text = "유튜브 링크 아님 -> 다른 방식 표현하기 ! ", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
