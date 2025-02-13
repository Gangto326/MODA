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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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


@Composable
fun newCardListScreen(
    navController: NavController,
    currentRoute: String,
    categoryId: Int?,
    viewModel: CardViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
) {
    var selectedCategory by remember { mutableStateOf("전체") }
    var selectedSort by remember { mutableStateOf("최신순") } // ✅ 정렬 상태 추가
    val uiState by viewModel.uiState.collectAsState()
    val userId = "user" // 실제 사용자 ID로 교체 필요
    val categoryName by categoryViewModel.categoryName.collectAsState()

    // 카테고리 및 선택된 탭에 따라 API 호출
//    LaunchedEffect(categoryId) {
//        categoryViewModel.loadCategories(userId) // 카테고리 먼저 로드
//        categoryId?.let {
//            viewModel.loadCards(userId, it)
//            categoryViewModel.updateCategoryName(it) // 카테고리 로드 후 카테고리 이름 업데이트
//        }
//    }
    // 데이터 로드
    LaunchedEffect(categoryId, selectedCategory, selectedSort) {
        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
        println("[newCardListScreen] 선택된 탭: $selectedCategory, 정렬: $selectedSort ($sortDirection)")

        categoryViewModel.loadCategories(userId) // 카테고리 먼저 로드
        categoryId?.let {
            viewModel.loadCards(userId, it, selectedCategory, sortDirection) // selectedCategory 추가
            categoryViewModel.updateCategoryName(it) // 카테고리 로드 후 카테고리 이름 업데이트
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
                .background(Color.White), // 배경 흰색으로 설정
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
//                                selectedCategory = selectedCategory,
//                                onCategorySelected = { selectedCategory = it }
                                selectedCategory = selectedCategory,
                                selectedSort = selectedSort, // ✅ 정렬 상태 추가
                                onCategorySelected = { selectedCategory = it },
                                onSortSelected = { selectedSort = it } // ✅ 정렬 변경
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

                            // SwipableCardList 적용
                            "이미지" -> {
                                if (data.images.isEmpty()) {
                                    item { EmptyMessage("저장된 이미지가 없습니다") }
                                } else {
                                    val chunkedImages = data.images.chunked(2) // 2개씩 묶어서 새로운 리스트 생성

                                    items(chunkedImages) { rowImages ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp) // 카드 간격 추가
                                        ) {
                                            rowImages.forEach { card ->
                                                Box(modifier = Modifier.weight(1f)) {
                                                    SwipableCardList(
                                                        cards = listOf(card),
//                                                        onDelete = { viewModel.deleteCard(it) }
                                                        onDelete = { viewModel.deleteCard(listOf(it.cardId)) }
                                                    ) {
                                                        ImageBig(
                                                            imageUrl = card.thumbnailUrl ?: "",
//                                                            onClick = {},
                                                            onClick = { navController.navigate("cardDetail/${card.cardId}") }, // ✅ 카드 ID 전달
                                                                    isMine = card.isMine, // ✅ isMine 전달
                                                            modifier = Modifier.fillMaxWidth()
                                                        )
                                                    }
                                                }
                                            }
                                            // 홀수 개라면 빈 공간 차지
                                            if (rowImages.size == 1) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }

                            "블로그" -> {
                                if (data.blogs.isEmpty()) {
                                    item { EmptyMessage("저장된 블로그가 없습니다") }
                                } else {
                                    items(data.blogs) { card ->
                                        SwipableCardList(
                                            cards = listOf(card),
//                                            onDelete = { viewModel.deleteCard(it) }
                                            onDelete = { viewModel.deleteCard(listOf(it.cardId)) }
                                        ) {
                                            BlogBig(
                                                title = card.title,
                                                description = card.thumbnailContent ?: "",
                                                imageUrl = card.thumbnailUrl ?: "",
                                                isMine = card.isMine, // ✅ isMine 전달
//                                                onClick = {},
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }, // ✅ 카드 ID 전달
                                            )
                                        }
                                    }
                                }
                            }

                            "동영상" -> {
                                if (data.videos.isEmpty()) {
                                    item { EmptyMessage("저장된 영상이 없습니다") }
                                } else {
                                    items(data.videos) { card ->
                                        SwipableCardList(
                                            cards = listOf(card),
//                                            onDelete = { viewModel.deleteCard(it) }
                                            onDelete = { viewModel.deleteCard(listOf(it.cardId)) }
                                        ) {
                                            VideoBig(
                                                videoId = card.thumbnailUrl ?: "",
                                                title = card.title,
                                                isMine = card.isMine, // ✅ isMine 전달
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }, // ✅ 카드 ID 전달
                                            )
                                        }
                                    }
                                }
                            }



                            "뉴스" -> {
                                if (data.news.isEmpty()) {
                                    item { EmptyMessage("저장된 뉴스가 없습니다") }
                                } else {
                                    items(data.news) { card ->
                                        SwipableCardList(
                                            cards = listOf(card),
//                                            onDelete = { viewModel.deleteCard(it) }
                                            onDelete = { viewModel.deleteCard(listOf(it.cardId)) }
                                        ) {
                                            NewsBig(
                                                title = card.title,
                                                keywords = card.keywords,
                                                imageUrl = card.thumbnailUrl ?: "",
                                                isMine = card.isMine, // ✅ isMine 전달
//                                                onClick = {},
                                                onClick = { navController.navigate("cardDetail/${card.cardId}") }, // ✅ 카드 ID 전달
                                            )
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

// 공통으로 사용되는 빈 목록 메시지
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



// -> 1/3 지점으로 조정 필요 시 사용
//if (data.images.isEmpty()) {
//    item {
//        Box(
//            modifier = Modifier
//                .fillParentMaxSize()
//                .fillMaxWidth()
//                .padding(bottom = 200.dp),  // 위에서 1/3 지점으로 조정
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "저장된 이미지가 없습니다",
//                color = Color.Gray,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
