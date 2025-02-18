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
import com.example.modapjt.components.home.VideoListComponent
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun VideoSection(
    navController: NavController,
    homeKeywordViewModel: SearchViewModel,
    searchViewModel: SearchViewModel
) {
    val searchData by searchViewModel.searchData.collectAsState()
    val videos = searchData?.videos.orEmpty()
    val creator by homeKeywordViewModel.creator.collectAsState()

    // videos가 비어있어도 항상 표시
    HomeSmallTitle(
        title = if (creator.isNotEmpty()) "$creator 영상 어때요?" else "영상 어때요?",
        description = ""
    )

    VideoListComponent(navController, searchViewModel)

    Spacer(
        modifier = Modifier
            .height(20.dp)
            .fillMaxWidth()
    )
}