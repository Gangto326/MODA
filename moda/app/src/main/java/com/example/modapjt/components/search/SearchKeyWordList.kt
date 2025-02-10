package com.example.modapjt.components.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchKeywordList() {
    var keywords by remember { mutableStateOf(listOf("침착맨", "싸피", "중간발표", "프론트", "개발", "밥심")) }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow( // 🔽 기존 Row → LazyRow로 변경 (가로 스크롤 지원)
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // 🔽 양쪽 패딩 추가
            horizontalArrangement = Arrangement.spacedBy(8.dp) // 🔽 키워드 간격 유지
        ) {
            items(keywords) { keyword ->
                SearchKeywordItem(
                    keyword = keyword,
                    onDelete = { keywords = keywords.filter { it != keyword } } // 🔽 삭제 기능 유지
                )
            }
        }
    }
}
