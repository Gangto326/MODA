package com.example.modapjt.components.search

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.datastore.SearchKeywordDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchKeywordList(context: Context, navController: NavController) {
    val scope = rememberCoroutineScope()
    var keywords by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        keywords = SearchKeywordDataStore.getKeywords(context).first()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        // 🔹 "최근 검색어" 왼쪽 위에 크게 표시, "전체 삭제" 오른쪽 끝에 작게 회색으로 배치
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "최근 검색어",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f)) // 🔹 오른쪽 정렬

            Text(
                text = "전체 삭제",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 12.sp),
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .clickable(
                        indication = null, // 클릭 효과 제거
                        interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                    ) {
                        scope.launch {
                            SearchKeywordDataStore.saveKeywords(context, emptyList()) // 🔹 DataStore 초기화
                            keywords = emptyList() // 🔹 UI 업데이트
                        }
                    }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // 🔹 최근 검색어와 리스트 사이 간격 추가

        // 공식 Jetpack Compose FlowRow 사용
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = Int.MAX_VALUE // 자동으로 줄바꿈
        ) {
            keywords.forEach { keyword ->
                SearchKeywordItem(
                    keyword = keyword,
                    onSearchSubmit = { query ->
                        if (query.isNotBlank()) {
                            navController.navigate("newSearchCardListScreen/$query")
                        }
                    },
                    onDelete = {
                        scope.launch {
                            val updatedKeywords = keywords.filter { it != keyword }
                            SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                            keywords = updatedKeywords
                        }
                    }
                )
            }
        }
    }
}
