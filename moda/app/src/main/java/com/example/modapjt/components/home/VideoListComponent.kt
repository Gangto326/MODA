package com.example.modapjt.components.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun VideoListComponent(navController: NavController, viewModel: SearchViewModel) {
    val searchData by viewModel.searchData.collectAsState()

    val videosList = searchData?.videos?.map {
        VideoItemData(
            cardId = it.cardId,
            videoUrl = it.thumbnailUrl ?: "",
            title = it.title ?: ""
        )
    } ?: emptyList()

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
}