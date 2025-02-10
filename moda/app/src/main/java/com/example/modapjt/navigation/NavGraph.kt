package com.example.modapjt.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.modapjt.screen.SavedUrlsScreen
import com.example.modapjt.screen.linkupload.LinkUploadScreen
import com.example.modapjt.screen.recommend.RecommendScreen
import com.example.modapjt.screen.settings.SettingsScreen
import com.example.modapjt.screen2.newCardListScreen
import com.example.modapjt.screen2.search.NewSearchScreen
import com.example.modapjt.screen2.user.MyPageScreen
import newCardDetailScreen
import newHomeScreen
import newLinkUploadScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    onStartOverlay: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    AnimatedNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(
            route = "home",
            exitTransition = {
                // 홈 화면이 사라질 때의 애니메이션
                fadeOut(animationSpec = tween(300))
            }
        ) {
            newHomeScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable(
            route = "search",
            enterTransition = {
                // 검색 화면이 나타날 때의 애니메이션
                slideInVertically(
                    initialOffsetY = { -it },  // 위에서 아래로
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                // 검색 화면이 사라질 때의 애니메이션
                slideOutVertically(
                    targetOffsetY = { -it },  // 아래에서 위로
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            NewSearchScreen(navController = navController)
        }


        // 설정 화면
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onStartOverlay = onStartOverlay
            )
        }

        // 추천 화면
        composable("recommend") {
            RecommendScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        // 링크 업로드 화면
        composable("link_upload") {
            LinkUploadScreen(navController, currentRoute = "link_upload")
        }

        // 저장된 URL 화면
        composable("saved_urls") {
            SavedUrlsScreen()
        }

        // 카드리스트테스트 화면
        composable("card_list_test") {
            newCardListScreen(
                navController = navController,
                currentRoute = "card_list_test",
                categoryId = null
            )
        }

        // 카테고리 상세 화면
        composable("categoryDetail/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
            newCardListScreen(
                navController = navController,
                currentRoute = "categoryDetail",
                categoryId = categoryId
            )
        }

        // 파일업로드테스트 화면
        composable("file_upload_test") {
            newLinkUploadScreen(navController, currentRoute = "file_upload_test")
        }

        // 카드상세페이지 화면
        composable("card_detail_test") {
            newCardDetailScreen(navController, currentRoute = "card_detail_test")
        }

        // 마이페이지 화면 추가
        composable("mypage/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "user"
            val currentRoute = "mypage"
            MyPageScreen(userId = userId, navController = navController, currentRoute = currentRoute)
        }

    }
}