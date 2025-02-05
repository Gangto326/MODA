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
import com.example.modapjt.screen.SavedUrlsScreen
import com.example.modapjt.screen.linkupload.LinkUploadScreen

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

        composable("board/{boardId}") { backStackEntry ->
            val boardId = backStackEntry.arguments?.getString("boardId")
            BoardScreen(boardId = boardId, navController = navController, currentRoute = currentRoute)
        }

        composable("card/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId")
            CardScreen(cardId = cardId, navController = navController, currentRoute = "card")
        }

        composable("link_upload") {
            LinkUploadScreen(navController, currentRoute = "link_upload")
        }


        composable("saved_urls") {
            SavedUrlsScreen()
        }
    }
}