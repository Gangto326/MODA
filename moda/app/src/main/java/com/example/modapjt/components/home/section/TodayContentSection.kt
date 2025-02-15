package com.example.modapjt.components.home.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.home.BottomThumbnailList
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun TodayContentSection(
    navController: NavController,
    searchViewModel: SearchViewModel
) {
    val searchData by searchViewModel.searchData.collectAsState()
    val todays = searchData?.todays.orEmpty() // 오늘의 컨텐츠 리스트 가져오기

    if (todays.isNotEmpty()) {
        HomeSmallTitle(
            title = "오늘의 컨텐츠",
            description = " | 해당 컨텐츠에 대한 설명"
        )

        BottomThumbnailList(navController, searchViewModel)
        Spacer(modifier = Modifier.height(30.dp))
    }
}
