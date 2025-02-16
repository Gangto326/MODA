package com.example.modapjt.screen2.cardlist

import AllTabCard
import BlogBig
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.example.modapjt.components.cardtab.SwipableCardList
import com.example.modapjt.components.search.SearchListBar
import com.example.modapjt.domain.viewmodel.CardUiState
import com.example.modapjt.domain.viewmodel.CardViewModel
import com.example.modapjt.screen2.MasonryImageGrid
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun newSearchCardListScreen(
    navController: NavController,
    currentRoute: String,
    initialQuery: String,
    viewModel: CardViewModel = viewModel()
) {
    var query by remember { mutableStateOf(initialQuery) }
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    var selectedSort by remember { mutableStateOf("최신순") }
    val uiState by viewModel.uiState.collectAsState()
    val loadingMore by viewModel.loadingMore.collectAsState()
    val userId = "user"

    // LazyListState to keep track of the scroll position
    val lazyListState = rememberLazyListState()

    LaunchedEffect(query, selectedCategory, selectedSort) {
        if (query.isNotBlank()) {
            viewModel.resetPagination()
            val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
            viewModel.loadSearchCards(userId, query, selectedCategory, sortDirection)
        }
    }

    Scaffold(
        topBar = {
            SearchListBar(
                query = query,
                onQueryChange = { newQuery -> query = newQuery },
                onSearch = { searchQuery ->
                    if (searchQuery.isNotBlank()) {
                        query = searchQuery
                        viewModel.resetPagination()
                        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
                        viewModel.loadSearchCards(userId, searchQuery, selectedCategory, sortDirection)
                    }
                },
                navController = navController
            )
        },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when (val state = uiState) {
                is CardUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CardUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            TypeSelectBar(
                                selectedCategory = selectedCategory,
                                selectedSort = selectedSort,
                                onCategorySelected = { category -> viewModel.updateSelectedCategory(category) },
                                onSortSelected = { selectedSort = it }
                            )
                        }

                        when (selectedCategory) {
                            "전체" -> {
                                item {
                                    AllTabCard(
                                        navController = navController,
                                        imageCards = state.images,
                                        blogCards = state.blogs,
                                        videoCards = state.videos,
                                        newsCards = state.news,
                                        onImageMoreClick = { viewModel.updateSelectedCategory("이미지") },
                                        onBlogMoreClick = { viewModel.updateSelectedCategory("블로그") },
                                        onVideoMoreClick = { viewModel.updateSelectedCategory("동영상") },
                                        onNewsMoreClick = { viewModel.updateSelectedCategory("뉴스") }
                                    )
                                }
                            }

                            "이미지" -> {
                                if (state.images.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 이미지가 없습니다") }
                                } else {
                                    item {
                                        MasonryImageGrid(
                                            imageUrls = state.images.map { it.thumbnailUrl ?: "" },
                                            isMineList = state.images.map { it.isMine },
                                            cardIdList = state.images.map { it.cardId },
                                            onImageClick = { cardId -> navController.navigate("cardDetail/$cardId") }
                                        )
                                    }
                                }
                            }

                            "블로그" -> {
                                if (state.blogs.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 블로그가 없습니다") }
                                } else {
                                    items(state.blogs) { card ->
                                        SwipableCardList(
                                            cards = listOf(card),
                                            onDelete = { viewModel.deleteCard(listOf(card.cardId)) }
                                        ) {
                                            BlogBig(
                                                title = card.title,
                                                description = card.thumbnailContent ?: "",
                                                imageUrl = card.thumbnailUrl ?: "",
                                                isMine = card.isMine,
                                                keywords = card.keywords,
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }
                                            )
                                        }
                                        // 각 블로그 사이에 구분선 추가
                                        Divider(
                                            color = Color(0xFFF1F1F1),
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(start = 16.dp, end = 16.dp) // 양쪽에 패딩 추가
                                        )
                                    }
                                }
                            }

                            "동영상" -> {
                                if (state.videos.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 동영상이 없습니다") }
                                } else {
                                    items(state.videos) { card ->
                                        // Check if this video is the first one in the viewport
                                        val isTopVideo = lazyListState.firstVisibleItemIndex == state.videos.indexOf(card)

                                        SwipableCardList(
                                            cards = listOf(card),
                                            onDelete = { viewModel.deleteCard(listOf(card.cardId)) }
                                        ) {
                                            // 딜레이 2초 후에 재생 시작
                                            LaunchedEffect(lazyListState.firstVisibleItemIndex) {
                                                delay(2000) // 딜레이 추가
                                            }

                                            VideoBig(
                                                videoId = card.thumbnailUrl ?: "",
                                                title = card.title,
                                                isMine = card.isMine,
                                                thumbnailContent = card.thumbnailContent ?: "",
                                                keywords = card.keywords.take(3),
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") },
                                                isTopVideo = isTopVideo // 화면 상단에 있는 동영상만 자동 재생
                                            )
                                        }
                                        // 각 비디오 사이에 구분선 추가
                                        Divider(
                                            color = Color(0xFFF1F1F1),
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(start = 16.dp, end = 16.dp) // 양쪽에 패딩 추가
                                        )
                                    }
                                }
                            }

                            "뉴스" -> {
                                if (state.news.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 뉴스가 없습니다") }
                                } else {
                                    items(state.news) { card ->
                                        SwipableCardList(
                                            cards = listOf(card),
                                            onDelete = { viewModel.deleteCard(listOf(card.cardId)) }
                                        ) {
                                            NewsBig(
                                                title = card.title,
                                                keywords = card.keywords,
                                                imageUrl = card.thumbnailUrl ?: "",
                                                isMine = card.isMine,
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }
                                            )
                                        }
                                        // 각 뉴스 사이에 구분선 추가
                                        Divider(
                                            color = Color(0xFFF1F1F1),
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(start = 16.dp, end = 16.dp) // 양쪽에 패딩 추가
                                        )
                                    }
                                }
                            }
                        }

                        if (loadingMore && selectedCategory != "전체") {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }

                is CardUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMessage2(message: String) {
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
