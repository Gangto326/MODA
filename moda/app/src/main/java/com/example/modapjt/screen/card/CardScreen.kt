////package com.example.modapjt.screen.card
////
////import android.os.Build
////import androidx.annotation.RequiresApi
////import androidx.compose.foundation.layout.*
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.unit.dp
////import androidx.navigation.NavController
////import com.example.modapjt.components.card.CardDetail
////import com.example.modapjt.components.bar.TopBackBarComponent
////import com.example.modapjt.components.bar.BottomBarComponent
////import com.example.modapjt.domain.viewmodel.CardViewModel
////import androidx.lifecycle.viewmodel.compose.viewModel
////
////@RequiresApi(Build.VERSION_CODES.O)
////@Composable
////fun CardScreen(
////    cardId: String?,
////    navController: NavController,
////    currentRoute: String,
////    viewModel: CardViewModel = viewModel()
////) {
////    // 상태 관찰
////    val selectedCard by viewModel.selectedCard.collectAsState()
////    val isLoading by viewModel.isLoading.collectAsState()
////    val error by viewModel.error.collectAsState()
////
////    // 화면 진입 시 카드 데이터 로드
////    LaunchedEffect(cardId) {
////        if (cardId != null) {
////            viewModel.loadCardDetail(cardId)
////        }
////    }
////
////    Scaffold(
////        topBar = { TopBackBarComponent(navController) },
////        bottomBar = { BottomBarComponent(navController, currentRoute) }
////    ) { paddingValues ->
////        Box(
////            modifier = Modifier
////                .fillMaxSize()
////                .padding(paddingValues)
////        ) {
////            when {
////                isLoading -> {
////                    CircularProgressIndicator(
////                        modifier = Modifier.align(Alignment.Center)
////                    )
////                }
////                error != null -> {
////                    Text(
////                        text = "에러: $error",
////                        modifier = Modifier.align(Alignment.Center)
////                    )
////                }
////                selectedCard != null -> {
////                    Column(modifier = Modifier.padding(16.dp)) {
////                        CardDetail(card = selectedCard!!)
////                    }
////                }
////                else -> {
////                    Text(
////                        text = "카드를 찾을 수 없습니다.",
////                        modifier = Modifier.align(Alignment.Center)
////                    )
////                }
////            }
////        }
////    }
////}
//
//
//
//
//
////package com.example.modapjt.screen.card
////
////import android.os.Build
////import androidx.annotation.RequiresApi
////import androidx.compose.foundation.layout.*
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.unit.dp
////import androidx.navigation.NavController
////import androidx.lifecycle.viewmodel.compose.viewModel
////import com.example.modapjt.components.card.CardItem
////import com.example.modapjt.components.bar.BottomBarComponent
////import com.example.modapjt.components.bar.TopBackBarComponent
////import com.example.modapjt.domain.viewmodel.CardViewModel
////
////@RequiresApi(Build.VERSION_CODES.O)
////@Composable
////fun CardScreen(
////    cardId: String?,
////    navController: NavController,
////    currentRoute: String,
////    viewModel: CardViewModel = viewModel()
////) {
////    val cards by viewModel.cards.collectAsState()
////    val isLoading by viewModel.isLoading.collectAsState()
////    val error by viewModel.error.collectAsState()
////
////    val currentCard = remember(cards, cardId) {
////        cards.find { it.cardId == cardId }
////    }
////
////    LaunchedEffect(Unit) {
////        viewModel.loadCardList("user", "1") // 현재는 userId = "user", boardId = "1"로 테스트
////    }
////
////    Scaffold(
////        topBar = { TopBackBarComponent(navController) },
////        bottomBar = { BottomBarComponent(navController, currentRoute) }
////    ) { paddingValues ->
////        Box(
////            modifier = Modifier
////                .fillMaxSize()
////                .padding(paddingValues)
////        ) {
////            when {
////                isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
////                error != null -> Text(text = "에러: $error", Modifier.align(Alignment.Center))
////                currentCard != null -> {
////                    Column(Modifier.padding(16.dp)) {
////                        // 카드 제목
////                        Text(
////                            text = "카드 제목: ${currentCard.thumbnailContent ?: "제목 없음"}",
////                            style = MaterialTheme.typography.headlineMedium,
////                            modifier = Modifier.padding(bottom = 16.dp)
////                        )
////
////                        // 카드 내용
////                        Text(
////                            text = currentCard.thumbnailContent ?: "내용 없음",
////                            style = MaterialTheme.typography.bodyLarge
////                        )
////
////                        Spacer(modifier = Modifier.height(16.dp))
////                    }
////                }
////                else -> Text(text = "카드를 찾을 수 없습니다.", Modifier.align(Alignment.Center))
////            }
////        }
////    }
////}
//
//
//
//
//
//
//package com.example.modapjt.screen.card
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavController
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.modapjt.components.bar.BottomBarComponent
//import com.example.modapjt.components.bar.TopBackBarComponent
//import com.example.modapjt.components.cardlist.CardDetail
//import com.example.modapjt.domain.viewmodel.CardViewModel
//
////
////@Composable
////fun CardScreen(
////    cardId: String?,
////    navController: NavController,
////    currentRoute: String,
////    viewModel: CardViewModel = viewModel()
////) {
////    val cards by viewModel.cards.collectAsState()
////    val isLoading by viewModel.isLoading.collectAsState()
////    val error by viewModel.error.collectAsState()
////
////    val currentCard = remember(cards, cardId) {
////        cards.find { it.cardId == cardId }
////    }
////
////    LaunchedEffect(cardId) {
////        if (cardId != null) {
////            viewModel.loadCardList("user", "1") // 현재는 userId = "user", boardId = "1"로 테스트
////        }
////    }
////
////    Scaffold(
////        topBar = { TopBackBarComponent(navController) },
////        bottomBar = { BottomBarComponent(navController, currentRoute) }
////    ) { paddingValues ->
////        Box(
////            modifier = Modifier
////                .fillMaxSize()
////                .padding(paddingValues)
////        ) {
////            when {
////                isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
////                error != null -> Text(text = "에러: $error", Modifier.align(Alignment.Center))
////                currentCard != null -> {
////                    Column(Modifier.padding(16.dp)) {
////                        // 유튜브 카드면 자동으로 영상이 보이도록 CardDetail 사용
////                        CardDetail(card = currentCard)
////                    }
////                }
////                else -> Text(text = "카드를 찾을 수 없습니다.", Modifier.align(Alignment.Center))
////            }
////        }
////    }
////}
//
//@Composable
//fun CardScreen(
//    cardId: String?,
//    navController: NavController,
//    currentRoute: String,
//    viewModel: CardViewModel = viewModel()
//) {
//    val cardDetail by viewModel.cardDetail.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val error by viewModel.error.collectAsState()
//
//    LaunchedEffect(cardId) {
//        if (cardId != null) {
//            viewModel.loadCardDetail("user", cardId)
//        }
//    }
//
//    Scaffold(
//        topBar = { TopBackBarComponent(navController) },
//        bottomBar = { BottomBarComponent(navController, currentRoute) }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            when {
//                isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
//                error != null -> Text(text = "에러: $error", Modifier.align(Alignment.Center))
//                cardDetail != null -> {
//                    CardDetail(cardDetail = cardDetail!!)  // 매개변수 이름을 cardDetail로 지정
//                }
//                else -> Text(
//                    text = "카드를 찾을 수 없습니다.",
//                    modifier = Modifier.align(Alignment.Center)
//                )
//            }
//        }
//    }
//}