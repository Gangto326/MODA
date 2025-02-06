import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.components.bar.SearchBar
import com.example.modapjt.components.home.BottomThumbnail
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.home.CategoryList
import com.example.modapjt.components.home.HomeSmallTitle
import com.example.modapjt.components.home.ThumbnailSlider
import com.example.modapjt.components.home.WeeklyKeyword

@Composable
fun newHomeScreen(
    navController: NavController,
    currentRoute: String
) {
    val listState = rememberLazyListState()
    var isHeaderVisible by remember { mutableStateOf(true) }

    // 스크롤 방향 감지
    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        isHeaderVisible = listState.firstVisibleItemScrollOffset == 0
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = isHeaderVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                HeaderBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
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
                val categories = listOf(
                    "전체", "어쩌구1", "어쩌구2", "어쩌구3", "어쩌구4",
                    "어쩌구5", "어쩌구6", "어쩌구7", "어쩌구8", "어쩌구9"
                )

                CategoryList(categories, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) // 패딩 추가
            }

            item{
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

            item { HomeSmallTitle(
                title = "오늘의 컨텐츠",
                description = " | 해당 컨텐츠에 대한 설명"
            ) }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ){
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
