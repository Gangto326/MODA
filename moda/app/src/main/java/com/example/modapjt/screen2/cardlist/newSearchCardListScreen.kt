package com.example.modapjt.screen2

import AllTabCard
import BlogBig
import ImageBig
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.cardtab.SwipableCardList
import com.example.modapjt.domain.viewmodel.CardUiState
import com.example.modapjt.domain.viewmodel.CardViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun newSearchCardListScreen(
    navController: NavController,
    currentRoute: String,
    initialQuery: String,
    viewModel: CardViewModel = viewModel()
) {
    var query by remember { mutableStateOf(initialQuery) }
    var selectedCategory by remember { mutableStateOf("전체") }
    var selectedSort by remember { mutableStateOf("최신순") }
    val uiState by viewModel.uiState.collectAsState()
    val loadingMore by viewModel.loadingMore.collectAsState()
    val userId = "user"

    LaunchedEffect(query, selectedCategory, selectedSort) {
        if (query.isNotBlank()) {
            viewModel.resetPagination()
            val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
            viewModel.loadSearchCards(userId, query, selectedCategory, sortDirection)
        }
    }

    Scaffold(
        topBar = { SearchBarComponent(query = query, navController = navController) },
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
                                        imageCards = state.images,
                                        blogCards = state.blogs,
                                        videoCards = state.videos,
                                        newsCards = state.news,
                                        onImageMoreClick = { selectedCategory = "이미지" },
                                        onBlogMoreClick = { selectedCategory = "블로그" },
                                        onVideoMoreClick = { selectedCategory = "동영상" },
                                        onNewsMoreClick = { selectedCategory = "뉴스" }
                                    )
                                }
                            }
//                            "이미지" -> {
//                                if (state.images.isEmpty() && !loadingMore) {
//                                    item { EmptyMessage2("검색된 이미지가 없습니다") }
//                                } else {
//                                    items(state.images) { card ->
//                                        SwipableCardList(
//                                            cards = listOf(card),
//                                            onDelete = { viewModel.deleteCard(listOf(card.cardId)) }
//                                        ) {
//                                            ImageBig(
//                                                imageUrl = card.thumbnailUrl ?: "",
//                                                isMine = card.isMine,
//                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }
//                                            )
//                                        }
//
//                                        if (card == state.images.lastOrNull() && !loadingMore) {
//                                            LaunchedEffect(Unit) {
//                                                val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
//                                                viewModel.loadSearchCards(userId, query, selectedCategory, sortDirection, true)
//                                            }
//                                        }
//                                    }
//                                }
//                            }
                            "이미지" -> {
                                if (state.images.isEmpty() && !loadingMore) {
                                    item { EmptyMessage("저장된 이미지가 없습니다") }
                                } else {
                                    item {
                                        FlowRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween, // ✅ 아이템 간격 균등 배치
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            state.images.forEachIndexed { index, card ->
                                                ImageBig(
                                                    imageUrl = card.thumbnailUrl ?: "",
                                                    isMine = card.isMine,
                                                    modifier = Modifier
                                                        .fillMaxWidth(0.5f) // ✅ 한 줄에 2개씩 배치
                                                        .aspectRatio(1f), // ✅ 정사각형 유지
                                                    onClick = { navController.navigate("cardDetail/${card.cardId}") }
                                                )
                                            }

                                            // ✅ 마지막 아이템이 홀수라면 빈 Spacer 추가
                                            if (state.images.size % 2 != 0) {
                                                Box(modifier = Modifier.fillMaxWidth(0.5f)) // ✅ 빈 공간 확보
                                            }
                                        }
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
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }
                                            )
                                        }

                                        if (card == state.blogs.lastOrNull() && !loadingMore) {
                                            LaunchedEffect(Unit) {
                                                val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
                                                viewModel.loadSearchCards(userId, query, selectedCategory, sortDirection, true)
                                            }
                                        }
                                    }
                                }
                            }
                            "동영상" -> {
                                if (state.videos.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 동영상이 없습니다") }
                                } else {
                                    items(state.videos) { card ->
                                        SwipableCardList(
                                            cards = listOf(card),
                                            onDelete = { viewModel.deleteCard(listOf(card.cardId)) }
                                        ) {
                                            VideoBig(
                                                videoId = card.thumbnailUrl ?: "",
                                                title = card.title,
                                                isMine = card.isMine,
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }
                                            )
                                        }

                                        if (card == state.videos.lastOrNull() && !loadingMore) {
                                            LaunchedEffect(Unit) {
                                                val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
                                                viewModel.loadSearchCards(userId, query, selectedCategory, sortDirection, true)
                                            }
                                        }
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

                                        if (card == state.news.lastOrNull() && !loadingMore) {
                                            LaunchedEffect(Unit) {
                                                val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
                                                viewModel.loadSearchCards(userId, query, selectedCategory, sortDirection, true)
                                            }
                                        }
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




// SearchBarComponent 추가
@Composable
fun SearchBarComponent(
    query: String,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("search") }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = query,
                onValueChange = {},
                modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate("search") },
                readOnly = true,
                placeholder = { Text("검색어를 입력하세요") },
                singleLine = true
            )

            IconButton(
                onClick = { navController.navigate("search") },
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// EmptyMessage2 추가
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