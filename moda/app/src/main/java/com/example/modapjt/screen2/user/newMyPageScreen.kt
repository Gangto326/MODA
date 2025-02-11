package com.example.modapjt.screen2.user

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.OverlayService
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.setting.SettingItem
import com.example.modapjt.components.user.InterestKeywords
import com.example.modapjt.components.user.MyPageHeader
import com.example.modapjt.components.user.UserProfileCard
import com.example.modapjt.domain.viewmodel.UserViewModel

@Composable
fun MyPageScreen(
    userId: String,
    navController: NavController? = null,
    currentRoute: String = ""
) {
    val viewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    var isOverlayActive by remember { mutableStateOf(false) } // 오버레이 상태 추가

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

                // ✅ 링크 저장 기능 (오버레이 설정 추가)
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "링크 저장 기능",
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "다른 앱에서 링크를 빠르게 저장할 수 있습니다.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // ✅ 오버레이 시작 버튼 (토글 기능 추가)
                            Button(
                                onClick = {
                                    isOverlayActive = !isOverlayActive // 상태 반전

                                    // 오버레이 서비스 시작/종료
                                    val serviceIntent = Intent(context, OverlayService::class.java)
                                    if (isOverlayActive) {
                                        context.startService(serviceIntent) // 오버레이 시작
                                    } else {
                                        context.stopService(serviceIntent) // 오버레이 종료
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC80))
                            ) {
                                Text(if (isOverlayActive) "오버레이 종료" else "오버레이 시작", color = Color.Black)
                            }
                        }
                    }
                }

                // ✅ 설정 항목 리스트 추가
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        SettingItem(title = "알림 설정") { /* TODO: 알림 설정 추가 */ }
                        SettingItem(title = "로그아웃") { /* TODO: 로그아웃 기능 추가 */ }
                    }
                }
            }
        }
    }
}
