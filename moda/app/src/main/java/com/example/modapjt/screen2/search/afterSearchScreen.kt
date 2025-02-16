package com.example.modapjt.screen2.search

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val searchResults by searchViewModel.searchResults.collectAsState()


    // ✨ 이전 검색 결과를 저장하는 변수 추가
    var lastValidSearchResults by remember { mutableStateOf<List<String>>(emptyList()) }

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
                isSearchActive = isSearchActive,
                onSearchValueChange = {
                    searchQuery = it
                    searchViewModel.fetchAutoCompleteKeywords(it)
                },
                onFocusChanged = { isSearchActive = it },
                onSearchSubmit = { query -> // ✅ 검색 버튼 클릭 시 동작
                    if (query.isNotBlank()) {
                        navController.navigate("newSearchCardListScreen/$query") // ✅ 검색어와 함께 이동
                    }
                },
                onBackPressed = {
                    // 단순히 이전 화면으로 돌아가기
                    navController.navigateUp()
                },
                context = context
            )
        }, bottomBar = { BottomBarComponent(navController, currentRoute) }
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
                    item { SearchKeywordList(context, navController = navController) }
                    // ✨ 검색어 리스트 사이 간격 20dp 추가
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item { KeywordRankList(viewModel = viewModel(), navController = navController) }
                }

                if (searchQuery.isNotEmpty()) {
                    // 🔹 검색어 입력 시 자동완성 검색어 표시
                    item {
                        // ✨ 빈 검색 결과일 경우 마지막 유효한 결과 사용
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
