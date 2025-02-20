package com.example.modapjt.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun WeeklyKeywordList(
    keywords: List<String>,
    selectedKeyword: String?,
    onKeywordSelected: (String) -> Unit
) {
//    val topKeywords by homeKeywordViewModel.topKeywords.collectAsState()
//    val selectedKeyword by homeKeywordViewModel.selectedKeyword.collectAsState()
//
//    LaunchedEffect(topKeywords) {
//        if (topKeywords.isNotEmpty() && selectedKeyword == null) {
//            homeKeywordViewModel.updateKeywordAndFetchData(topKeywords.first())
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            keywords.forEach { keyword ->
                val isSelected = keyword == selectedKeyword
                Box(
                    modifier = Modifier
                        .let {
                            // 선택된 키워드에 배경색 추가
                            if (isSelected) {
                                it.background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50))
                            } else {
                                // 선택되지 않은 키워드는 테두리만 추가
                                it.border(1.dp, MaterialTheme.colorScheme.onSecondary, shape = RoundedCornerShape(50))
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 2.dp)
                        .clickable(
                            indication = null, // 클릭 효과 제거
                            interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                        ) {
                            onKeywordSelected(keyword)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = keyword,
                        fontSize = 12.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary // 선택된 키워드 색상은 흰색, 나머지는 검은색
                    )
                }
            }
        }
    }
}
