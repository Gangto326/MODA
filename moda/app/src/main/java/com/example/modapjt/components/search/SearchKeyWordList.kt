package com.example.modapjt.components.search

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.modapjt.datastore.SearchKeywordDataStore

@Composable
fun SearchKeywordList(context: Context) {
    val scope = rememberCoroutineScope()
    var keywords by remember { mutableStateOf(listOf<String>()) }

    // 🔹 DataStore에서 최근 검색어 불러오기
    LaunchedEffect(Unit) {
        keywords = SearchKeywordDataStore.getKeywords(context).first()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(keywords) { keyword ->
                SearchKeywordItem(
                    keyword = keyword,
                    onDelete = {
                        scope.launch {
                            // 🔹 DataStore에서 해당 검색어 삭제
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
