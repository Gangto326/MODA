package com.example.modapjt.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modapjt.screen.home.HomeScreen
//import com.example.modapjt.screen.board.BoardScreen
//import com.example.modapjt.screen.card.CardScreen
import com.example.modapjt.screen.settings.SettingsScreen
import com.example.modapjt.screen.recommend.RecommendScreen

import androidx.navigation.NavHostController  // NavHostController 임포트 추가
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.modapjt.screen.SavedUrlsScreen
import com.example.modapjt.screen.linkupload.LinkUploadScreen
import com.example.modapjt.screen2.newCardListScreen
import newCardDetailScreen
import newHomeScreen
import newLinkUploadScreen
import newSearchScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    onStartOverlay: () -> Unit  // 오버레이 시작 콜백 추가
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // 홈 화면
        composable("home") {
            HomeScreen(
                navController = navController,
                currentRoute = currentRoute,
                onStartOverlay = onStartOverlay  // 홈 화면에 콜백 전달
            )
        }

        // 설정 화면
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onStartOverlay = onStartOverlay  // 설정 화면에도 콜백 전달
            )
        }

        // 나머지 composable들은 그대로 유지
        composable("recommend") {
            RecommendScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable("link_upload") {
            LinkUploadScreen(navController, currentRoute = "link_upload")
        }


        composable("saved_urls") {
            SavedUrlsScreen()
        }



//         "홈테스트" 화면 추가
        composable("home_test") {
            newHomeScreen(navController, currentRoute = "home_test")
        }

        // "카드리스트테스트" 화면 추가 -> 검색창 임시 변환
        composable("card_list_test") {
            newSearchScreen(navController, currentRoute = "card_list_test")
        }


        composable("categoryDetail/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
            newCardListScreen(navController = navController, currentRoute = "categoryDetail", categoryId = categoryId)
        }

        // "파일업로드테스트" 화면 추가
        composable("file_upload_test") {
            newLinkUploadScreen(navController, currentRoute = "file_upload_test")
        }

        // "카드상세페이지" 화면 추가
        composable("card_detail_test") {
            newCardDetailScreen(navController, currentRoute = "card_detail_test")
        }
    }
}