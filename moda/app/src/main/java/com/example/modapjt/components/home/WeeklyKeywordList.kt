package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun WeeklyKeywordList(
    homeKeywordViewModel: SearchViewModel = viewModel(),
    userId: String
) {
    val topKeywords by homeKeywordViewModel.topKeywords.collectAsState()
    val selectedKeyword by homeKeywordViewModel.selectedKeyword.collectAsState()

    LaunchedEffect(topKeywords) {
        if (topKeywords.isNotEmpty() && selectedKeyword == null) {
            homeKeywordViewModel.updateKeywordAndFetchData(topKeywords.first(), userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topKeywords.forEach { keyword ->
                val isSelected = keyword == selectedKeyword
                Box(
                    modifier = Modifier
                        .background(if (isSelected) Color(0xFFFF7043) else Color(0xFFFFCC80), shape = RoundedCornerShape(50))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .clickable {
                            homeKeywordViewModel.updateKeywordAndFetchData(keyword, userId)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = keyword,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
