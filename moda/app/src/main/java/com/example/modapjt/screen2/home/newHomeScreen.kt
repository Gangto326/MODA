
// ... 기존 imports는 유지
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.HeaderBar
import com.example.modapjt.components.bar.SearchBar
import com.example.modapjt.components.home.CategoryList
import com.example.modapjt.components.home.ThumbnailSlider
import com.example.modapjt.components.home.section.ForgottenContentSection
import com.example.modapjt.components.home.section.ImageSection
import com.example.modapjt.components.home.section.TodayContentSection
import com.example.modapjt.components.home.section.VideoSection
import com.example.modapjt.components.home.section.WeeklyKeywordSection
import com.example.modapjt.domain.viewmodel.AuthViewModel
import com.example.modapjt.domain.viewmodel.CategoryViewModel
import com.example.modapjt.domain.viewmodel.SearchViewModel


@Composable
fun newHomeScreen(
    navController: NavController,
    currentRoute: String,
    homeKeywordViewModel: SearchViewModel = viewModel(),
    authViewModel: AuthViewModel // 추가
) {
    // 추가 : 로그인 상태 확인
    val isLoggedIn by authViewModel.isLoggedIn

    // 로그인 되지 않은 경우 로그인 화면으로 이동
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    val listState = rememberLazyListState()
    var isHeaderVisible by remember { mutableStateOf(true) }
    var lastScrollOffset by remember { mutableStateOf(0) }
    val categoryViewModel: CategoryViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()

    // 🔹 API에서 받아올 creator 값 저장
    val creator by homeKeywordViewModel.creator.collectAsState()

    LaunchedEffect(Unit) {
        homeKeywordViewModel.fetchHomeKeywords() // userId 전달
    }


    val headerOffsetY by animateDpAsState(
        targetValue = if (isHeaderVisible) 0.dp else (-60).dp,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "Header Animation"
    )

    val headerAlpha by animateFloatAsState(
        targetValue = if (isHeaderVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "Header Alpha"
    )

    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset, ) {
            val currentOffset = listState.firstVisibleItemScrollOffset
            val isScrollingDown = currentOffset > lastScrollOffset

            isHeaderVisible = if (listState.firstVisibleItemIndex == 0) {
                true
            } else {
                !isScrollingDown
            }

        lastScrollOffset = currentOffset
    }

    Scaffold(
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .offset(y = headerOffsetY)
                        .alpha(headerAlpha)
                ) {
                    HeaderBar(modifier = Modifier)
                }
            }

            item {
                Spacer(modifier = Modifier.height(3.dp))
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ThumbnailSlider(viewModel = searchViewModel, navController = navController)
                Spacer(modifier = Modifier.height(16.dp))
            }


//            item {
//                CategoryList(navController = navController, viewModel = categoryViewModel, homeKeywordViewModel = homeKeywordViewModel)
//            }
            item {
                CategoryList(navController = navController, viewModel = categoryViewModel)
            }


            item {
                WeeklyKeywordSection(
                    homeKeywordViewModel = homeKeywordViewModel,
                    navController = navController,
                    searchViewModel = searchViewModel
                )
            }


            item {
                TodayContentSection(
                    navController = navController,
                    searchViewModel = searchViewModel
                )
            }

            item {
                Image(
                    painter = painterResource(id = R.drawable.overlayad),
                    contentDescription = "광고 이미지",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                VideoSection(
                    navController = navController,
                    homeKeywordViewModel = homeKeywordViewModel,
                    searchViewModel = searchViewModel
                )
            }


            item {
                ImageSection(
                    navController = navController,
                    searchViewModel = searchViewModel
                )
            }


            item {
                ForgottenContentSection(
                    navController = navController,
                    searchViewModel = searchViewModel
                )
            }
        }
    }
}
