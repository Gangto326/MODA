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
import com.example.modapjt.components.user.InterestKeywords

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
        viewModel.fetchInterestKeywords(userId) // ✅ userId 기반으로 호출
    }


    val user by viewModel.user.collectAsState()
    val keywords by viewModel.interestKeywords.collectAsState(initial = emptyList())

    Scaffold(
        bottomBar = {
            navController?.let {
                BottomBarComponent(it, currentRoute)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MyPageHeader()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (user == null) {
                        CircularProgressIndicator()
                    } else {
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ✅ 관심 키워드 컴포넌트 추가 (API 연동)
                item {
                    InterestKeywords(keywords)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
