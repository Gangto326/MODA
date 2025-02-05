package com.example.modapjt.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TopBarComponent
import com.example.modapjt.components.board.BoardItem
import com.example.modapjt.components.home.CreateBoardButton
import com.example.modapjt.domain.viewmodel.BoardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    currentRoute: String,
    onStartOverlay: () -> Unit,  // 오버레이 기능 콜백 유지
    viewModel: BoardViewModel = viewModel()
) {
    // ViewModel에서 보드 목록 불러오기
    val boards by viewModel.boards.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // 화면이 로드될 때 API 호출 실행
    LaunchedEffect(Unit) {
        viewModel.loadBoardList("user") // 현재는 userId = "user"로 테스트
    }

    Scaffold(
        topBar = { TopBarComponent() }, // 상단바 유지
        bottomBar = { BottomBarComponent(navController, currentRoute) } // 하단바 유지
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center)) // 로딩 UI
                }
                error != null -> {
                    Text(text = "에러: $error", Modifier.align(Alignment.Center)) // 에러 메시지 UI
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 오버레이 관련 버튼 유지
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "링크 저장 기능",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = onStartOverlay,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("오버레이 시작")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { navController.navigate("saved_urls") },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("저장된 링크 보기")
                                    }
                                }
                            }
                        }

//                        // 보드 생성 버튼 추가
//                        item {
//                            CreateBoardButton()
//                        }

                        // 보드 목록 표시
                        items(boards) { board ->
                            BoardItem(
                                board = board,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
