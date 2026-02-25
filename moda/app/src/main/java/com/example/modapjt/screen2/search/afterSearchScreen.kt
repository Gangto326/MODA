package com.example.modapjt.screen2.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.search.KeywordRankList
import com.example.modapjt.components.search.SearchKeywordList
import com.example.modapjt.components.search.SearchScreenBar
import com.example.modapjt.domain.viewmodel.SearchViewModel


@Composable
fun oldSearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel(),
    currentRoute: String,
    searchQuery: String = "" // 초기 검색어 파라미터 추가
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var isSearchActive by remember { mutableStateOf(false) }
    val searchResults by searchViewModel.searchResults.collectAsState()

    // searchQuery 파라미터를 초기값으로 사용
    var currentSearchQuery by remember { mutableStateOf(searchQuery) }

    // ✨ 이전 검색 결과를 저장하는 변수 추가
    var lastValidSearchResults by remember { mutableStateOf<List<String>>(emptyList()) }

    var searchText by remember { mutableStateOf(searchQuery) }

    // ✅ 화면이 열리자마자 키보드 활성화 // 초기 검색어가 있을 경우 자동완성 실행
    LaunchedEffect(Unit) {
        if (searchQuery.isNotEmpty()) {
            searchViewModel.fetchAutoCompleteKeywords(searchQuery)
            isSearchActive = true
        }
        keyboardController?.show()
    }

    LaunchedEffect(isSearchActive) {
        Log.d("SEARCH_SCREEN", "isSearchActive 상태 변경됨: $isSearchActive")
    }

    // ✨ 검색 결과 모니터링
    LaunchedEffect(searchResults) {
        if (searchResults.isNotEmpty()) {
            lastValidSearchResults = searchResults
        }
    }

    Scaffold(
        topBar = {
            SearchScreenBar(
                navController = navController,
                initialValue = currentSearchQuery, // 현재 검색어 전달
                isSearchActive = isSearchActive,
                onSearchValueChange = {
                    currentSearchQuery = it
                    searchViewModel.fetchAutoCompleteKeywords(it)
                },
                onFocusChanged = { isSearchActive = it },
                onSearchSubmit = { query ->
                    if (query.isNotBlank()) {
                        navController.navigate("newSearchCardListScreen/$query")
                    }
                },
                onBackPressed = {
                    navController.navigateUp()
                },
                context = context,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures {
                        keyboardController?.hide()
                    }
                }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                if (currentSearchQuery.isEmpty()) {
                    item { SearchKeywordList(context, navController = navController) }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item { KeywordRankList(viewModel = viewModel(), navController = navController) }
                }

                if (currentSearchQuery.isNotEmpty()) {
                    item {
                        val displayResults = if (searchResults.isEmpty()) lastValidSearchResults else searchResults
                        SearchSuggestions(displayResults, onSearchSubmit = { query ->
                            if (query.isNotBlank()) {
                                navController.navigate("newSearchCardListScreen/$query")
                            }
                        })
                    }
                }
            }
        }
    }
}

