package com.example.modapjt.components.home.section

import androidx.compose.foundation.layout.Spacer
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
import com.example.modapjt.components.home.ImageListComponent
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun ImageSection(
    navController: NavController,
    searchViewModel: SearchViewModel
) {
    val searchData by searchViewModel.searchData.collectAsState()
    val images = searchData?.images.orEmpty() // 이미지 리스트 가져오기

    if (images.isNotEmpty()) {
        HomeSmallTitle(
            title = "이미지 보고가세요",
            description = "| 해당 컨텐츠들에 대한 설명"
        )

        ImageListComponent(navController, searchViewModel)
        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color(0xFFDCDCDC), thickness = 4.dp, modifier = Modifier.padding(horizontal = 0.dp))
        Spacer(modifier = Modifier.height(16.dp))
    }
}
