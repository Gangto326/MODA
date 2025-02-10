package com.example.modapjt.components.search

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.modapjt.datastore.SearchKeywordDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SearchKeywordList(context: Context) {
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
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f)) // 🔹 오른쪽 정렬

            Text(
                text = "전체 삭제",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 12.sp),
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            SearchKeywordDataStore.saveKeywords(context, emptyList()) // 🔹 DataStore 초기화
                            keywords = emptyList() // 🔹 UI 업데이트
                        }
                    }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // 🔹 최근 검색어와 리스트 사이 간격 추가

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(keywords) { keyword ->
                SearchKeywordItem(
                    keyword = keyword,
                    onDelete = {
                        scope.launch {
                            val updatedKeywords = keywords.filter { it != keyword }
                            SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                            keywords = updatedKeywords // 🔹 UI 업데이트
                        }
                    }
                )
            }
        }
    }
}
