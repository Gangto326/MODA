//package com.example.modapjt.screen2
//
//import AllTabCard
//import BlogBig
//import ImageBig
//import NewsBig
//import TypeSelectBar
//import VideoBig
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.modapjt.components.bar.BottomBarComponent
//import com.example.modapjt.components.bar.CategoryHeaderBar
//import com.example.modapjt.components.cardtab.SwipableCardList
//import com.example.modapjt.domain.viewmodel.CardUiState
//import com.example.modapjt.domain.viewmodel.CardViewModel
//
//@Composable
//fun newBookMarkCardListScreen(
//    navController: NavController,
//    currentRoute: String,
//    viewModel: CardViewModel = viewModel()
//) {
//    var selectedCategory by remember { mutableStateOf("전체") }
//    var selectedSort by remember { mutableStateOf("최신순") }
//    val uiState by viewModel.uiState.collectAsState()
//    val loadingMore by viewModel.loadingMore.collectAsState()
//    val userId = "user"
//
//    LaunchedEffect(selectedCategory, selectedSort) {
//        viewModel.resetPagination()
//        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
//
//        if (selectedCategory == "전체") {
//            // ✅ 전체 탭일 경우 모든 데이터를 가져오는 API 호출
//            viewModel.loadAllBookmarkedCards(userId)
//        } else {
//            // ✅ 특정 카테고리(이미지, 블로그, 뉴스, 동영상)만 가져옴
//            val typeId = when (selectedCategory) {
//                "이미지" -> 4
//                "블로그" -> 2
//                "뉴스" -> 3
//                "동영상" -> 1
//                else -> 0
//            }
//            viewModel.loadBookmarkedCards(userId, typeId, sortDirection)
//        }
//    }
//
//
//    Scaffold(
//        topBar = { CategoryHeaderBar(categoryName = "즐겨찾기", navController = navController) },
//        bottomBar = { BottomBarComponent(navController, currentRoute) }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(Color.White)
//        ) {
//            when (val state = uiState) {
//                is CardUiState.Loading -> {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//
//                is CardUiState.Success -> {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        item {
//                            TypeSelectBar(
//                                selectedCategory = selectedCategory,
//                                selectedSort = selectedSort,
//                                onCategorySelected = { selectedCategory = it },
//                                onSortSelected = { selectedSort = it }
//                            )
//                        }
//
//                        when (selectedCategory) {
//                            "전체" -> {
//                                item {
//                                    AllTabCard(
//                                        imageCards = state.images,
//                                        blogCards = state.blogs,
//                                        videoCards = state.videos,
//                                        newsCards = state.news,
//                                        onImageMoreClick = { selectedCategory = "이미지" },
//                                        onBlogMoreClick = { selectedCategory = "블로그" },
//                                        onVideoMoreClick = { selectedCategory = "동영상" },
//                                        onNewsMoreClick = { selectedCategory = "뉴스" }
//                                    )
//                                }
//                            }
//
//                            else -> {
//                                val cards = when (selectedCategory) {
//                                    "이미지" -> state.images
//                                    "블로그" -> state.blogs
//                                    "동영상" -> state.videos
//                                    "뉴스" -> state.news
//                                    else -> emptyList()
//                                }
//
//                                if (cards.isEmpty() && !loadingMore) {
//                                    item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
//                                } else {
//                                    items(cards) { card ->
//                                        SwipableCardList(
//                                            cards = listOf(card),
//                                            onDelete = { viewModel.deleteCard(listOf(card.cardId)) }
//                                        ) {
//                                            when (card.typeId) {
//                                                4 -> ImageBig(
//                                                    imageUrl = card.thumbnailUrl ?: "",
//                                                    isMine = card.isMine,
//                                                    onClick = { navController.navigate("cardDetail/${card.cardId}") }
//                                                )
//                                                2 -> BlogBig(
//                                                    title = card.title,
//                                                    description = card.thumbnailContent ?: "",
//                                                    imageUrl = card.thumbnailUrl ?: "",
//                                                    isMine = card.isMine,
//                                                    onClick = { navController.navigate("cardDetail/${card.cardId}") }
//                                                )
//                                                3 -> NewsBig(
//                                                    title = card.title,
//                                                    keywords = card.keywords,
//                                                    imageUrl = card.thumbnailUrl ?: "",
//                                                    isMine = card.isMine,
//                                                    onClick = { navController.navigate("cardDetail/${card.cardId}") }
//                                                )
//                                                1 -> VideoBig(
//                                                    videoId = card.thumbnailUrl ?: "",
//                                                    title = card.title,
//                                                    isMine = card.isMine,
//                                                    onClick = { navController.navigate("cardDetail/${card.cardId}") }
//                                                )
//                                                else -> {} // 기타 타입에 대한 처리
//                                            }
//                                        }
//
//                                        if (card == cards.lastOrNull() && !loadingMore) {
//                                            LaunchedEffect(Unit) {
//                                                val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
//                                                val typeId = when (selectedCategory) {
//                                                    "이미지" -> 4
//                                                    "블로그" -> 2
//                                                    "뉴스" -> 3
//                                                    "동영상" -> 1
//                                                    else -> 0
//                                                }
//                                                viewModel.loadBookmarkedCards(userId, typeId, sortDirection, true)
//                                            }
//                                        }
//                                    }
//
//                                    if (loadingMore) {
//                                        item {
//                                            Box(
//                                                modifier = Modifier
//                                                    .fillMaxWidth()
//                                                    .padding(16.dp),
//                                                contentAlignment = Alignment.Center
//                                            ) {
//                                                CircularProgressIndicator()
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//                is CardUiState.Error -> {
//                    Text(
//                        text = state.message,
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EmptyMessage3(message: String) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = message,
//            color = Color.Gray,
//            textAlign = TextAlign.Center
//        )
//    }
//}

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.delay


