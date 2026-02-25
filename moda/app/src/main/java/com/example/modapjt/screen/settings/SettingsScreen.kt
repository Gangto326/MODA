package com.example.modapjt.screen.settings

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.toktok.overlay.OverlayService
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.setting.SettingHeader
import com.example.modapjt.components.setting.SettingItem

@Composable
fun SettingsScreen(
    navController: NavController,
    currentRoute: String = "settings",
    onStartOverlay: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var isOverlayActive by remember { mutableStateOf(false) } // 오버레이 상태 추가

    Scaffold(
        topBar = {
            SettingHeader(
                title = "설정",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // 링크 저장 기능 카드
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
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

                        // 오버레이 버튼 (토글 기능 추가)
                        Button(
                            onClick = {
                                isOverlayActive = !isOverlayActive // 상태 반전
                                onStartOverlay(isOverlayActive)  // 현재 상태 전달

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
                            Text(if (isOverlayActive) "오버레이 종료aaa" else "오버레이 시작aaa", color = Color.Black)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

            // 설정 항목 리스트
            Column {
                SettingItem(title = "알림 설정") { /* TODO */ }
                SettingItem(title = "다크모드 설정") { /* TODO */ }
                SettingItem(title = "카테고리 순서 변경") { /* TODO */ }
                SettingItem(title = "검색창 설정") { /* TODO */ }
            }
            }
        }
    }
}
