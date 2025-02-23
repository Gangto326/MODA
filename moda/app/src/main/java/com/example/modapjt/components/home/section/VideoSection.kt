package com.example.modapjt.components.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.components.home.VideoItemData
import com.example.modapjt.components.home.VideoList
import com.example.modapjt.components.home.VideoListComponent
import com.example.modapjt.data.dto.response.SearchItem
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun VideoSection(
    videos: List<SearchItem>,
    creator: String,
    navController: NavController
) {
    // 상태를 직접 받아서 사용
    HomeSmallTitle(
        title = if (creator.isNotEmpty()) "$creator 영상 어때요?" else "영상 어때요?",
        description = ""
    )

    // VideoListComponent 대신 VideoList를 직접 사용
    val videosList = videos.map {
        VideoItemData(
            cardId = it.cardId,
            videoUrl = it.thumbnailUrl ?: "",
            title = it.title ?: ""
        )
    }

    val finalVideos = if (videosList.isEmpty()) {
        listOf(
            VideoItemData(
                cardId = "default",
                videoUrl = "PRpeWTudJls",
                title = "나만의 포탈 사이트 '모다'"
            )
        )
    } else {
        videosList
    }

    VideoList(
        navController = navController,
        videos = finalVideos
    )

    Spacer(
        modifier = Modifier
            .height(20.dp)
            .fillMaxWidth()
    )
}