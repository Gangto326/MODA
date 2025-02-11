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
import androidx.compose.material3.AlertDialog
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
    navController: NavController,
    currentRoute: String = ""
) {
    val viewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    var isOverlayActive by remember { mutableStateOf(false) } // 오버레이 상태 추가
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
        viewModel.fetchInterestKeywords(userId)
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

                item {
                    InterestKeywords(keywords)
                    Spacer(modifier = Modifier.height(16.dp))
                }

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

                            Button(
                                onClick = {
                                    isOverlayActive = !isOverlayActive
                                    val serviceIntent = Intent(context, OverlayService::class.java)
                                    if (isOverlayActive) {
                                        context.startService(serviceIntent)
                                    } else {
                                        context.stopService(serviceIntent)
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

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        SettingItem(title = "알림 설정") { /* TODO: 알림 설정 추가 */ }
                        SettingItem(title = "로그아웃") { showLogoutDialog = true }
                    }

                    // ✅ LazyColumn 내부에서 다이얼로그 호출
                    if (showLogoutDialog) {
                        LogoutDialog(
                            onConfirm = {
                                showLogoutDialog = false
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onDismiss = { showLogoutDialog = false }
                        )
                    }
                }
            }
        }
    }
}

// ✅ 별도 @Composable 함수로 로그아웃 다이얼로그 분리
@Composable
fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("로그아웃") },
        text = { Text("로그아웃 하시겠습니까?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
