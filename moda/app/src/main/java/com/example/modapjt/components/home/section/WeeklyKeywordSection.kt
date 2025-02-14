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
import com.example.modapjt.components.home.FirstKeywordList
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.components.home.WeeklyKeywordList
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun WeeklyKeywordSection(
    homeKeywordViewModel: SearchViewModel,
    userId: String,
    navController: NavController,
    searchViewModel: SearchViewModel
) {
    val topKeywords by homeKeywordViewModel.topKeywords.collectAsState()
    val keywordSearchData by searchViewModel.keywordSearchData.collectAsState()

    val shouldShowTitle = topKeywords.isNotEmpty() || keywordSearchData.isNotEmpty()

    if (shouldShowTitle) {
        HomeSmallTitle(
            title = "이번주 주요 키워드",
            description = "| 이번주 사용자가 많이 저장한 키워드"
        )
    }

    if (topKeywords.isNotEmpty()) {
        WeeklyKeywordList(homeKeywordViewModel, userId)
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (keywordSearchData.isNotEmpty()) {
        FirstKeywordList(navController, searchViewModel)
        Spacer(modifier = Modifier.height(16.dp))
    }

    Divider(color = Color(0xFFDCDCDC), thickness = 4.dp, modifier = Modifier.padding(horizontal = 0.dp))
    Spacer(modifier = Modifier.height(16.dp))
}
