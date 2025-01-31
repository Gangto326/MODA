package com.example.modapjt.screen.recommend

//import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavController

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TopBarComponent


//@Composable
//fun RecommendScreen(navController: NavController) {
//    // 공유 화면 기본 UI
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text(text = "Recommend Screen", modifier = Modifier.padding(bottom = 16.dp))
//        // 공유 관련 기능을 여기에 추가할 수 있음 (예: 콘텐츠 공유 버튼)
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendScreen(navController: NavController, currentRoute: String) {
    Scaffold(
        topBar = { TopBarComponent() },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "Recommend Screen Page")
        }
    }
}