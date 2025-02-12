
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.HeaderBar
import com.example.modapjt.components.bar.SearchBar
import com.example.modapjt.components.home.BottomThumbnailList
import com.example.modapjt.components.home.CategoryList
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.components.home.KeywordList
import com.example.modapjt.components.home.ThumbnailSlider
import com.example.modapjt.domain.viewmodel.CategoryViewModel
import com.example.modapjt.domain.viewmodel.SearchViewModel

@Composable
fun newHomeScreen(
    navController: NavController,
    currentRoute: String,

) {
    val listState = rememberLazyListState()
    var isHeaderVisible by remember { mutableStateOf(true) }
    var lastScrollOffset by remember { mutableStateOf(0) }
    val categoryViewModel: CategoryViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()


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

    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
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
            // 기존 `topBar`에서 제거된 헤더를 리스트의 첫 번째 `item`으로 추가
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
                ThumbnailSlider(viewModel = searchViewModel, navController = navController, userId = "user123")
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Divider(color = Color(0xFFDCDCDC), thickness = 4.dp, modifier = Modifier.padding(horizontal = 0.dp))
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                CategoryList(navController = navController, viewModel = categoryViewModel)
            }

            item {
                Divider(color = Color(0xFFDCDCDC), thickness = 4.dp, modifier = Modifier.padding(horizontal = 0.dp))
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                HomeSmallTitle(
                    title = "이번주 주요 키워드",
                    description = "| 이번주 사용자가 많이 저장한 키워드"
                )
            }



            item {
                KeywordList()
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Divider(color = Color(0xFFDCDCDC), thickness = 4.dp, modifier = Modifier.padding(horizontal = 0.dp))
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                HomeSmallTitle(
                    title = "오늘의 컨텐츠",
                    description = " | 해당 컨텐츠에 대한 설명"
                )
            }

            item {
                BottomThumbnailList()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 이미지 추가
            item {
                Image(
                    painter = painterResource(id = R.drawable.overlayad),
                    contentDescription = "광고 이미지",
                    contentScale = ContentScale.FillWidth, // 가로 너비에 맞추기
                    modifier = Modifier
                        .fillMaxWidth() // 가로 전체 채우기
                        .height(80.dp) // 원본 높이 유지
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                HomeSmallTitle(
                    title = "여유로운 토요일 저녁",
                    description = ""
                )
            }

            item {
                Divider(color = Color(0xFFDCDCDC), thickness = 4.dp, modifier = Modifier.padding(horizontal = 0.dp))
                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}
