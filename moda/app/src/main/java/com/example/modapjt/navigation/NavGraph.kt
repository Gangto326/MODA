package com.example.modapjt.navigation

//import com.google.accompanist.navigation.animation.AnimatedNavHost
//import com.google.accompanist.navigation.animation.composable
//import com.google.accompanist.navigation.animation.rememberAnimatedNavController
//import newCardListScreen
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.modapjt.screen2.auth.FindIdScreen
import com.example.modapjt.screen2.auth.FindPasswordScreen
import com.example.modapjt.screen2.auth.LoginScreen
import com.example.modapjt.screen2.auth.SignUpScreen
import com.example.modapjt.screen2.carddetail.newCardDetailScreen
import com.example.modapjt.screen2.cardlist.newSearchCardListScreen
import com.example.modapjt.screen2.newBookMarkCardListScreen
import com.example.modapjt.screen2.newCardListScreen
import com.example.modapjt.screen2.search.NewSearchScreen
import com.example.modapjt.screen2.user.MyPageScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import newHomeScreen
import newLinkUploadScreen


@RequiresApi(Build.VERSION_CODES.O)
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

        composable("mypage") { backStackEntry ->
//            val userId = backStackEntry.arguments?.getString("userId") ?: "user"
            MyPageScreen(navController = navController, currentRoute = "mypage")
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

        // NavGraph.kt에서
        composable(
            route = "cardDetail/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: "Unknown"
            newCardDetailScreen(
                navController = navController,
                currentRoute = "cardDetail",
                cardId = cardId
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate("signup") },
                onNavigateToHome = { navController.navigate("home") },
                onNavigateToFindId = { navController.navigate("find_id") },
                onNavigateToFindPassword = { navController.navigate("find_password") }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // 아이디 찾기 화면
        composable("find_id") {
            FindIdScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // 비밀번호 찾기 화면
        composable("find_password") {
            FindPasswordScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // 즐겨찾기 카드 페이지
        composable("bookmarkScreen") {
            newBookMarkCardListScreen(navController = navController, currentRoute = "bookmarkScreen")
        }

//        composable("favorite_card_list") {
//            newFavoriteCardListScreen(
//                navController = navController,
//                currentRoute = "favorite_card_list"
//            )
//        }

    }
}