@Composable
fun newBookMarkCardListScreen(
    navController: NavController,
    currentRoute: String,
    viewModel: CardViewModel = viewModel()
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    var selectedSort by remember { mutableStateOf("최신순") }
    val uiState by viewModel.uiState.collectAsState()
    val loadingMore by viewModel.loadingMore.collectAsState()
    val userId = "user"

    // LazyListState to keep track of the scroll position
    val lazyListState = rememberLazyListState()

    LaunchedEffect(selectedCategory, selectedSort) {
        viewModel.resetPagination()
        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"

        if (selectedCategory == "전체") {
            viewModel.loadAllBookmarkedCards(userId)
        } else {
            val typeId = when (selectedCategory) {
                "이미지" -> 4
                "블로그" -> 2
                "뉴스" -> 3
                "동영상" -> 1
                else -> 0
            }
            viewModel.loadBookmarkedCards(userId, typeId, sortDirection)
        }
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
                                onCategorySelected = { category ->
                                    viewModel.updateSelectedCategory(category)
                                },
                                onSortSelected = { selectedSort = it }
                            )
                        }

                        when (selectedCategory) {
                            "전체" -> {
                                item {
                                    AllTabCard(
                                        navController = navController, // NavController 전달
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
                                    item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
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

                            // 동영상 카테고리 처리
                            "동영상" -> {
                                if (state.videos.isEmpty() && !loadingMore) {
                                    item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
                                } else {
                                    items(state.videos) { card ->
                                        // Determine if this video is the first visible item
                                        val isTopVideo = lazyListState.firstVisibleItemIndex == state.videos.indexOf(card)

                                        SwipableCardList(
                                            cards = listOf(card),
                                            onDelete = { viewModel.deleteCard(listOf(card.cardId)) }
                                        ) {
                                            // Wait for 2 seconds before auto-playing the first video
                                            LaunchedEffect(lazyListState.firstVisibleItemIndex) {
                                                // Add delay before auto-playing the video
                                                delay(2000) // 2 seconds delay
                                            }

                                            VideoBig(
                                                videoId = card.thumbnailUrl ?: "",
                                                title = card.title,
                                                isMine = card.isMine,
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") },
                                                isTopVideo = isTopVideo // Only autoplay the video that is on top of the screen
                                            )
                                        }
                                    }
                                }
                            }

                            "블로그" -> {
                                if (state.blogs.isEmpty() && !loadingMore) {
                                    item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
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
                                    }
                                }
                            }

                            "뉴스" -> {
                                if (state.news.isEmpty() && !loadingMore) {
                                    item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
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
