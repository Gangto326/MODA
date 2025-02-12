package com.example.modapjt.components.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun VideoListComponent(navController: NavController, viewModel: SearchViewModel) {
    val searchData by viewModel.searchData.collectAsState()

    searchData?.videos?.let { videos ->
        if (videos.isNotEmpty()) {
            VideoList(
                navController = navController,
                videos = videos.map {
                    VideoItemData(
                        cardId = it.cardId,
                        videoUrl = it.thumbnailUrl?:"", // ✅ YouTube URL
                        title = it.title?:""
                    )
                }
            )
        }
    }
}
