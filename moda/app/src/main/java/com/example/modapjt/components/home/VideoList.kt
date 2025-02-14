package com.example.modapjt.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class VideoItemData(
    val cardId: String,
    val videoUrl: String,
    val title: String
)

@Composable
fun VideoList(navController: NavController, videos: List<VideoItemData>) {
    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✅ 현재 페이지의 영상 표시
        if (videos.isNotEmpty()) {
            val currentVideo = videos[currentIndex]

            VideoItem(
                videoUrl = currentVideo.videoUrl,
                title = currentVideo.title,
                cardId = currentVideo.cardId,
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ 페이지네이션 (이전/다음 버튼)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex--
                    }
                },
                enabled = currentIndex > 0
            ) {
                Text(text = "◀", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "${currentIndex + 1} / ${videos.size}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    if (currentIndex < videos.size - 1) {
                        currentIndex++
                    }
                },
                enabled = currentIndex < videos.size - 1
            ) {
                Text(text = "▶", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
