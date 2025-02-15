package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.components.video.YouTubePlayer



@Composable
fun VideoItem(videoUrl: String, title: String, cardId: String, navController: NavController) {
    // ✅ YouTube 영상 URL에서 Video ID 추출
    val videoId = getYouTubeVideoId(videoUrl)

    Column(
        modifier = Modifier
            .fillMaxWidth() // 부모 요소의 가로 크기 최대
            .padding(8.dp), // 좌우 여백 추가
        horizontalAlignment = Alignment.CenterHorizontally // 내부 요소들을 중앙 정렬
    ) {
        // ✅ YouTube 플레이어 박스
        Box(
            modifier = Modifier
                .fillMaxWidth() // ✅ 가로 폭을 최대한 채우기
                .aspectRatio(16 / 9f) // ✅ 16:9 비율 유지 (높이 자동 조정)
                .clip(RoundedCornerShape(8.dp)) // ✅ 모서리 둥글게
                .background(Color(0xFFC4C4C4)) // ✅ 유튜브 로딩 시 배경 색상 (회색)
        ) {
            YouTubePlayer(
                videoId = videoId, // ✅ YouTube 영상 ID
                modifier = Modifier
                    .fillMaxSize() // ✅ 부모(Box)에 맞게 채우기 (가로, 세로 자동 조절)
            )
        }


        // ✅ 영상과 제목 사이 여백 추가
        Spacer(modifier = Modifier.height(10.dp))

        // ✅ 제목 텍스트
        Text(
            text = title, // 영상 제목
            fontSize = 16.sp,
            lineHeight = 24.sp,
//            fontWeight = FontWeight(700),
            color = Color(0xFF2B2826),

            fontWeight = FontWeight.Bold, // 볼드체 적용
            maxLines = 2, // 최대 2줄까지만 표시
            overflow = TextOverflow.Ellipsis, // 넘칠 경우 "..." 처리
            textAlign = TextAlign.Left, // 왼쪽 정렬
            modifier = Modifier
                .fillMaxWidth() // 가로 최대 크기
                .clickable { // ✅ 클릭 시 상세 페이지로 이동
                    navController.navigate("cardDetail/$cardId")
                }
//                .padding(4.dp) // 내부 여백 추가
        )
    }
}

// ✅ YouTube URL에서 Video ID 추출하는 함수
private fun getYouTubeVideoId(url: String): String {
    return url.substringAfter("v=").substringBefore("&")
}
