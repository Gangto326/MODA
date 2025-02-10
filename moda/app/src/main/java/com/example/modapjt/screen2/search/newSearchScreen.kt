package com.example.modapjt.screen2.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.search.KeywordRankList
import com.example.modapjt.components.search.SearchKeywordList
import com.example.modapjt.components.search.SearchScreenBar
import com.example.modapjt.components.search.SearchSubtitle
import com.example.modapjt.domain.viewmodel.SearchViewModel
import android.util.Log

@Composable
fun NewSearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val searchResults by searchViewModel.searchResults.collectAsState()

    Scaffold(
        topBar = {
            SearchScreenBar(
                navController = navController,
                isSearchActive = isSearchActive,
                onSearchValueChange = {
                    searchQuery = it
                    Log.d("SearchScreen", "입력된 검색어: $it") // 🔹 검색어 입력 로그
                    searchViewModel.fetchAutoCompleteKeywords(it)
                },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isSearchActive) {
                item { SearchSubtitle(title = "최근 검색어", date = "전체 삭제", isDeletable = true) }
                item { SearchKeywordList() }
                item { SearchSubtitle(title = "인기 검색어", date = "25.02.02 기준") }
                item { KeywordRankList() }
            } else {
                Log.d("SearchScreen", "자동완성 검색어 개수: ${searchResults.size}") // 🔹 검색 결과 개수 로그
                item { SearchSuggestions(searchResults) }
            }
        }
    }
}

@Composable
fun SearchSuggestions(suggestions: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "자동완성 검색어", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(suggestions.take(10)) { suggestion ->
                    Log.d("SearchSuggestions", "자동완성 검색어 아이템: $suggestion") // 🔹 검색어 리스트 로그
                    Text(
                        text = suggestion,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
