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
import com.example.modapjt.domain.viewmodel.CategoryViewModel

@OptIn(ExperimentalLayoutApi::class)
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
    val userId = "user"
    val categoryName by categoryViewModel.categoryName.collectAsState()

    // 카테고리나 정렬 변경시 페이지네이션 리셋
    LaunchedEffect(categoryId, selectedCategory, selectedSort) {
        viewModel.resetPagination()
        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
        categoryViewModel.loadCategories(userId)
        categoryId?.let {
            viewModel.loadCards(userId, it, selectedCategory, sortDirection)
            categoryViewModel.updateCategoryName(it)
        }
    }


    Scaffold(
        topBar = { CategoryHeaderBar(categoryName = categoryName, navController = navController) },
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

                    // 블로그 탭 페이징
                    LaunchedEffect(state.blogs.size) {
                        if (state.blogs.isNotEmpty() && !loadingMore && selectedCategory == "블로그") {
                            val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
                            categoryId?.let {
                                viewModel.loadCards(userId, it, selectedCategory, sortDirection, true)
                            }
                        }
                    }

                    // 동영상 탭 페이징
                    LaunchedEffect(state.videos.size) {
                        if (state.videos.isNotEmpty() && !loadingMore && selectedCategory == "동영상") {
                            val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
                            categoryId?.let {
                                viewModel.loadCards(userId, it, selectedCategory, sortDirection, true)
                            }
                        }
                    }

                    // 뉴스 탭 페이징
                    LaunchedEffect(state.news.size) {
                        if (state.news.isNotEmpty() && !loadingMore && selectedCategory == "뉴스") {
                            val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
                            categoryId?.let {
                                viewModel.loadCards(userId, it, selectedCategory, sortDirection, true)
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
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

                            else -> {
                                when (selectedCategory) {
//                                    "이미지" -> {
//                                        if (state.images.isEmpty() && !loadingMore) {
//                                            item { EmptyMessage("저장된 이미지가 없습니다") }
//                                        } else {
//                                            items(state.images) { card ->
//                                                SwipableCardList(
//                                                    cards = listOf(card),
//                                                    onDelete = { viewModel.deleteCard(listOf(card.cardId)) }
//                                                ) {
//                                                    ImageBig(
//                                                        imageUrl = card.thumbnailUrl ?: "",
//                                                        isMine = card.isMine,
//                                                        onClick = { navController.navigate("cardDetail/${card.cardId}") }
//                                                    )
//                                                }
//
//                                                if (card == state.images.lastOrNull() && !loadingMore) {
//                                                    LaunchedEffect(Unit) {
//                                                        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
//                                                        categoryId?.let {
//                                                            viewModel.loadCards(userId, it, selectedCategory, sortDirection, true)
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
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
                                            item { EmptyMessage("저장된 블로그가 없습니다") }
                                        } else {
                                            items(
                                                items = state.blogs,
                                                key = { it.cardId }
                                            ) { card ->
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
                                    "동영상" -> {
                                        if (state.videos.isEmpty() && !loadingMore) {
                                            item { EmptyMessage("저장된 영상이 없습니다") }
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
                                            }
                                        }
                                    }
                                    "뉴스" -> {
                                        if (state.news.isEmpty() && !loadingMore) {
                                            item { EmptyMessage("저장된 뉴스가 없습니다") }
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
fun EmptyMessage(message: String) {
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