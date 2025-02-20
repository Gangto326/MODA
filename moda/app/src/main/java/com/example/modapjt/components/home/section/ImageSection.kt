package com.example.modapjt.components.home.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.components.home.ImageItem
import com.example.modapjt.components.home.ImageList
import com.example.modapjt.components.home.ImageListComponent
import com.example.modapjt.data.dto.response.SearchItem
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun ImageSection(
    images: List<SearchItem>,
    navController: NavController
) {
    if (images.isNotEmpty()) {
        Divider(
            color = MaterialTheme.colorScheme.onTertiary,
            thickness = 6.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
        )

        HomeSmallTitle(
            title = "이미지 보고가세요",
            description = "| 오늘의 추천 이미지 모음"
        )

        // ImageListComponent 대신 ImageList 직접 사용
        ImageList(
            navController = navController,
            images = images.map {
                ImageItem(
                    cardId = it.cardId,
                    thumbnailUrl = it.thumbnailUrl ?: "",
                    bookmark = it.bookmark ?: false
                )
            }
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}