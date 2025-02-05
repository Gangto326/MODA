//package com.example.modapjt.screen.settings
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.modapjt.components.bar.BottomBarComponent
//import com.example.modapjt.components.bar.TopBarComponent
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SettingsScreen(navController: NavController, currentRoute: String = "settings") {
//    Scaffold(
//        topBar = { TopBarComponent() },
//        bottomBar = { BottomBarComponent(navController, currentRoute) } // 하단 네비게이션 바 추가
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp)
//        ) {
//            Text(text = "설정 화면", style = MaterialTheme.typography.titleLarge)
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 설정 리스트 (예제)
//            Column {
//                SettingItem(title = "알림 설정") { /* TODO: 알림 설정 페이지 이동 */ }
//                SettingItem(title = "계정 관리") { /* TODO: 계정 설정 페이지 이동 */ }
//                SettingItem(title = "앱 정보") { /* TODO: 앱 정보 페이지 이동 */ }
//                SettingItem(title = "로그아웃") { /* TODO: 로그아웃 기능 추가 */ }
//            }
//        }
//    }
//}
//
//// 개별 설정 항목 UI
//@Composable
//fun SettingItem(title: String, onClick: () -> Unit) {
//    Text(
//        text = title,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 12.dp)
//            .clickable(onClick = onClick),
//        style = MaterialTheme.typography.bodyLarge
//    )
//}




package com.example.modapjt.screen.settings

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TopBarComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    currentRoute: String = "settings",
    context: Context,
    onStartOverlay: () -> Unit  // 오버레이 콜백 추가
) {
    Scaffold(
        topBar = { TopBarComponent() },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "설정 화면", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // 기존 오버레이 설정 섹션
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "링크 저장 기능",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "다른 앱에서 링크를 빠르게 저장할 수 있습니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onStartOverlay()
                        },
                        modifier = Modifier.fillMaxWidth()) {
                        Text("오버레이 시작")
                    }
                }
            }

            // 새로운 기능 섹션 추가
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "제스처 트래킹",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "브라우저에서 제스처를 추적합니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // 제스처 트래킹 시작 로직
                            startGestureTrackingService(context)
                        },
                        modifier = Modifier.fillMaxWidth()) {
                        Text("제스처 트래킹 시작")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 기존 설정 리스트
            Column {
                SettingItem(title = "알림 설정") { /* TODO */ }
                SettingItem(title = "계정 관리") { /* TODO */ }
                SettingItem(title = "앱 정보") { /* TODO */ }
                SettingItem(title = "로그아웃") { /* TODO */ }
            }
        }
    }
}

// 앱 종료 함수
fun exitApp(context: Context) {
    (context as? Activity)?.finish()
    // 또는 시스템 전체 앱 종료
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    activityManager.killBackgroundProcesses(context.packageName)
}

// 제스처 트래킹 서비스 시작 함수
fun startGestureTrackingService(context: Context) {
    val serviceIntent = Intent(context, GestureTrackingService::class.java)
    context.startService(serviceIntent)
}
@Composable
fun SettingItem(title: String, onClick: () -> Unit) {
    // 기존 코드 유지
}