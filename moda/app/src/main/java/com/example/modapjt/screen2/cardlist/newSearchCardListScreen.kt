package com.example.modapjt.screen2.cardlist

import AllTabCard
import BlogBig
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.cardlist.BlogSelectionItem
import com.example.modapjt.components.cardlist.NewsSelectionItem
import com.example.modapjt.components.cardlist.ScrollToTopButton
import com.example.modapjt.components.cardlist.VideoSelectionItem
import com.example.modapjt.components.cardtab.SwipableCardList
import com.example.modapjt.components.search.SearchListBar
import com.example.modapjt.domain.model.Card
import com.example.modapjt.domain.viewmodel.CardSelectionViewModel
import com.example.modapjt.domain.viewmodel.CardUiState
import com.example.modapjt.domain.viewmodel.CardViewModel
import com.example.modapjt.screen2.EmptyMessage
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
    val topScoreState by viewModel.topScoreState.collectAsState()  // TopScore 상태 추가
    val loadingMore by viewModel.loadingMore.collectAsState()

    // LazyListState to keep track of the scroll position
    val lazyListState = rememberLazyListState()

    // 다중 선택을 위한 ViewModel
    val selectionViewModel: CardSelectionViewModel<Card> = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CardSelectionViewModel<Card>() as T
            }
        }
    )

    // 다중 선택 상태 관리
    val isSelectionMode by selectionViewModel.isSelectionMode.collectAsState()
    val selectedCardIds by selectionViewModel.selectedCards.collectAsState()

    // 진동
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(query, selectedCategory, selectedSort) {
        if (query.isNotBlank()) {
            viewModel.resetPagination()
            val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
            viewModel.loadSearchCards(query, selectedCategory, sortDirection)
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
                        viewModel.loadSearchCards(searchQuery, selectedCategory, sortDirection)
                    }
                },
                navController = navController,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
