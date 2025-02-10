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
//import com.example.modapjt.components.setting.MyTopAppBar
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SettingsScreen(
//    navController: NavController,
//    currentRoute: String = "settings",
//    onStartOverlay: () -> Unit  // 오버레이 콜백 추가
//) {
//    Scaffold(
//        topBar = { MyTopAppBar(
//            title = "설정",
//            onBackClick = { navController.popBackStack() }
//        ) },
//        bottomBar = { BottomBarComponent(navController, currentRoute) }
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
//            // 오버레이 설정 섹션 추가
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "링크 저장 기능",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "다른 앱에서 링크를 빠르게 저장할 수 있습니다.",
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(
//                        onClick = onStartOverlay,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("오버레이 시작")
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 기존 설정 리스트
//            Column {
//                SettingItem(title = "알림 설정") { /* TODO */ }
//                SettingItem(title = "계정 관리") { /* TODO */ }
//                SettingItem(title = "앱 정보") { /* TODO */ }
//                SettingItem(title = "로그아웃") { /* TODO */ }
//            }
//        }
//    }
//}
//
//@Composable
//fun SettingItem(title: String, onClick: () -> Unit) {
//    // 기존 코드 유지
//}

package com.example.modapjt.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
//import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.setting.SettingHeader
import com.example.modapjt.components.setting.SettingItem

@Composable
fun SettingsScreen(
    navController: NavController,
    currentRoute: String = "settings",
    onStartOverlay: () -> Unit  // 오버레이 콜백 추가
) {
    Scaffold(
        topBar = {
            SettingHeader(
                title = "설정",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    Button(
                        onClick = onStartOverlay,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC80))
                    ) {
                        Text("오버레이 시작", color = Color.Black)
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
