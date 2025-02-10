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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.modapjt.datastore.SearchKeywordDataStore
import kotlinx.coroutines.launch
@Composable
fun NewSearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // ✅ `derivedStateOf` 제거하고 `collectAsState()`만 사용
    val searchResults by searchViewModel.searchResults.collectAsState()

    LaunchedEffect(searchResults) {
        Log.d("UI_UPDATE_CHECK", "UI가 업데이트되었습니다: $searchResults")
    }
    LaunchedEffect(isSearchActive) {
        Log.d("SEARCH_SCREEN", "isSearchActive 상태 변경됨: $isSearchActive")
    }

    Scaffold(
        topBar = {
            SearchScreenBar(
                navController = navController,
                isSearchActive = isSearchActive,
                onSearchValueChange = {
                    searchQuery = it
                    searchViewModel.fetchAutoCompleteKeywords(it)
                },
                onFocusChanged = { isSearchActive = it },
                onBackPressed = {
                    if (isSearchActive) {
                        isSearchActive = false
                        searchQuery = ""
                        keyboardController?.hide()
                    } else {
                        navController.navigateUp()
                    }
                },
                context = context
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isSearchActive) { // 🔹 검색창이 비활성화되면 기본 화면 (최근 검색어 & 인기 검색어)
                item { SearchSubtitle(title = "최근 검색어", date = "", isDeletable = true, onDeleteAll = {/* 삭제 로직 */}) }
                item { SearchKeywordList(context) }
                item { SearchSubtitle(title = "인기 검색어", date = "25.02.02 기준") }
                item { KeywordRankList() }
            }

            if (isSearchActive && searchResults.isNotEmpty()) { // 🔹 검색 결과가 있을 때만 표시
                item {
                    Log.d("UI_CHECK", "SearchSuggestions 표시됨!")
                    SearchSuggestions(searchResults)
                }
            }
        }
    }
}

@Composable
fun SearchSuggestions(suggestions: List<String>) {
    Log.d("SearchSuggestions", "검색어 리스트 갱신됨: $suggestions")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "자동완성 검색어", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 400.dp), // ✅ 높이 강제 설정
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(suggestions.take(10), key = { it }) { suggestion ->
                Log.d("SearchSuggestions", "자동완성 검색어 아이템: $suggestion")
                Text(
                    text = suggestion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}
