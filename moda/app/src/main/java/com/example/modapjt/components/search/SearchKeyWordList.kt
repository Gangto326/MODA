package com.example.modapjt.components.search

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.modapjt.datastore.SearchKeywordDataStore
@Composable
fun SearchKeywordList(context: Context) {
    val scope = rememberCoroutineScope()
    var keywords by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        keywords = SearchKeywordDataStore.getKeywords(context).first()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
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

            Spacer(modifier = Modifier.width(8.dp)) // 여백 추가

            Text(
                text = "전체 삭제",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red),
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
    }
}
