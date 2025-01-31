package com.example.modapjt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.modapjt.ui.theme.ModapjtTheme
import com.example.modapjt.navigation.NavGraph


// MainActivity: 앱의 진입점 (최초 실행되는 화면)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Jetpack Compose 기반 UI 설정
        setContent {
            // 앱의 테마 적용 (ModapjtTheme 안에서 MaterialTheme이 적용됨)
            ModapjtTheme {
                // 네비게이션 컨트롤러 생성 (화면 전환을 관리)
                val navController = rememberNavController()
                // 네비게이션 그래프 설정 (화면 간 이동을 정의한 NavGraph 연결)
                NavGraph(navController = navController)
            }
        }
    }
}



//Unresolve reference: navigation, rememberNavController