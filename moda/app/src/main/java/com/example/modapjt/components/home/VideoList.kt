package com.example.modapjt.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ 개선된 페이지네이션 UI
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ◀ 이전 버튼
            androidx.compose.material3.IconButton(
                onClick = { if (currentIndex > 0) currentIndex-- },
                enabled = currentIndex > 0
            ) {
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                    contentDescription = "이전 페이지",
                    modifier = Modifier.size(24.dp)
                )
            }

            // ✅ 페이지 번호 표시 박스
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${currentIndex + 1} / ${videos.size}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // ▶ 다음 버튼
            androidx.compose.material3.IconButton(
                onClick = { if (currentIndex < videos.size - 1) currentIndex++ },
                enabled = currentIndex < videos.size - 1
            ) {
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowForward,
                    contentDescription = "다음 페이지",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
