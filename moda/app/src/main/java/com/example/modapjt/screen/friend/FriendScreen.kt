package com.example.modapjt.screen.friend

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.screen.friends.FollowListScreen
import com.example.modapjt.screen.friends.FollowingListScreen
import com.example.modapjt.screen.friends.FriendSearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendScreen(navController: NavController, selectedTab: String, userName: String = "이름") {
    var selectedTabIndex by remember { mutableStateOf(getTabIndex(selectedTab)) }

    val tabTitles = listOf("팔로우", "팔로잉", "추가")

    Scaffold(
        topBar = {
            // 헤더 추가 (뒤로 가기 버튼 + 가운데 유저 이름)
            CenterAlignedTopAppBar(
                title = { Text(userName) }, // 유저 이름 가운데 정렬
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // 뒤로 가기 버튼
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            // 탭 UI 구성
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // 선택된 탭에 맞는 화면 표시
            when (selectedTabIndex) {
                0 -> FollowListScreen(navController)  // 팔로우 리스트
                1 -> FollowingListScreen(navController) // 팔로잉 리스트
                2 -> FriendSearchScreen(navController) // 친구 추가 검색 화면
            }
        }
    }
}

// URL에서 받은 파라미터로 어떤 탭을 선택할지 결정
fun getTabIndex(selectedTab: String): Int {
    return when (selectedTab) {
        "following" -> 0
        "followers" -> 1
        "add" -> 2
        else -> 0
    }
}
