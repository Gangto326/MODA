package com.example.modapjt.screen2

import AllTabCard
import BlogBig
import NewsBig
import TypeSelectBar
import VideoBig
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.CategoryHeaderBar
import com.example.modapjt.components.cardlist.BlogSelectionItem
import com.example.modapjt.components.cardlist.NewsSelectionItem
import com.example.modapjt.components.cardlist.ScrollToTopButton
import com.example.modapjt.components.cardlist.VideoSelectionItem
import com.example.modapjt.components.cardtab.SwipableCardList
import com.example.modapjt.domain.model.Card
import com.example.modapjt.domain.viewmodel.CardSelectionViewModel
import com.example.modapjt.domain.viewmodel.CardUiState
import com.example.modapjt.domain.viewmodel.CardViewModel
import com.example.modapjt.domain.viewmodel.CategoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalFoundationApi
@Composable
fun newCardListScreen(
    navController: NavController,
    currentRoute: String,
    categoryId: Int?,
    viewModel: CardViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    var selectedSort by remember { mutableStateOf("최신순") }
    val uiState by viewModel.uiState.collectAsState()
    val loadingMore by viewModel.loadingMore.collectAsState()
    val topScoreState by viewModel.topScoreState.collectAsState()  // TopScore 상태 추가
    val categoryName by categoryViewModel.categoryName.collectAsState()

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

    // 카테고리나 정렬 변경시 페이지네이션 리셋
    LaunchedEffect(categoryId, selectedCategory, selectedSort) {
        viewModel.resetPagination()
        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
        categoryViewModel.loadCategories()
        categoryId?.let {
            viewModel.loadCards(it, selectedCategory, sortDirection)
            categoryViewModel.updateCategoryName(it)
        }
    }

    LaunchedEffect(selectedCardIds.size) {
        if (selectedCardIds.isEmpty() && isSelectionMode) {
            selectionViewModel.toggleSelectionMode(false)
        }
    }

    val systemUiController = rememberSystemUiController()
    LaunchedEffect(isSelectionMode) {
        // 네비게이션 바 색상
        systemUiController.setNavigationBarColor(
            color = if (isSelectionMode) Color(0xFF1B1B1B) else Color.Transparent,
            darkIcons = !isSelectionMode
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CategoryHeaderBar(
                    categoryName = categoryName,
                    navController = navController
                )
            },
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
                            state = lazyListState, // LazyListState 연결
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

                                "동영상" -> {
                                    if (state.videos.isEmpty() && !loadingMore) {
                                        item { EmptyMessage("저장된 영상이 없습니다") }
                                    } else {
                                        items(
                                            items = state.videos,
                                            key = { it.cardId }
                                        ) { card ->
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
                                                        // 비디오 표시
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
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 16.dp, vertical = 8.dp)  // Surface와 동일한 padding 적용
                                                            .clip(RoundedCornerShape(8.dp))
                                                    ) {
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

                                                        if (selectedCardIds.contains(card.cardId)) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .matchParentSize()
                                                                    .background(Color.Black.copy(alpha = 0.5f))
                                                            )
                                                            if (selectedCardIds.contains(
                                                                    card.cardId
                                                                )) {
                                                                Image(
                                                                    painter = painterResource(id = R.drawable.ic_s_select),
                                                                    contentDescription = "Selected",
                                                                    modifier = Modifier
                                                                        .align(Alignment.BottomEnd)
                                                                        .padding(top = 16.dp, end = 17.dp, bottom = 12.dp)
                                                                        .size(19.dp)
                                                                        .zIndex(2f)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            // 각 비디오 사이에 구분선 추가
                                            Divider(
                                                color = Color(0xFFF1F1F1),
                                                thickness = 1.dp,
                                                modifier = Modifier.padding(
                                                    start = 16.dp,
                                                    end = 16.dp
                                                ) // 양쪽에 패딩 추가
                                            )
                                        }
                                    }
                                }

                                "이미지" -> {
                                    if (state.images.isEmpty() && !loadingMore) {
                                        item { EmptyMessage("저장된 이미지가 없습니다") }
                                    } else {
                                        item {
                                            MasonryImageGrid(
                                                imageUrls = state.images.map {
                                                    it.thumbnailUrl ?: ""
                                                },
                                                isMineList = state.images.map { it.isMine },
                                                cardIdList = state.images.map { it.cardId },
                                                onImageClick = { cardId -> navController.navigate("cardDetail/$cardId") }
                                            )
                                        }
                                    }
                                }

                                "블로그" -> {
                                    if (state.blogs.isEmpty() && !loadingMore) {
                                        item { EmptyMessage("저장된 블로그가 없습니다") }
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
                                                        description = card.thumbnailContent ?: "",
                                                        imageUrl = card.thumbnailUrl ?: "",
                                                        isMine = card.isMine,
                                                        keywords = card.keywords,
                                                        isSelected = selectedCardIds.contains(card.cardId),
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
                                                color = Color(0xFFF1F1F1),
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
                                        item { EmptyMessage("저장된 뉴스가 없습니다") }
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
                                                        description = card.thumbnailContent ?: "",
                                                        imageUrl = card.thumbnailUrl ?: "",
                                                        isMine = card.isMine,
                                                        keywords = card.keywords,
                                                        isSelected = selectedCardIds.contains(card.cardId),
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
                                                color = Color(0xFFF1F1F1),
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
                            if (loadingMore) {
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
fun EmptyMessage(message: String) {
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


