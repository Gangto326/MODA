package com.example.modapjt.screen.card

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.card.CardDetail
import com.example.modapjt.components.bar.TopBackBarComponent
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.domain.viewmodel.CardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardScreen(
    cardId: String?,
    navController: NavController,
    currentRoute: String,
    viewModel: CardViewModel = viewModel()
) {
    // 상태 관찰
    val selectedCard by viewModel.selectedCard.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // 화면 진입 시 카드 데이터 로드
    LaunchedEffect(cardId) {
        if (cardId != null) {
            viewModel.loadCardDetail(cardId)
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
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Text(
                        text = "에러: $error",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                selectedCard != null -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        CardDetail(card = selectedCard!!)
                    }
                }
                else -> {
                    Text(
                        text = "카드를 찾을 수 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
