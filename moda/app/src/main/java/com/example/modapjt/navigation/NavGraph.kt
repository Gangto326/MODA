package com.example.modapjt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modapjt.screen.home.HomeScreen
import com.example.modapjt.screen.board.BoardScreen
import com.example.modapjt.screen.card.CardScreen
import com.example.modapjt.screen.settings.SettingsScreen
import com.example.modapjt.screen.recommend.RecommendScreen

import androidx.navigation.NavHostController  // NavHostController 임포트 추가
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.modapjt.screen.friend.FriendScreen
import com.example.modapjt.screen.linkupload.LinkUploadScreen

// NavGraph.kt (네비게이션 관리) : 앱의 화면 간 이동을 관리하는 파일
// 네비게이션 그래프를 정의하는 Composable 함수
// NavController : Jetpack Compose에서 화면 이동을 관리하는 객체
@Composable
fun NavGraph(navController: NavHostController) {
    // 현재 네비게이션 경로 추적 (어떤 화면이 활성화되어 있는지 확인)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"


    // NavHost 설정 : 네비게이션의 루트 컨테이너 (앱이 처음 실행될 때 'home'화면이 기본적으로 표시)
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // 홈 화면
        composable("home") { HomeScreen(
            navController = navController,
            currentRoute = currentRoute
        ) }
        // 추천 알고리즘 화면
        composable("recommend") {
            RecommendScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        // 보드 화면 (특정 보드 선택 시 이동)
        composable("board/{boardId}") { backStackEntry ->
            val boardId = backStackEntry.arguments?.getString("boardId")
            BoardScreen(boardId = boardId, navController = navController, currentRoute = currentRoute)
        }

        // 카드(컨텐츠) 상세 화면 (특정 카드 선택 시 이동)
        composable("card/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId")
            CardScreen(cardId = cardId, navController = navController, currentRoute = "card")
        }

        // 링크 업로드 화면
        composable("link_upload") { LinkUploadScreen(navController, currentRoute = "link_upload") } // ✅ 추가

        // 설정 화면
        composable("settings") { SettingsScreen(navController = navController) }

        // 팔로워/팔로잉 리스트 화면
        composable("friends/{tab}") { backStackEntry ->
            val tab = backStackEntry.arguments?.getString("tab") ?: "following"
            FriendScreen(navController, selectedTab = tab)
        }


    }
}