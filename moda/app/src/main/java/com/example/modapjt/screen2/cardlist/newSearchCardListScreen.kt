package com.example.modapjt.screen2.cardlist

import AllTabCard
import BlogBig
import NewsBig
import TypeSelectBar
import VideoBig
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.cardtab.SwipableCardList
import com.example.modapjt.datastore.SearchKeywordDataStore
import com.example.modapjt.domain.viewmodel.CardUiState
import com.example.modapjt.domain.viewmodel.CardViewModel
import com.example.modapjt.screen2.MasonryImageGrid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
    val loadingMore by viewModel.loadingMore.collectAsState()
    val context = LocalContext.current

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
                onQueryChange = { newQuery ->
                    query = newQuery
                },
                onSearch = { searchQuery ->
                    if (searchQuery.isNotBlank()) {
                        query = searchQuery
                        viewModel.resetPagination()
                        val sortDirection = if (selectedSort == "최신순") "DESC" else "ASC"
                        viewModel.loadSearchCards(searchQuery, selectedCategory, sortDirection)
                    }
                },
                navController = navController
            )
        },
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
                                    item { EmptyMessage2("검색된 이미지가 없습니다") }
                                } else {
                                    item {
                                        MasonryImageGrid(
                                            imageUrls = state.images.map { it.thumbnailUrl ?: "" },  // ✅ 이미지 리스트 전달
                                            isMineList = state.images.map { it.isMine },  // ✅ 내 콘텐츠 여부 전달
                                            cardIdList = state.images.map { it.cardId },  // ✅ 카드 ID 전달
                                            onImageClick = { cardId -> navController.navigate("cardDetail/$cardId") }  // ✅ 클릭 시 이동
                                        )
                                    }
                                }
                            }


///////                         ////////////////////////////////////////////////////////////////////( 기존 코드_추후에 삭제 예정 )
//                            "이미지" -> {
//                                if (state.images.isEmpty() && !loadingMore) {
//                                    item { EmptyMessage2("검색된 이미지가 없습니다") }
//                                } else {
//                                    item {
//                                        FlowRow(
//                                            modifier = Modifier.fillMaxWidth(),
//                                            horizontalArrangement = Arrangement.SpaceBetween,
//                                            verticalArrangement = Arrangement.spacedBy(8.dp)
//                                        ) {
//                                            state.images.forEachIndexed { index, card ->
//                                                ImageBig(
//                                                    imageUrl = card.thumbnailUrl ?: "",
//                                                    isMine = card.isMine,
//                                                    modifier = Modifier
//                                                        .fillMaxWidth(0.5f)
//                                                        .aspectRatio(1f),
//                                                    onClick = { navController.navigate("cardDetail/${card.cardId}") }
//                                                )
//                                            }
//
//                                            if (state.images.size % 2 != 0) {
//                                                Box(modifier = Modifier.fillMaxWidth(0.5f))
//                                            }
//                                        }
//                                    }
//                                }
//                            }

                            ///////////////////////////////////////////////////////////////////////////
                            "블로그" -> {
                                if (state.blogs.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 블로그가 없습니다") }
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
                            "동영상" -> {
                                if (state.videos.isEmpty() && !loadingMore) {
                                    item { EmptyMessage2("검색된 동영상이 없습니다") }
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
                                    item { EmptyMessage2("검색된 뉴스가 없습니다") }
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
fun SearchListBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf(query) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            // 테두리가 있는 검색창
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFFFCC80),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .heightIn(min = 48.dp)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "찾고 싶은 내용을 입력하세요",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = 0.sp
                                )
                            )
                        }

                        BasicTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                onQueryChange(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 0.sp,
                                lineHeight = 24.sp,
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (searchText.isNotBlank()) {
                                        onSearch(searchText)
                                        keyboardController?.hide()
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                                            val updatedKeywords = (listOf(searchText) + currentKeywords).distinct().take(10)
                                            SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                                        }
                                    }
                                }
                            )
                        )
                    }

                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchText = ""
                                onQueryChange("")
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear text",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            if (searchText.isNotBlank()) {
                                onSearch(searchText)
                                keyboardController?.hide()
                                CoroutineScope(Dispatchers.IO).launch {
                                    val currentKeywords = SearchKeywordDataStore.getKeywords(context).first()
                                    val updatedKeywords = (listOf(searchText) + currentKeywords).distinct().take(10)
                                    SearchKeywordDataStore.saveKeywords(context, updatedKeywords)
                                }
                            }
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
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
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}