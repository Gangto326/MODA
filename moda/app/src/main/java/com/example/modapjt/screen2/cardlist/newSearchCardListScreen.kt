package com.example.modapjt.screen2

import AllTabCard
import BlogBig
import ImageBig
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun newSearchCardListScreen(
    navController: NavController,
    currentRoute: String,
    initialQuery: String, // ✅ 초기 검색어 추가
    viewModel: CardViewModel = viewModel()
) {
    var query by remember { mutableStateOf(initialQuery) } // ✅ 검색어 상태 관리
    var selectedCategory by remember { mutableStateOf("전체") }
    var selectedSort by remember { mutableStateOf("최신순") } // ✅ 정렬 상태 추가
    val uiState by viewModel.uiState.collectAsState()
    val userId = "user" // 실제 사용자 ID로 교체 필요

    // 데이터 로드
    LaunchedEffect(query, selectedCategory, selectedSort) {
        if (query.isNotBlank()) {
            val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
            println("[newSearchCardListScreen] 검색어: $query, 선택된 탭: $selectedCategory, 정렬: $selectedSort ($sortDirection)")
            viewModel.loadSearchCards(userId, query, selectedCategory, sortDirection) // ✅ 검색 API 호출
        }
    }

    Scaffold(
//        topBar = { SearchBarComponent(query, onQueryChanged = { query = it }) }, // ✅ 검색창 추가
        topBar = { SearchBarComponent(query = query, navController = navController) }, // ✅ 검색창 컴포넌트 수정
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

                            "이미지" -> {
                                if (data.images.isEmpty()) {
                                    item { EmptyMessage2("저장된 이미지가 없습니다") }
                                } else {
                                    val chunkedImages = data.images.chunked(2)

                                    items(chunkedImages) { rowImages ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                                            onClick = {},
                                                            modifier = Modifier.fillMaxWidth(),
                                                            isMine = card.isMine,  // ✅ isMine 값 전달
                                                        )
                                                    }
                                                }
                                            }
                                            if (rowImages.size == 1) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }

                            "블로그" -> {
                                if (data.blogs.isEmpty()) {
                                    item { EmptyMessage2("저장된 블로그가 없습니다") }
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
                                                onClick = {},
                                                isMine = card.isMine,  // ✅ isMine 값 전달
                                            )
                                        }
                                    }
                                }
                            }

                            "동영상" -> {
                                if (data.videos.isEmpty()) {
                                    item { EmptyMessage2("저장된 영상이 없습니다") }
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
                                                isMine = card.isMine,  // ✅ isMine 값 전달
                                            )
                                        }
                                    }
                                }
                            }

                            "뉴스" -> {
                                if (data.news.isEmpty()) {
                                    item { EmptyMessage2("저장된 뉴스가 없습니다") }
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
                                                isMine = card.isMine,  // ✅ isMine 값 전달
                                                onClick = {}
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

// ✅ 검색창 컴포넌트 추가
//@Composable
//fun SearchBarComponent(
//    query: String,
//    onQueryChanged: (String) -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        androidx.compose.material3.TextField(
//            value = query,
//            onValueChange = onQueryChanged,
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = { Text("검색어를 입력하세요") },
//            singleLine = true
//        )
//    }
//}
//@Composable
//fun SearchBarComponent(
//    query: String,
//    navController: NavController
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//            .clickable { navController.navigate("search") } // ✅ 검색창 클릭 시 검색 화면으로 이동
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextField(
//                value = query,
//                onValueChange = {},
//                modifier = Modifier.weight(1f),
//                readOnly = true, // ✅ 검색어 입력 방지
//                placeholder = { Text("검색어를 입력하세요") },
//                singleLine = true
//            )
//
//            IconButton(
//                onClick = { navController.navigate("search") }, // ✅ 돋보기 클릭 시 검색 화면 이동
//                modifier = Modifier.size(48.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_search),
//                    contentDescription = "Search Icon",
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        }
//    }
//}
@Composable
fun SearchBarComponent(
    query: String,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("search") } // ✅ 검색창 클릭 시 검색 화면으로 이동
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
                    .clickable { navController.navigate("search") }, // ✅ 입력창 클릭 시 이동
                readOnly = true, // ✅ 검색어 입력 방지
                placeholder = { Text("검색어를 입력하세요") },
                singleLine = true
            )

            IconButton(
                onClick = { navController.navigate("search") }, // ✅ 돋보기 클릭 시 이동
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



// 공통으로 사용되는 빈 목록 메시지
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
