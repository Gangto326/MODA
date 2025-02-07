//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.modapjt.components.bar.BottomBarComponent
//import com.example.modapjt.components.bar.CategoryHeaderBar
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.slideInVertically
//import androidx.compose.animation.slideOutVertically
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.rememberLazyGridState
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.key
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.snapshotFlow
//
//
//@Composable
//fun newCardListScreen(navController: NavController, currentRoute: String, category: String?) {
//    var isTypeBarVisible by remember { mutableStateOf(true) }
//    // TypeSelectBar에서 선택한 콘텐츠 유형 (변경 가능)
//    var selectedCategory by remember { mutableStateOf("전체") }
//    // 홈 화면에서 선택한 카테고리 (변하지 않음)
//    val homeCategory = remember { category ?: "전체" }
//
//    // 카드 리스트를 선택한 카테고리 기준으로 필터링
//    val cardList = remember {
//        getAllCards().filter { it.category == homeCategory || homeCategory == "전체" }
//    }
//
//    Scaffold(
//        topBar = { CategoryHeaderBar(categoryName = homeCategory) }, // 선택한 카테고리를 전달
//        bottomBar = { BottomBarComponent(navController, currentRoute) }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            when (selectedCategory) {
//                "전체" -> {
//                    LazyColumn {
//                        item {
//                            TypeSelectBar(
//                                selectedCategory = selectedCategory,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 8.dp),
//                                onCategorySelected = { selectedCategory = it }
//                            )
//                        }
//                        item {
//                            AllTabCard(
//                                onImageMoreClick = { selectedCategory = "이미지" },
//                                onVideoMoreClick = { selectedCategory = "동영상" },
//                                onBlogMoreClick = { selectedCategory = "블로그" },
//                                onNewsMoreClick = { selectedCategory = "뉴스" },
//                                onImageClick = { index ->
//                                    println("개별 이미지 클릭 시 상세 페이지로 이동")
//                                }
//                            )
//                        }
//                    }
//                }
//
//                "이미지" -> {
//                    Column {
//                        AnimatedVisibility(
//                            visible = isTypeBarVisible,
//                            enter = slideInVertically() + fadeIn(),
//                            exit = slideOutVertically() + fadeOut()
//                        ) {
//                            TypeSelectBar(
//                                selectedCategory = selectedCategory,
//                                onCategorySelected = { selectedCategory = it }
//                            )
//                        }
//
//                        LazyVerticalGrid(
//                            columns = GridCells.Fixed(2),
//                            contentPadding = PaddingValues(12.dp),
//                            horizontalArrangement = Arrangement.spacedBy(8.dp),
//                            verticalArrangement = Arrangement.spacedBy(8.dp),
//                            state = rememberLazyGridState().also { gridState ->
//                                LaunchedEffect(gridState) {
//                                    snapshotFlow { gridState.firstVisibleItemIndex }
//                                        .collect { index ->
//                                            isTypeBarVisible = index == 0
//                                        }
//                                }
//                            }
//                        ) {
//                            items(60) { index ->
//                                ImageBig(
//                                    modifier = Modifier.padding(4.dp),
//                                    onClick = { println("이미지 $index 클릭됨") }
//                                )
//                            }
//                        }
//                    }
//                }
//
//                "동영상" -> {
//                    LazyColumn {
//                        item {
//                            TypeSelectBar(
//                                selectedCategory = selectedCategory,
//                                onCategorySelected = { selectedCategory = it }
//                            )
//                        }
//                        items(30) { index ->
//                            key(index) {
//                                VideoBig(
//                                    videoId = "sample_video_id_$index",
//                                    title = "Video Title $index",
//                                    modifier = Modifier.padding(vertical = 4.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//
//                "블로그" -> {
//                    LazyColumn {
//                        item {
//                            TypeSelectBar(
//                                selectedCategory = selectedCategory,
//                                onCategorySelected = { selectedCategory = it }
//                            )
//                        }
//                        items(30) { index ->
//                            BlogBig(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(8.dp)
//                                    .clickable {
//                                        println("블로그 $index 클릭됨")
//////                                        navController.navigate("imageDetail/$index") // 클릭 시 상세 페이지 이동 가능
//                                    }
//                            )
//                        }
//                    }
//                }
//
//                "뉴스" -> {
//                    LazyColumn {
//                        item {
//                            TypeSelectBar(
//                                selectedCategory = selectedCategory,
//                                onCategorySelected = { selectedCategory = it }
//                            )
//                        }
//                        items(30) { index ->
//                            NewsBig(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(8.dp),
//                                onClick = {
//                                    println("뉴스 $index 클릭됨")
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//// 더미 데이터 (실제 데이터 가져오는 함수)
//fun getAllCards(): List<Card> {
//    return listOf(
//        Card("Card 1", "IT"),
//        Card("Card 2", "Food"),
//        Card("Card 3", "Entertainment"),
//        Card("Card 4", "Finance"),
//        Card("Card 5", "IT")
//    )
//}
//
//// 카드 데이터 모델
//data class Card(val title: String, val category: String)









package com.example.modapjt.screen2

import AllTabCard
import TypeSelectBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.domain.viewmodel.CardViewModel
import com.example.modapjt.domain.viewmodel.CategoryViewModel
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.CategoryHeaderBar
@Composable
fun newCardListScreen(
    navController: NavController,
    currentRoute: String,
    categoryId: Int?,
    viewModel: CardViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel()
) {
    var selectedCategory by remember { mutableStateOf(" ") }
    val userId = "user"  // 테스트용 userId
    val homeCategory = categoryId ?: 1 // 기본값 (1)
    val categoryName by categoryViewModel.categoryName.collectAsState()

    // 카테고리 로드 후 categoryName 업데이트
    LaunchedEffect(homeCategory) {
        categoryViewModel.loadCategories(userId) // 1. 카테고리 리스트 불러오기
    }

    // 카테고리 리스트가 로드된 후, categoryName 업데이트
    LaunchedEffect(categoryViewModel.categories.collectAsState().value) {
        categoryViewModel.updateCategoryName(homeCategory)
    }

    LaunchedEffect(homeCategory) {
        viewModel.loadCards(userId, homeCategory)
    }

    Scaffold(
        topBar = { CategoryHeaderBar(categoryName = categoryName, navController = navController) }, // categoryName 동적으로 적용
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                item {
                    TypeSelectBar(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }
                item {
                    AllTabCard(
                        onImageMoreClick = { selectedCategory = "이미지" },
                        onVideoMoreClick = { selectedCategory = "동영상" },
                        onBlogMoreClick = { selectedCategory = "블로그" },
                        onNewsMoreClick = { selectedCategory = "뉴스" }
                    )
                }
            }
        }
    }
}