import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.bar.SearchBar
import com.example.modapjt.components.home.BottomThumbnail
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.home.CategoryList
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.components.home.ThumbnailSlider
import com.example.modapjt.components.home.WeeklyKeyword
import com.example.modapjt.domain.viewmodel.CategoryViewModel

@Composable
fun newHomeScreen(
    navController: NavController,
    currentRoute: String,
) {
    val listState = rememberLazyListState()
    var isHeaderVisible by remember { mutableStateOf(true) }
    var lastScrollOffset by remember { mutableStateOf(0) }
    val viewModel: CategoryViewModel = viewModel() // ✅ 올바른 ViewModel 인스턴스 선언

    // 부드러운 애니메이션을 위한 transitionY 값
    val headerOffsetY by animateDpAsState(
        targetValue = if (isHeaderVisible) 0.dp else (-60).dp, // 헤더 높이에 맞춰 조정
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "Header Animation"
    )

    // 투명도 조절하기 위해
    val headerAlpha by animateFloatAsState(
        targetValue = if (isHeaderVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "Header Alpha"
    )


    // 스크롤 방향 감지
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
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .offset(y = headerOffsetY) // 애니메이션 적용
                    .alpha(headerAlpha) // 투명도 조절
            ) {
                HeaderBar()
            }
        },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Spacer(modifier = Modifier.height(3.dp))
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                ThumbnailSlider()
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                CategoryList(navController = navController, viewModel = viewModel)
            }

            item {
                HomeSmallTitle(
                    title = "이번주 주요 키워드",
                    description = "| 이번주 사용자가 많이 저장한 키워드"
                )
            }

            item {
                val keywords = listOf("운동", "다이어트", "헬스", "근력", "유산소", "식단", "스트레칭")

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (keyword in keywords) {
                            WeeklyKeyword(keyword)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                HomeSmallTitle(
                    title = "오늘의 컨텐츠",
                    description = " | 해당 컨텐츠에 대한 설명"
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val thumbnails = listOf(
                        Color(0xFFFFCDD2),
                        Color(0xFFC8E6C9),
                        Color(0xFFBBDEFB),
                        Color(0xFFFFF9C4),
                        Color(0xFFD1C4E9)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (color in thumbnails) {
                            BottomThumbnail(backgroundColor = color)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
