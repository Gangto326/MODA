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
import com.example.modapjt.domain.viewmodel.CardViewModel
import com.example.modapjt.utils.extractYouTubeVideoId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BoardScreen(
    boardId: String?,
    navController: NavController,
    currentRoute: String,
    boardViewModel: BoardViewModel = viewModel(),
    cardViewModel: CardViewModel = viewModel() // 카드 ViewModel 추가
) {
    val boards by boardViewModel.boards.collectAsState()
    val cards by cardViewModel.cards.collectAsState() // 카드 목록 상태 추가
    val isLoading by cardViewModel.isLoading.collectAsState()
    val error by cardViewModel.error.collectAsState()

    val currentBoard = remember(boards, boardId) {
        boards.find { it.boardId == boardId }
    }

    // 보드 ID가 변경될 때마다 카드 데이터 불러오기
    LaunchedEffect(boardId) {
        if (boardId != null) {
            boardViewModel.loadBoardList("user") // 기존 보드 로딩
            cardViewModel.loadCardList("user", boardId) // 보드 ID 기반으로 카드 로딩
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
                        Text(
                            text = "보드 이름: ${currentBoard.title}",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // 카드 목록 표시
                        if (cards.isEmpty()) {
                            Text(text = "카드 리스트가 존재하지 않습니다.", style = MaterialTheme.typography.bodyLarge)
                        } else {
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