//                .background(Color.White)
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
                                    viewModel.updateSelectedCategory(
                                        category
                                    )
                                },
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
                                        topScores = topScoreState.scores,
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
                                    item { EmptyMessage2("저장된 블로그가 없습니다") }
                                } else {
                                    items(
                                        items = state.blogs,
                                        key = { it.cardId }
                                    ) { card ->
                                        if (!isSelectionMode) {  // 일반 모드일 때
                                            SwipableCardList(
                                                cards = listOf(card),
                                                onDelete = { selectedCards ->
                                                    viewModel.deleteCard(selectedCards.map { it.cardId })
                                                },
                                                onCardDetail = { selectedCard ->
                                                    navController.navigate("cardDetail/${selectedCard.cardId}")
                                                },
                                                selectionViewModel = selectionViewModel,
                                                enableLongPress = false,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = Dp.Unspecified)
                                            ) { currentCard, isSelected ->
                                                BlogBig(
                                                    title = currentCard.title,
                                                    description = currentCard.thumbnailContent
                                                        ?: "",
                                                    imageUrl = currentCard.thumbnailUrl ?: "",
                                                    isMine = currentCard.isMine,
                                                    isSelected = isSelected,
                                                    keywords = currentCard.keywords
                                                    //                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }
                                                )
                                            }
                                        } else { // 선택 모드일 때
                                            BlogSelectionItem(
                                                title = card.title,
                                                description = card.thumbnailContent ?: "",
                                                imageUrl = card.thumbnailUrl ?: "",
                                                isMine = card.isMine,
                                                keywords = card.keywords,
                                                isSelected = selectedCardIds.contains(card.cardId),
                                                onClick = {
                                                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    selectionViewModel.toggleCardSelection(card)
                                                }
                                            )
                                        }
                                        // 각 블로그 사이에 구분선 추가
                                        Divider(
                                            color = MaterialTheme.colorScheme.onTertiary,
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(
                                                start = 16.dp,
                                                end = 16.dp
                                            ) // 양쪽에 패딩 추가
                                        )
                                    }
                                }
                            }

                            "동영상" -> {
                                if (state.videos.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 동영상이 없습니다") }
                                } else {
                                    items(
                                        items = state.videos,
                                        key = { it.cardId }
                                    ) { card ->
                                        // Check if this video is the first one in the viewport
                                        val isTopVideo =
                                            lazyListState.firstVisibleItemIndex == state.videos.indexOf(
                                                card
                                            )
                                        if (!isSelectionMode) {  // 일반 모드일 때
                                            SwipableCardList(
                                                cards = listOf(card),
                                                onDelete = { selectedCards ->
                                                    viewModel.deleteCard(selectedCards.map { it.cardId })
                                                },
                                                onCardDetail = { selectedCard ->
                                                    navController.navigate("cardDetail/${selectedCard.cardId}")
                                                },
                                                selectionViewModel = selectionViewModel,
                                                enableLongPress = false,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = Dp.Unspecified)
                                            ) { currentCard, isSelected ->
                                                // 딜레이 2초 후에 재생 시작
                                                LaunchedEffect(lazyListState.firstVisibleItemIndex) {
                                                    delay(2000) // 딜레이 추가
                                                }

                                                VideoBig(
                                                    videoId = currentCard.thumbnailUrl ?: "",
                                                    title = currentCard.title,
                                                    isMine = currentCard.isMine,
                                                    thumbnailContent = currentCard.thumbnailContent
                                                        ?: "",
                                                    isSelected = isSelected,
                                                    keywords = currentCard.keywords.take(3),
                                                    //                                                onClick = { navController.navigate("cardDetail/${card.cardId}") },
                                                    isTopVideo = isTopVideo
                                                )
                                            }
                                        } else {
                                            VideoSelectionItem(
                                                videoId = card.thumbnailUrl ?: "",
                                                title = card.title,
                                                isMine = card.isMine,
                                                bookMark = card.bookMark,
                                                keywords = card.keywords,
                                                isSelected = selectedCardIds.contains(card.cardId),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = Dp.Unspecified),
                                                thumbnailContent = card.thumbnailContent ?: "",
                                                onClick = {
                                                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    selectionViewModel.toggleCardSelection(card)
                                                }
                                            )
                                        }
                                        // 각 비디오 사이에 구분선 추가
                                        Divider(
                                            color = MaterialTheme.colorScheme.onTertiary,
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(
                                                start = 16.dp,
                                                end = 16.dp
                                            ) // 양쪽에 패딩 추가
                                        )
                                    }
                                }
                            }

                            "뉴스" -> {
                                if (state.news.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 뉴스가 없습니다") }
                                } else {
                                    items(
                                        items = state.news,
                                        key = { it.cardId }
                                    ) { card ->
                                        if (!isSelectionMode) {
                                            SwipableCardList(
                                                cards = listOf(card),
                                                onDelete = { selectedCards ->
                                                    viewModel.deleteCard(selectedCards.map { it.cardId })
                                                },
                                                onCardDetail = { selectedCard ->
                                                    navController.navigate("cardDetail/${selectedCard.cardId}")
                                                },
                                                selectionViewModel = selectionViewModel,
                                                enableLongPress = false,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = Dp.Unspecified)
                                            ) { currentCard, isSelected ->
                                                NewsBig(
                                                    title = currentCard.title,
                                                    keywords = currentCard.keywords,
                                                    imageUrl = currentCard.thumbnailUrl ?: "",
                                                    isMine = currentCard.isMine,
                                                    isSelected = isSelected,
                                                    description = currentCard.thumbnailContent ?: ""
                                                )
                                            }
                                        } else {
                                            NewsSelectionItem(
                                                title = card.title,
                                                description = card.thumbnailContent ?: "",
                                                imageUrl = card.thumbnailUrl ?: "",
                                                isMine = card.isMine,
                                                keywords = card.keywords,
                                                isSelected = selectedCardIds.contains(card.cardId),
                                                onClick = {
                                                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    selectionViewModel.toggleCardSelection(card)
                                                }
                                            )
                                        }
                                        // 각 뉴스 사이에 구분선 추가
                                        Divider(
                                            color = MaterialTheme.colorScheme.onTertiary,
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(
                                                start = 16.dp,
                                                end = 16.dp
                                            ) // 양쪽에 패딩 추가
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

                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        color = Color(0xFF1B1B1B),
                        shadowElevation = 0.dp,  // 그림자 제거
                        tonalElevation = 0.dp    // 톤 변화 제거
                    ) {
                        AnimatedVisibility(
                            visible = isSelectionMode,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${selectedCardIds.size}개 선택됨",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(
                                        onClick = {
                                            selectionViewModel.clearSelection()
                                            selectionViewModel.toggleSelectionMode(false)
                                        }
                                    ) {
                                        Text("취소")
                                    }
                                    Button(
                                        onClick = {
                                            val cardsToDelete = when (selectedCategory) {
                                                "블로그" -> state.blogs
                                                "동영상" -> state.videos
                                                "뉴스" -> state.news
                                                else -> emptyList()
                                            }.filter { card ->
                                                selectedCardIds.contains(card.cardId)
                                            }
                                            viewModel.deleteCard(cardsToDelete.map { it.cardId })
                                            selectionViewModel.toggleSelectionMode(false)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text("삭제")
                                    }
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
            // Scroll to Top 버튼 추가
            ScrollToTopButton(scrollState = lazyListState)
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
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}
