package com.example.modapjt.screen2

import AllTabCard
import BlogBig
import ImageBig
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.CategoryHeaderBar
import com.example.modapjt.components.cardtab.SwipableCardList
import com.example.modapjt.domain.viewmodel.CardUiState
import com.example.modapjt.domain.viewmodel.CardViewModel

@Composable
fun newBookMarkCardListScreen(
    navController: NavController,
    currentRoute: String,
    viewModel: CardViewModel = viewModel()
) {
    var selectedCategory by remember { mutableStateOf("전체") }
    var selectedSort by remember { mutableStateOf("최신순") }
    val uiState by viewModel.uiState.collectAsState()
    val userId = "user" // 실제 사용자 ID로 교체 필요

    LaunchedEffect(selectedCategory, selectedSort) {
        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
        println("[newBookMarkCardListScreen] 선택된 탭: $selectedCategory, 정렬: $selectedSort ($sortDirection)")
        viewModel.loadBookmarkedCards(userId, selectedCategory, sortDirection)
    }

    Scaffold(
        topBar = { CategoryHeaderBar(categoryName = "즐겨찾기", navController = navController) },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when (uiState) {
                is CardUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CardUiState.Success -> {
                    val data = uiState as CardUiState.Success

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            TypeSelectBar(
                                selectedCategory = selectedCategory,
                                selectedSort = selectedSort,
                                onCategorySelected = { selectedCategory = it },
                                onSortSelected = { selectedSort = it }
                            )
                        }

                        when (selectedCategory) {
                            "전체" -> {
                                item {
                                    AllTabCard(
                                        imageCards = data.images,
                                        blogCards = data.blogs,
                                        videoCards = data.videos,
                                        newsCards = data.news,
                                        onImageMoreClick = { selectedCategory = "이미지" },
                                        onBlogMoreClick = { selectedCategory = "블로그" },
                                        onVideoMoreClick = { selectedCategory = "동영상" },
                                        onNewsMoreClick = { selectedCategory = "뉴스" }
                                    )
                                }
                            }
                            else -> {
                                if (data.images.isEmpty() && data.blogs.isEmpty() && data.videos.isEmpty() && data.news.isEmpty()) {
                                    item { EmptyMessage3("저장된 즐겨찾기가 없습니다.") }
                                } else {
                                    items(data.images + data.blogs + data.videos + data.news) { card ->
                                        SwipableCardList(
                                            cards = listOf(card),
                                            onDelete = { viewModel.deleteCard(listOf(card.cardId)) } // ✅ onDelete 추가
                                        ) {
                                            when (card.typeId) {
                                                4 -> ImageBig(imageUrl = card.thumbnailUrl ?: "", isMine = card.isMine, onClick = { navController.navigate("cardDetail/${card.cardId}") })
                                                2 -> BlogBig(title = card.title, description = card.thumbnailContent ?: "", imageUrl = card.thumbnailUrl ?: "", isMine = card.isMine, onClick = { navController.navigate("cardDetail/${card.cardId}") })
                                                3 -> NewsBig(title = card.title, keywords = card.keywords, imageUrl = card.thumbnailUrl ?: "", isMine = card.isMine, onClick = { navController.navigate("cardDetail/${card.cardId}") })
                                                1 -> VideoBig(videoId = card.thumbnailUrl ?: "", title = card.title, isMine = card.isMine, onClick = { navController.navigate("cardDetail/${card.cardId}") })
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
                is CardUiState.Error -> {
                    Text(
                        text = (uiState as CardUiState.Error).message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMessage3(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
