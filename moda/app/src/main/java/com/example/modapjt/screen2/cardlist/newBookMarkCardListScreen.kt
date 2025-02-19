package com.example.modapjt.screen2

import AllTabCard
import BlogBig
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
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
import com.example.modapjt.components.bar.CategoryHeaderBar
import com.example.modapjt.components.bar.TitleHeaderBar
import com.example.modapjt.components.cardlist.BlogSelectionItem
import com.example.modapjt.components.cardlist.NewsSelectionItem
import com.example.modapjt.components.cardlist.ScrollToTopButton
import com.example.modapjt.components.cardlist.VideoSelectionItem
import com.example.modapjt.components.cardtab.SwipableCardList
import com.example.modapjt.domain.model.Card
import com.example.modapjt.domain.viewmodel.CardSelectionViewModel
import com.example.modapjt.domain.viewmodel.CardUiState
import com.example.modapjt.domain.viewmodel.CardViewModel
import kotlinx.coroutines.delay

@ExperimentalFoundationApi
@Composable
fun newBookMarkCardListScreen(
    navController: NavController,
    currentRoute: String,
    viewModel: CardViewModel = viewModel()
) {
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

    val displayCount = remember { mutableStateOf(0) }

    // 선택된 카드 목록 업데이트
    LaunchedEffect(selectedCardIds, selectedCategory, uiState) {
        if (uiState is CardUiState.Success) {
            viewModel.updateCardsToDelete(selectedCategory,
                uiState as CardUiState.Success, selectedCardIds)
        }
    }

    LaunchedEffect(selectedCardIds.size) {
        if (selectedCardIds.size > 0) {
            displayCount.value = selectedCardIds.size
        }
    }

    LaunchedEffect(selectedCategory, selectedSort) {
        viewModel.resetPagination()
        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"

        if (selectedCategory == "전체") {
            viewModel.loadAllBookmarkedCards()
        } else {
            val typeId = when (selectedCategory) {
                "이미지" -> 4
                "블로그" -> 2
                "뉴스" -> 3
                "동영상" -> 1
                else -> 0
            }
            viewModel.loadBookmarkedCards(typeId, sortDirection)
        }
    }

    LaunchedEffect(selectedCardIds.size) {
        if (selectedCardIds.isEmpty() && isSelectionMode) {
            selectionViewModel.toggleSelectionMode(false)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { TitleHeaderBar(titleName = "즐겨찾기") },
            bottomBar = {
                AnimatedVisibility(
                    visible = !isSelectionMode,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    BottomBarComponent(navController, currentRoute)
                }
            }
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
                                        selectionViewModel.resetSelection() // 선택 상태 초기화
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
                                            topScores = topScoreState.scores,
                                            onImageMoreClick = {
                                                viewModel.updateSelectedCategory(
                                                    "이미지"
                                                )
                                            },
                                            onBlogMoreClick = {
                                                viewModel.updateSelectedCategory(
                                                    "블로그"
                                                )
                                            },
                                            onVideoMoreClick = {
                                                viewModel.updateSelectedCategory(
                                                    "동영상"
                                                )
                                            },
                                            onNewsMoreClick = {
                                                viewModel.updateSelectedCategory(
                                                    "뉴스"
                                                )
                                            }
                                        )
                                    }
                                }

                                "이미지" -> {
                                    if (state.images.isEmpty() && !loadingMore) {
                                        item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
                                    } else {
                                        item {
                                            MasonryImageGrid(
                                                imageUrls = state.images.map {
                                                    it.thumbnailUrl ?: ""
                                                },
                                                isMineList = state.images.map { it.isMine },
                                                cardIdList = state.images.map { it.cardId },
                                                onImageClick = { cardId ->
                                                    navController.navigate(
                                                        "cardDetail/$cardId"
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }

                                // 동영상 카테고리 처리
                                "동영상" -> {
                                    if (state.videos.isEmpty() && !loadingMore) {
                                        item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
                                    } else {
                                        items(
                                            items = state.videos,
                                            key = { it.cardId }
                                        ) { card ->
                                            // Determine if this video is the first visible item
                                            val isTopVideo =
                                                lazyListState.firstVisibleItemIndex == state.videos.indexOf(
                                                    card
                                                )
                                            Box(
                                                modifier = Modifier
                                                    .animateContentSize(
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                                            stiffness = if (isSelectionMode) Spring.StiffnessLow else Spring.StiffnessMediumLow
                                                        )
                                                    )
                                                    .animateItemPlacement(
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                                            stiffness = if (isSelectionMode) Spring.StiffnessLow else Spring.StiffnessMediumLow
                                                        )
                                                    )
                                            ) {
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
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .heightIn(max = Dp.Unspecified)
                                                    ) { currentCard, isSelected ->
                                                        // Wait for 2 seconds before auto-playing the first video
                                                        LaunchedEffect(lazyListState.firstVisibleItemIndex) {
                                                            // Add delay before auto-playing the video
                                                            delay(2000) // 2 seconds delay
                                                        }

                                                        VideoBig(
                                                            videoId = currentCard.thumbnailUrl
                                                                ?: "",
                                                            title = currentCard.title,
                                                            isMine = currentCard.isMine,
                                                            thumbnailContent = currentCard.thumbnailContent
                                                                ?: "",
                                                            isSelected = isSelected,
                                                            keywords = currentCard.keywords.take(
                                                                3
                                                            ),
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
                                                        isSelected = selectedCardIds.contains(
                                                            card.cardId
                                                        ),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .heightIn(max = Dp.Unspecified),
                                                        thumbnailContent = card.thumbnailContent
                                                            ?: "",
                                                        onClick = {
                                                            haptics.performHapticFeedback(
                                                                HapticFeedbackType.TextHandleMove
                                                            )
                                                            selectionViewModel.toggleCardSelection(
                                                                card
                                                            )
                                                        }
                                                    )
                                                }
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

                                "블로그" -> {
                                    if (state.blogs.isEmpty() && !loadingMore) {
                                        item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
                                    } else {
                                        items(
                                            items = state.blogs,
                                            key = { it.cardId }
                                        ) { card ->
                                            Box(
                                                modifier = Modifier
                                                    .animateContentSize(
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                                            stiffness = if (isSelectionMode) Spring.StiffnessLow else Spring.StiffnessMediumLow
                                                        )
                                                    )
                                                    .animateItemPlacement(
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                                            stiffness = if (isSelectionMode) Spring.StiffnessLow else Spring.StiffnessMediumLow
                                                        )
                                                    )
                                            ) {
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
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .heightIn(max = Dp.Unspecified)
                                                    ) { currentCard, isSelected ->
                                                        BlogBig(
                                                            title = currentCard.title,
                                                            description = currentCard.thumbnailContent
                                                                ?: "",
                                                            imageUrl = currentCard.thumbnailUrl
                                                                ?: "",
                                                            isMine = currentCard.isMine,
                                                            isSelected = isSelected,
                                                            keywords = currentCard.keywords
                                                            //                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }
                                                        )
                                                    }
                                                } else { // 선택 모드일 때
                                                    BlogSelectionItem(
                                                        title = card.title,
                                                        description = card.thumbnailContent
                                                            ?: "",
                                                        imageUrl = card.thumbnailUrl ?: "",
                                                        isMine = card.isMine,
                                                        keywords = card.keywords,
                                                        isSelected = selectedCardIds.contains(
                                                            card.cardId
                                                        ),
                                                        onClick = {
                                                            haptics.performHapticFeedback(
                                                                HapticFeedbackType.TextHandleMove
                                                            )
                                                            selectionViewModel.toggleCardSelection(
                                                                card
                                                            )
                                                        }
                                                    )
                                                }
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

                                "뉴스" -> {
                                    if (state.news.isEmpty() && !loadingMore) {
                                        item { EmptyMessage3("저장된 즐겨찾기가 없습니다") }
                                    } else {
                                        items(
                                            items = state.news,
                                            key = { it.cardId }
                                        ) { card ->
                                            Box(
                                                modifier = Modifier
                                                    .animateContentSize(
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                                            stiffness = if (isSelectionMode) Spring.StiffnessLow else Spring.StiffnessMediumLow
                                                        )
                                                    )
                                                    .animateItemPlacement(
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                                            stiffness = if (isSelectionMode) Spring.StiffnessLow else Spring.StiffnessMediumLow
                                                        )
                                                    )
                                            ) {
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
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .heightIn(max = Dp.Unspecified)
                                                    ) { currentCard, isSelected ->
                                                        NewsBig(
                                                            title = currentCard.title,
                                                            keywords = currentCard.keywords,
                                                            imageUrl = currentCard.thumbnailUrl
                                                                ?: "",
                                                            isMine = currentCard.isMine,
                                                            isSelected = isSelected,
                                                            description = currentCard.thumbnailContent
                                                                ?: ""
                                                        )
                                                    }
                                                } else {
                                                    NewsSelectionItem(
                                                        title = card.title,
                                                        description = card.thumbnailContent
                                                            ?: "",
                                                        imageUrl = card.thumbnailUrl ?: "",
                                                        isMine = card.isMine,
                                                        keywords = card.keywords,
                                                        isSelected = selectedCardIds.contains(
                                                            card.cardId
                                                        ),
                                                        onClick = {
                                                            haptics.performHapticFeedback(
                                                                HapticFeedbackType.TextHandleMove
                                                            )
                                                            selectionViewModel.toggleCardSelection(
                                                                card
                                                            )
                                                        }
                                                    )
                                                }
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

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = isSelectionMode,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Surface(
                    color = Color(0xFF1B1B1B),
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 6.dp)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${displayCount.value}개 선택됨",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp).padding(top = 2.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    selectionViewModel.clearSelection()
                                    selectionViewModel.toggleSelectionMode(false)
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White.copy(alpha = 0.8f)
                                )
                            ) {
                                Text(
                                    "취소",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.deleteCard(selectedCardIds.toList())
                                    selectionViewModel.clearSelection()
                                    selectionViewModel.toggleSelectionMode(false)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF5C50)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                modifier = Modifier.height(30.dp).width(72.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "삭제",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }
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
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}
