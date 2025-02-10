package com.example.modapjt.screen2.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.components.user.MyPageHeader
import com.example.modapjt.components.user.UserProfileCard
import com.example.modapjt.domain.viewmodel.UserViewModel
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent

@Composable
fun MyPageScreen(
    userId: String,
    navController: NavController? = null,
    currentRoute: String = ""
) {
    val viewModel: UserViewModel = viewModel()

    // ✅ API 호출하여 데이터 가져오기
    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }

    val user by viewModel.user.collectAsState()

    Scaffold(
        bottomBar = {
            navController?.let {
                BottomBarComponent(it, currentRoute)
            }
        } // ✅ 항상 밑에 고정되는 BottomBar 추가
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ✅ Scaffold 내부 패딩 적용
        ) {
            // ✅ 헤더 항상 고정
            MyPageHeader()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(), // ✅ 패딩 제거하여 Divider가 가득 차도록 수정
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // ✅ 로딩 상태 표시
                    if (user == null) {
                        CircularProgressIndicator()
                    } else {
                        // ✅ UserProfileCard에 데이터 전달
                        UserProfileCard(
                            profileImage = user?.profileImage,
                            nickname = user?.nickname ?: "사용자"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }



                item {
                    Divider(
                        color = Color(0xFFDCDCDC),
                        thickness = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp) // ✅ 패딩 제거하여 전체 너비 차지하도록 수정
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
