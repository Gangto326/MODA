package com.example.modapjt.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.R

data class VideoItemData(
    val cardId: String,
    val videoUrl: String,
    val title: String
)



@Composable
fun VideoList(navController: NavController, videos: List<VideoItemData>) {
    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (videos.isNotEmpty()) {
            val currentVideo = videos[currentIndex]

            Box(modifier = Modifier.fillMaxWidth()) {
                VideoItem(
                    videoUrl = currentVideo.videoUrl,
                    title = currentVideo.title,
                    cardId = currentVideo.cardId,
                    navController = navController
                )
            }
        }


        // ✅ 영상 제목과 페이지네이션 UI 사이 간격 추가
            Spacer(modifier = Modifier.height(16.dp)) // 여기서 간격 조정 가능!



        // 페이지네이션 UI
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ◀ 이전 버튼 (ic_left 아이콘 적용)
            Image(
                painter = painterResource(id = R.drawable.ic_left),
                contentDescription = "이전 페이지",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(40.dp) // 버튼 크기 조정
                    .clickable { if (currentIndex > 0) currentIndex-- }
                    .padding(4.dp)
            )

            // 페이지 번호 표시 박스
            Box(
                modifier = Modifier.padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${currentIndex + 1}",
                        fontSize = 16.sp,
                        color = Color(0xFF665F5B), // 현재 페이지 숫자 색상
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Text(
                        text = " / ",
                        fontSize = 16.sp,
                        color = Color(0xFFBAADA4) // 구분자 색상
                    )
                    Text(
                        text = "${videos.size}",
                        fontSize = 16.sp,
                        color = Color(0xFFBAADA4) // 전체 페이지 숫자 색상
                    )
                }
            }

            // ▶ 다음 버튼 (ic_right 아이콘 적용)
            Image(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = "다음 페이지",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(40.dp) // 버튼 크기 조정
                    .clickable { if (currentIndex < videos.size - 1) currentIndex++ }
                    .padding(4.dp)
            )
        }
    }
}
