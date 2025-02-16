package com.example.modapjt.screen2.carddetail

import BlogDetailScreen
import ImageDetailScreen
import NewsDetailScreen
import VideoDetailScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.CardDetailHeaderBar
import com.example.modapjt.components.bar.HeaderBar
import com.example.modapjt.components.bar.NotMineCardDetailHeaderBar
import com.example.modapjt.domain.viewmodel.CardDetailUiState
import com.example.modapjt.domain.viewmodel.CardDetailViewModel

//@Composable
//fun newCardDetailScreen(
//    navController: NavController,
//    currentRoute: String,
//    cardId: String,
//    viewModel: CardDetailViewModel = viewModel()
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    val userId = "user" // 실제 로그인된 사용자 ID 필요
//
//    // 화면 진입 시 API 호출
//    LaunchedEffect(cardId) {
//        viewModel.loadCardDetail(userId, cardId)
//    }
//
//    Scaffold(
//        topBar = { HeaderBar(modifier = Modifier) },
//        bottomBar = { BottomBarComponent(navController, currentRoute) }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            when (uiState) {
//                is CardDetailUiState.Loading -> {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//
//                is CardDetailUiState.Success -> {
//                    val card = (uiState as CardDetailUiState.Success).cardDetail
//                    Column(
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .verticalScroll(rememberScrollState())
//                    ) {
//                        // 카드 제목
//                        Text(
//                            text = card.title,
//                            style = MaterialTheme.typography.headlineMedium,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        // 카드 썸네일 (유튜브 영상 포함)
//                        if (card.typeId == 1) { // typeId == 1이면 유튜브 영상
//                            val videoId = extractYouTubeVideoId(card.originalUrl)
//                            if (!videoId.isNullOrEmpty()) {
//                                YouTubePlayer(videoId = videoId, modifier = Modifier.fillMaxWidth().height(200.dp))
//                                Spacer(modifier = Modifier.height(8.dp))
//                            }
//                        } else {
//                            card.thumbnailUrl?.let { url ->
//                                ImageThumbnail(url)
//                                Spacer(modifier = Modifier.height(8.dp))
//                            }
//                        }
//
//                        // 카드 내용
//                        Text(
//                            text = card.content,
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        // 키워드 태그
//                        if (card.keywords.isNotEmpty()) {
//                            Text(text = "키워드: ${card.keywords.joinToString(", ")}")
//                        }
//                    }
//                }
//
//                is CardDetailUiState.Error -> {
//                    Text(
//                        text = (uiState as CardDetailUiState.Error).message,
//                        modifier = Modifier.align(Alignment.Center)
//                    )
//                }
//            }
//        }
//    }
//}
//
//// 썸네일 이미지 컴포넌트
//@Composable
//fun ImageThumbnail(url: String) {
//    // 이미지 로딩 로직 (예: Coil 또는 Glide 사용)
//    Text(text = "이미지 썸네일: $url")
//}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun newCardDetailScreen(
    navController: NavController,
    currentRoute: String,
    cardId: String,
    viewModel: CardDetailViewModel = viewModel()
) {
    LaunchedEffect(cardId) {
        viewModel.loadCardDetail(cardId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            when (uiState) {
                is CardDetailUiState.Success -> {
                    val cardDetail = (uiState as CardDetailUiState.Success).cardDetail
                    val title = when (cardDetail.typeId) {
                        1 -> "동영상"
                        2 -> "블로그"
                        3 -> "뉴스"
                        4 -> "이미지"
                        else -> ""
                    }

                    if (cardDetail.isMine) {
                        CardDetailHeaderBar(
                            title = title,
                            isBookmarked = cardDetail.bookmark,
                            onBackClick = { navController.popBackStack() },
                            onBookmarkClick = { viewModel.toggleBookmark(cardId) },
                            onEditClick = { /* 수정 로직 */ },
                            onDeleteClick = { viewModel.deleteCard(listOf(cardId), navController) }
                        )
                    } else {
                        NotMineCardDetailHeaderBar(title = title, onBackClick = { navController.popBackStack() },)
                    }
                }
                else -> HeaderBar(modifier = Modifier)
            }
        },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is CardDetailUiState.Loading -> {
                    Text(text = "로딩 중...")
                }

                is CardDetailUiState.Success -> {
                    val cardDetail = (uiState as CardDetailUiState.Success).cardDetail

                    when (cardDetail.typeId) {
                        1 -> VideoDetailScreen(cardDetail) // ✅ 동영상 UI
                        2 -> BlogDetailScreen(cardDetail) // ✅ 블로그 UI
                        3 -> NewsDetailScreen(cardDetail) // ✅ 뉴스 UI
                        4 -> ImageDetailScreen(cardDetail) // ✅ 이미지 UI
                        else -> Text("알 수 없는 카드 타입입니다.")
                    }
                }

                is CardDetailUiState.Error -> {
                    Text(text = "오류 발생: ${(uiState as CardDetailUiState.Error).message}")
                }
            }
        }
    }
}
