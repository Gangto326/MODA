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
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

    Scaffold(
        topBar = { TitleHeaderBar(titleName = "즐겨찾기") },
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
                                    items(
                                        items = state.videos,
                                        key = { it.cardId }
                                    ) { card ->
                                        // Determine if this video is the first visible item
                                        val isTopVideo = lazyListState.firstVisibleItemIndex == state.videos.indexOf(card)
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
                                            color = Color(0xFFF1F1F1),
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(start = 16.dp, end = 16.dp) // 양쪽에 패딩 추가
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
                                            color = Color(0xFFF1F1F1),
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(start = 16.dp, end = 16.dp) // 양쪽에 패딩 추가
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
                                                    imageUrl = currentCard.thumbnailUrl ?: "",
                                                    isMine = currentCard.isMine,
                                                    isSelected = isSelected,
                                                    description = currentCard.thumbnailContent?:""
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
                    // 하단 고정 버튼을 Box의 자식으로 이동
                    AnimatedVisibility (
                        visible = isSelectionMode,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${selectedCardIds.size}개 선택됨",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton (
                                    onClick = {
                                        selectionViewModel.clearSelection()
                                        selectionViewModel.toggleSelectionMode(false)
                                    }
                                ) {
                                    Text("취소")
                                }
                                Button(
                                    onClick = {
                                        val cardsToDelete = when(selectedCategory) {
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
