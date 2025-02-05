import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.modapjt.components.bar.SearchBar
import com.example.modapjt.components.home.TopThumbnail
import com.example.modapjt.components.home.BottomThumbnail
import com.example.modapjt.components.bar.BottomBar
import com.example.modapjt.components.home.CategoryItem

@Composable
fun HomeScreen() {
    val listState = rememberLazyListState()
    var isHeaderVisible by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    // 스크롤 방향 감지
    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        if (listState.firstVisibleItemScrollOffset > 0) {
            isHeaderVisible = false  // 아래로 스크롤 시 헤더 숨기기
        } else {
            isHeaderVisible = true   // 위로 스크롤 시 헤더 보이기
        }
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
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)  // 하단 바 높이 조정
                    .background(Color(0xFFFFF9C4))  // 연한 노란색 배경
            ) {
                BottomBar()  // 하단 바 내용
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))  // 상단 여백
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)  // 좌우 여백 추가
                )
                Spacer(modifier = Modifier.height(16.dp))  // 하단 여백
            }
            item {
                TopThumbnail()
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                val categories = listOf(
                    "전체", "어쩌구1", "어쩌구2", "어쩌구3", "어쩌구4",
                    "어쩌구5", "어쩌구6", "어쩌구7", "어쩌구8", "어쩌구9"
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)  // 전체 가로 패딩 추가
                ) {
                    // 2행 반복
                    for (row in 0 until 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween  // 아이템을 균등하게 배치
                        ) {
                            // 5열 반복
                            for (col in 0 until 5) {
                                val index = row * 5 + col
                                if (index < categories.size) {
                                    CategoryItem(name = categories[index])  // 카테고리 아이템 호출
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))  // 행 간격
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))  // 카테고리 아래 여백
            }


            item {
                val keywords = listOf("운동", "다이어트", "헬스", "근력", "유산소", "식단", "스트레칭")

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)  // 좌우 패딩 추가
                ) {
                    // 상단 타이틀
                    Text(
                        text = "이번주 주요 키워드",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))  // 타이틀과 키워드 간격

                    // 키워드 리스트 (수평 스크롤 가능)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),  // 가로 스크롤 가능하게 설정
                        horizontalArrangement = Arrangement.spacedBy(8.dp)  // 키워드 간 간격
                    ) {
                        for (keyword in keywords) {
                            WeeklyKeyword(keyword)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))  // 위클리 키워드 아래 여백
            }

            item {
                val thumbnails = listOf(
                    Color(0xFFFFCDD2),  // 연한 빨강
                    Color(0xFFC8E6C9),  // 연한 초록
                    Color(0xFFBBDEFB),  // 연한 파랑
                    Color(0xFFFFF9C4),  // 연한 노랑
                    Color(0xFFD1C4E9)   // 연한 보라
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)  // 좌우 패딩
                ) {
                    // 상단 타이틀
                    Text(
                        text = "오늘의 컨텐츠",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))  // 타이틀과 썸네일 간격

                    // 썸네일 리스트 (수평 스크롤 가능)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),  // 가로 스크롤 가능
                        horizontalArrangement = Arrangement.spacedBy(12.dp)  // 썸네일 간 간격
                    ) {
                        for (color in thumbnails) {
                            BottomThumbnail(backgroundColor = color)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))  // 하단 썸네일 아래 여백
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}
