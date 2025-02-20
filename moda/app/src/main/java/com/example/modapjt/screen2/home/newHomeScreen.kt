
// ... 기존 imports는 유지
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
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
import com.airbnb.lottie.compose.*
import okhttp3.internal.wait


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

    val searchData by searchViewModel.searchData.collectAsState()

    val topKeywords by homeKeywordViewModel.topKeywords.collectAsState()
    val selectedKeyword by homeKeywordViewModel.selectedKeyword.collectAsState()
    val keywordSearchData by searchViewModel.keywordSearchData.collectAsState()
    var sliderIndex by remember { mutableStateOf(0) }

    val thumbnails = remember(searchData?.thumbnails) {
        searchData?.thumbnails ?: emptyList()
    }

    LaunchedEffect(Unit) {
        homeKeywordViewModel.fetchHomeKeywords() // userId 전달
        searchViewModel.loadSearchData()
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
                .background(MaterialTheme.colorScheme.tertiary) // 배경
                .padding(paddingValues)
        ) {

            // 검색바 상단에 로고 배치
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .clipToBounds() // 🔥 배경 영역을 벗어나지 않도록 자름
                    .height(70.dp), // 로고 크기에 맞춰 조절
                    contentAlignment = Alignment.Center // 로고 가운데 정렬
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("logo.json"))
                    val progress by animateLottieCompositionAsState(
                        composition,
                        iterations = LottieConstants.IterateForever)

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier
                            .size(120.dp) // 원하는 크기로 조절
//                            .padding(bottom = (-10).dp)
                            .offset(y = 1.dp) // 🔥 아이콘을 아래로 10dp 이동

                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(3.dp))
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, top = 8.dp),
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ThumbnailSlider(
                    thumbnails = searchData?.thumbnails,
                    navController = navController,
                    onLoadData = { searchViewModel.loadSearchData() }
//                    viewModel = searchViewModel,
//                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                CategoryList(navController = navController, viewModel = categoryViewModel, homeKeywordViewModel = homeKeywordViewModel)
            }


            item {
                WeeklyKeywordSection(
                    topKeywords = topKeywords,
                    selectedKeyword = selectedKeyword,
                    keywordSearchData = keywordSearchData,
                    onKeywordSelected = { keyword ->
                        homeKeywordViewModel.updateKeywordAndFetchData(keyword)
                    },
                    navController = navController
                )
            }


            item {
                TodayContentSection(
                    navController = navController,
                    todayContent = searchData?.todays.orEmpty()
                )
            }

            item {
                Image(
                    painter = painterResource(id = R.drawable.newoverlay),
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
                    videos = searchData?.videos.orEmpty(),
                    creator = creator,
                    navController = navController
                )
            }


            item {
                ImageSection(
                    images = searchData?.images.orEmpty(),
                    navController = navController
                )
            }


            item {
                ForgottenContentSection(
                    forgottenContents = searchData?.forgotten.orEmpty(),
                    navController = navController
                )
            }
        }
    }
}
