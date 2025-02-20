package com.example.modapjt.components.home.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.home.BottomThumbnailList
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.components.home.ThumbnailItem
import com.example.modapjt.data.dto.response.SearchItem
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun TodayContentSection(
    navController: NavController,
    todayContent: List<SearchItem>
) {
//    val searchData by searchViewModel.searchData.collectAsState()
//    val todays = searchData?.todays.orEmpty() // 오늘의 컨텐츠 리스트 가져오기

    if (todayContent.isNotEmpty()) {
        Divider(
            color = MaterialTheme.colorScheme.onTertiary,
            thickness = 6.dp,
            modifier = Modifier
//            .padding(horizontal = 0.dp)
//            .background(Color.Green) // ✅ Divider의 배경색 (위치를 시각적으로 확인)
                .fillMaxWidth()  // ✅ 가로 전체를 차지하도록 설정
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
//            .background(Color.Yellow) // ✅ 마지막 Spacer (Divider 아래)
                .fillMaxWidth()  // ✅ 가로 전체를 차지하도록 설정
        )

        HomeSmallTitle(
            title = "오늘의 컨텐츠",
            description = "| 오늘의 당신을 위한 컨텐츠"
        )

        // SearchItem을 ThumbnailItem으로 변환
        val thumbnailItems = todayContent.map { item ->
            ThumbnailItem(
                cardId = item.cardId,
                thumbnailUrl = item.thumbnailUrl ?: "",
                title = item.title ?: "",
                type = item.type ?: "",
                keywords = item.keywords ?: emptyList(),
                bookmark = item.bookmark ?: false
            )
        }

        BottomThumbnailList(thumbnails = thumbnailItems,
            onItemClick = { cardId ->
                navController.navigate("cardDetail/$cardId")
            })
        Spacer(modifier = Modifier.height(30.dp))
    }
}
