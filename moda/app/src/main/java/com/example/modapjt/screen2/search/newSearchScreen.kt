package com.example.modapjt.screen2.search

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.modapjt.components.search.SearchScreenBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSearchScreen(
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SearchScreenBar(
                navController = navController,
                onSearchValueChange = { searchQuery = it }
            )
        }
    ) { paddingValues ->
        // 여기에 검색 결과 목록이나 추천 검색어 등을 표시할 수 있습니다
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 검색 결과나 추천 검색어 UI를 추가할 수 있습니다
        }
    }
}