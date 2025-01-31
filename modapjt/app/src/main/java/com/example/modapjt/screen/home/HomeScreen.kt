package com.example.modapjt.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TopBarComponent
import com.example.modapjt.components.board.BoardItem
import com.example.modapjt.components.home.CreateBoardButton
import com.example.modapjt.components.home.SelectBoard
import com.example.modapjt.components.profile.MyProfile
import com.example.modapjt.domain.viewmodel.BoardViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    currentRoute: String,
    viewModel: BoardViewModel = viewModel()
) {
    // 상태 관리 : ViewModel에서 보드 목록을 불러옴
    val boards by viewModel.boards.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // 현재 선택된 보드 유형 ("my" = 내 보드, "bookmark" = 즐겨찾기 보드)
    var selectedBoardType by remember { mutableStateOf("my") }

    Scaffold(
        topBar = { TopBarComponent() },
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
                        modifier = Modifier.align(Alignment.Center) // 로딩 UI
                    )
                }
                error != null -> {
                    Text(
                        text = "에러: $error",
                        modifier = Modifier.align(Alignment.Center) // 에러 메시지
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // MyProfile에 navController 전달
                        item { MyProfile(navController = navController) }

                        item {
                            Column {
                                SelectBoard(
                                    onMyBoardsClick = {
                                        selectedBoardType = "my" // 내 보드 선택
                                        viewModel.loadMyBoards() // 내 보드 데이터 로드
                                    },
                                    onBookMarkBoardsClick = {
                                        selectedBoardType = "bookmark" // 즐겨찾기 보드 선택
                                        viewModel.loadMyBookMarkBoards() // 즐겨찾기 데이터 로드
                                    }
                                )

                                // "내 보드" 선택 시, SelectBoard 아래에 보드 생성 버튼 추가
                                if (selectedBoardType == "my") {
                                    CreateBoardButton()
                                }
                            }
                        }
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
