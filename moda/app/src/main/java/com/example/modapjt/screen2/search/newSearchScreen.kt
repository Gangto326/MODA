package com.example.modapjt.screen2.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.search.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSearchScreen(
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val lastUpdatedDate = "25.02.02 기준"

    Scaffold(
        topBar = {
            SearchScreenBar(
                navController = navController,
                isSearchActive = isSearchActive,
                onSearchValueChange = { searchQuery = it },
                onFocusChanged = { isSearchActive = it },
                onBackPressed = {
                    if (isSearchActive) {
                        isSearchActive = false
                        searchQuery = ""
                    } else {
                        navController.navigateUp()
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn( // 🔽 스크롤 가능하도록 변경
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp) // 🔽 위아래 간격 추가
        ) {
            if (!isSearchActive) {
                item {
                    SearchSubtitle(
                        title = "최근 검색어",
                        date = "전체 삭제",
                        isDeletable = true
                    ) {
                        println("최근 검색어 삭제됨!")
                    }
                }

                item { SearchKeywordList() } // 🔽 가로 스크롤 키워드 리스트 추가

                item { SearchSubtitle(title = "즐겨찾기", date = "(최근에 많이 저장한 검색어 자동 즐겨찾기)") }
                item { SearchSubtitle(title = "많이 저장한 키워드", date = lastUpdatedDate) }
                item { KeywordRankList() } // 🔽 많이 저장한 키워드 리스트 추가

                item { SearchSubtitle(title = "최근 저장한 키워드", date = lastUpdatedDate) }
                item { KeywordRankList() } // 🔽 최근 저장한 키워드 리스트 추가
                item {SearchSubtitle(title = "최근에 본 컨텐츠", date = "")}

            } else {
                item { SearchSuggestions(searchQuery) }
            }
        }
    }
}





// 🔽 연관 검색어 UI (현재는 더미 데이터)
@Composable
fun SearchSuggestions(query: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "'$query'와 관련된 검색어", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        repeat(5) { index ->
            Text(text = "$query 관련 검색어 $index", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
