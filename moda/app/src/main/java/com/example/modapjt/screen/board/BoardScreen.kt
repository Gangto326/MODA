package com.example.modapjt.screen.board

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.card.CardItem
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TopBackBarComponent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.components.video.YouTubePlayer
import com.example.modapjt.domain.viewmodel.BoardViewModel
import com.example.modapjt.utils.extractYouTubeVideoId


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BoardScreen(
    boardId: String?,
    navController: NavController,
    currentRoute: String,
    viewModel: BoardViewModel = viewModel()
) {
    val boards by viewModel.boards.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val currentBoard = remember(boards, boardId) {
        boards.find { it.boardId == boardId }
    }

    LaunchedEffect(boardId) {
        if (boardId != null) {
            viewModel.loadBoardDetail(boardId)
        }
    }

    Scaffold(
        topBar = { TopBackBarComponent(navController) },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                error != null -> Text(text = "에러: $error", Modifier.align(Alignment.Center))
                currentBoard != null -> {
                    Column(Modifier.padding(16.dp)) {
                        // 보드 제목
                        Text(
                            text = "보드 이름: ${currentBoard.title}",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        val cards = currentBoard.cards
                        if (cards.isEmpty()) {
                            Text(text = "카드 리스트가 존재하지 않습니다.", style = MaterialTheme.typography.bodyLarge)
                        } else {
                            // 🔹 모든 카드를 `CardItem`으로 표시 (첫 번째 카드 포함)
                            cards.forEach { card ->
                                CardItem(card = card, navController = navController)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                else -> Text(text = "보드를 찾을 수 없습니다.", Modifier.align(Alignment.Center))
            }
        }
    }
}