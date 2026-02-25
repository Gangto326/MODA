package com.example.modapjt.components.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun ImageListComponent(navController: NavController, viewModel: SearchViewModel) {
    val searchData by viewModel.searchData.collectAsState()

    searchData?.images?.let { images ->
        if (images.isNotEmpty()) {
            ImageList(
                navController = navController,
                images = images.map {
                    ImageItem(
                        cardId = it.cardId,
                        thumbnailUrl = it.thumbnailUrl?: "",
                        bookmark = it.bookmark ?: false  // 🔥 북마크 필드 추가
                    )
                }
            )
        }
    }
}
