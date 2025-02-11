package com.example.modapjt.navigation

//import com.google.accompanist.navigation.animation.AnimatedNavHost
//import com.google.accompanist.navigation.animation.composable
//import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.domain.viewmodel.AuthViewModel
import com.example.modapjt.screen.SavedUrlsScreen
import com.example.modapjt.screen.linkupload.LinkUploadScreen
import com.example.modapjt.screen.recommend.RecommendScreen
import com.example.modapjt.screen.settings.SettingsScreen
import com.example.modapjt.screen2.auth.LoginScreen
import com.example.modapjt.screen2.auth.SignUpScreen
import com.example.modapjt.screen2.newCardListScreen
import com.example.modapjt.screen2.newSearchCardListScreen
import com.example.modapjt.screen2.search.NewSearchScreen
import com.example.modapjt.screen2.user.MyPageScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import newCardDetailScreen
import newHomeScreen
import newLinkUploadScreen

//
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun NavGraph(
//    navController: NavHostController,
//    onStartOverlay: () -> Unit
//) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route ?: "home"
//
//    AnimatedNavHost(
//        navController = navController,
//        startDestination = "home"
//    ) {
//        composable(
//            route = "home",
//            exitTransition = {
//                // 홈 화면이 사라질 때의 애니메이션
//                fadeOut(animationSpec = tween(300))
//            }
//        ) {
//            newHomeScreen(
//                navController = navController,
//                currentRoute = currentRoute
//            )
//        }
//
//        composable(
//            route = "search",
//            enterTransition = {
//                // 검색 화면이 나타날 때의 애니메이션
//                slideInVertically(
//                    initialOffsetY = { -it },  // 위에서 아래로
//                    animationSpec = tween(300, easing = FastOutSlowInEasing)
//                ) + fadeIn(animationSpec = tween(300))
//            },
//            exitTransition = {
//                // 검색 화면이 사라질 때의 애니메이션
//                slideOutVertically(
//                    targetOffsetY = { -it },  // 아래에서 위로
//                    animationSpec = tween(300, easing = FastOutSlowInEasing)
//                ) + fadeOut(animationSpec = tween(300))
//            }
//        ) {
//            NewSearchScreen(navController = navController)
//        }
//
//
//        // 설정 화면
//        composable("settings") {
//            SettingsScreen(
//                navController = navController,
//                onStartOverlay = onStartOverlay
//            )
//        }
//
//        // 추천 화면
//        composable("recommend") {
//            RecommendScreen(
//                navController = navController,
//                currentRoute = currentRoute
//            )
//        }
//
//        // 링크 업로드 화면
//        composable("link_upload") {
//            LinkUploadScreen(navController, currentRoute = "link_upload")
//        }
//
//        // 저장된 URL 화면
//        composable("saved_urls") {
//            SavedUrlsScreen()
//        }
//
//        // 카드리스트테스트 화면
//        composable("card_list_test") {
//            newCardListScreen(
//                navController = navController,
//                currentRoute = "card_list_test",
//                categoryId = null
//            )
//        }
//
//        // 카테고리 상세 화면
//        composable("categoryDetail/{categoryId}") { backStackEntry ->
//            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
//            newCardListScreen(
//                navController = navController,
//                currentRoute = "categoryDetail",
//                categoryId = categoryId
//            )
//        }
//
//        // 파일업로드테스트 화면
//        composable("file_upload_test") {
//            newLinkUploadScreen(navController, currentRoute = "file_upload_test")
//        }
//
//        // 카드상세페이지 화면
//        composable("card_detail_test") {
//            newCardDetailScreen(navController, currentRoute = "card_detail_test")
//        }
//
//        // 마이페이지 화면 추가
//        composable("mypage/{userId}") { backStackEntry ->
//            val userId = backStackEntry.arguments?.getString("userId") ?: "user"
//            val currentRoute = "mypage"
//            MyPageScreen(userId = userId, navController = navController, currentRoute = currentRoute)
//        }
//
//    }
//}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberAnimatedNavController(), // AnimatedNavController 사용
    onStartOverlay: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"
    var isOverlayActive by remember { mutableStateOf(false) } // 오버레이 상태 추가

    // AuthViewModel 인스턴스 생성
    val authViewModel: AuthViewModel = viewModel()

    val repository = CardRepository() // CardRepository 인스턴스 생성


    AnimatedNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            newHomeScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable("search") {
            NewSearchScreen(navController = navController)
        }

        // 설정 화면 (오버레이 활성화 여부 전달)
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onStartOverlay = { isActive ->
                    isOverlayActive = isActive
                }
            )
        }

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

        composable("card_list_test") {
            newCardListScreen(
                navController = navController,
                currentRoute = "card_list_test",
                categoryId = null
            )
        }

        composable("categoryDetail/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
            newCardListScreen(
                navController = navController,
                currentRoute = "categoryDetail",
                categoryId = categoryId
            )
        }

        composable("file_upload_test") {
            newLinkUploadScreen(navController, currentRoute = "file_upload_test", repository = repository)
        }

//        composable("card_detail_test") {
//            newCardDetailScreen(navController, currentRoute = "card_detail_test")
//        }

        composable("mypage/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "user"
            MyPageScreen(userId = userId, navController = navController, currentRoute = "mypage")
        }


        // ✅✅추가
        composable(route = "newSearchCardListScreen/{searchQuery}") { navBackStackEntry ->
            val searchQuery = navBackStackEntry.arguments?.getString("searchQuery") ?: ""
            newSearchCardListScreen(
                navController = navController,
                currentRoute = "newSearchCardListScreen",
                initialQuery = searchQuery  // ✅ 검색어를 전달받아 사용
            )
        }

        composable(
            route = "cardDetail/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: "Unknown"
            newCardDetailScreen(navController, currentRoute = "cardDetail", cardId)
        }



        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate("signup") },
                onNavigateToHome = { navController.navigate("home") } // 로그인 성공 시 홈 이동
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }


//        composable("favorite_card_list") {
//            newFavoriteCardListScreen(
//                navController = navController,
//                currentRoute = "favorite_card_list"
//            )
//        }

    }
}
