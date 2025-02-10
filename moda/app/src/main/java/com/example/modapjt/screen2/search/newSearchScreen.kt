package com.example.modapjt.screen2.search

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.search.KeywordRankList
import com.example.modapjt.components.search.SearchKeywordList
import com.example.modapjt.components.search.SearchScreenBar
import com.example.modapjt.components.search.SearchSubtitle
import com.example.modapjt.domain.viewmodel.SearchViewModel

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
    val searchResults by searchViewModel.searchResults.collectAsState()

    // ✅ 화면이 열리자마자 키보드 활성화
    LaunchedEffect(Unit) {
        keyboardController?.show()
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
                        navController.navigate("home")
                    }
                },
                context = context
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures {
                        keyboardController?.hide() // ✅ 헤더 외 다른 부분 터치 시 키보드 숨김
                    }
                }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (searchQuery.isEmpty()) {
                    // 🔹 검색어가 없을 때 최근 검색어 & 인기 검색어 표시
                    item { SearchKeywordList(context) }
                    item { SearchSubtitle(title = "인기 검색어", date = "25.02.02 기준") }
                    item { KeywordRankList() }
                }

                if (searchQuery.isNotEmpty()) {
                    // 🔹 검색어 입력 시 자동완성 검색어 표시
                    item {
                        Log.d("UI_CHECK", "SearchSuggestions 표시됨!")
                        SearchSuggestions(searchResults)
                    }
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
