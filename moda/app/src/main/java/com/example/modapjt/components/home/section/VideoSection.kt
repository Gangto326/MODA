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
    val videos = searchData?.videos.orEmpty() // 비디오 리스트 가져오기
    val creator by homeKeywordViewModel.creator.collectAsState()

    if (videos.isNotEmpty()) {
        HomeSmallTitle(
            title = if (creator.isNotEmpty()) "$creator 영상 어때요?" else "영상 어때요?",
            description = ""
        )

        VideoListComponent(navController, searchViewModel)

        Spacer(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth() // ✅ 가로 전체 영역을 차지하도록 설정
//            .background(Color.Red) // ✅ 첫 번째 Spacer 배경색 (빨강)
        )



    }
}