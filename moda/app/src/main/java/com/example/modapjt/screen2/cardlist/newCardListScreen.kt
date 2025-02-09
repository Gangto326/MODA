package com.example.modapjt.screen2

import AllTabCard
import BlogBig
import ImageBig
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.cardtab.SwipableCardList
import com.example.modapjt.domain.viewmodel.CardViewModel
import com.example.modapjt.domain.viewmodel.CategoryViewModel
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.CategoryHeaderBar
import com.example.modapjt.domain.viewmodel.CardUiState


@Composable
fun newCardListScreen(
    navController: NavController,
    currentRoute: String,
    categoryId: Int?,
    viewModel: CardViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
) {
    var selectedCategory by remember { mutableStateOf("전체") }
    val uiState by viewModel.uiState.collectAsState()
    val userId = "user" // 실제 사용자 ID로 교체 필요
    val categoryName by categoryViewModel.categoryName.collectAsState()

    LaunchedEffect(categoryId) {
        categoryViewModel.loadCategories(userId) // 카테고리 먼저 로드
        categoryId?.let {
            viewModel.loadCards(userId, it)
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
                                onCategorySelected = { selectedCategory = it }
                            )
                        }

                        when (selectedCategory) {
                            "전체" -> {
                                item {
                                    AllTabCard(
                                        imageCards = data.images,
                                        videoCards = data.videos,
                                        blogCards = data.blogs,
                                        newsCards = data.news,
                                        onImageMoreClick = { selectedCategory = "이미지" },
                                        onVideoMoreClick = { selectedCategory = "동영상" },
                                        onBlogMoreClick = { selectedCategory = "블로그" },
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
                                                        onDelete = { viewModel.deleteCard(it) }
                                                    ) {
                                                        ImageBig(
                                                            imageUrl = card.thumbnailUrl ?: "",
                                                            onClick = {},
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


                            "동영상" -> {
                                if (data.videos.isEmpty()) {
                                    item { EmptyMessage("저장된 영상이 없습니다") }
                                } else {
                                    items(data.videos) { card ->
                                        SwipableCardList(
                                            cards = listOf(card),
                                            onDelete = { viewModel.deleteCard(it) }
                                        ) {
                                            VideoBig(
                                                videoId = card.thumbnailUrl ?: "",
                                                title = card.title,
                                                // onClick = { navController.navigate("videoDetail/${card.id}")} // 비디오 상세 페이지 이동
                                            )
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
                                            onDelete = { viewModel.deleteCard(it) }
                                        ) {
                                            BlogBig(
                                                title = card.title,
                                                description = card.thumbnailContent ?: "",
                                                imageUrl = card.thumbnailUrl ?: "",
                                                onClick = {},
                                                // onClick = { navController.navigate("blogDetail/${card.id}") } // 블로그 상세 페이지 이동
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
                                            onDelete = { viewModel.deleteCard(it) }
                                        ) {
                                            NewsBig(
                                                title = card.title,
                                                keywords = card.keywords,
                                                imageUrl = card.thumbnailUrl ?: "",
                                                onClick = {},
                                                // onClick = { navController.navigate("newsDetail/${card.id}") } // 뉴스 상세 페이지 이동
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
