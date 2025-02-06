import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.CategoryHeaderBar


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow

//@Composable
//fun newCardListScreen(navController: NavController, currentRoute: String) {
//    Scaffold(
//        topBar = { CategoryHeaderBar() },
//        bottomBar = { BottomBarComponent(navController, currentRoute) }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            Column {
//                TypeSelectBar(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    onCategorySelected = { category ->
//                        // 여기서 카테고리 선택 처리
//                        println("Selected category: $category")
//                    }
//                )
//
//                AllTabCard(
//                    onImageMoreClick = {
//                        // 이미지 더보기 클릭 시 이미지 탭으로 이동
//                        println("Image more clicked")
//                    },
//                    onImageClick = { index ->
//                        // 개별 이미지 클릭 시 상세 페이지로 이동
////                        navController.navigate("imageDetail/$index")
//                        println("개별 이미지 클릭 시 상세 페이지로 이동")
//                    },
//                    onVideoMoreClick = {
//                        // 동영상 더보기 클릭 시 동영상 탭으로 이동
//                        println("Video more clicked")
//                    },
//                    onBlogMoreClick = {
//                        // 블로그 더보기 클릭 시 블로그 탭으로 이동
//                        println("Blog more clicked")
//                    },
//                    onNewsMoreClick = {
//                        // 뉴스 더보기 클릭 시 뉴스 탭으로 이동
//                        println("News more clicked")
//                    }
//                )
//            }
//        }
//    }
//}




@Composable
fun newCardListScreen(navController: NavController, currentRoute: String) {
    val lazyListState = rememberLazyListState()
    var isTypeBarVisible by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf("전체") } // ✅ 선택된 카테고리 상태 추가

    // ✅ 스크롤 상태 감지하여 TypeSelectBar 표시 여부 조절
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
            .collect { offset ->
                isTypeBarVisible = offset <= 0
            }
    }

    Scaffold(
        topBar = { CategoryHeaderBar() },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = lazyListState,
//                modifier = Modifier.fillMaxSize()

                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {} // ✅ 클릭 이벤트 방해 방지
            ) {
                // ✅ TypeSelectBar (스크롤에 따라 나타났다 사라짐)
                item {
                    AnimatedVisibility(
                        visible = isTypeBarVisible,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        TypeSelectBar(
                            selectedCategory = selectedCategory, // ✅ 선택된 카테고리 전달
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            onCategorySelected = { category ->
                                selectedCategory = category // ✅ 선택된 카테고리 업데이트
                            }
                        )
                    }
                }

                // ✅ 선택된 카테고리에 따라 다른 콘텐츠 표시 (이미지를 LazyColumn에서 관리)
                when (selectedCategory) {
                    "전체" -> {
                        item {
                            AllTabCard(
                                onImageMoreClick = { println("Image more clicked") },
                                onImageClick = { index -> println("개별 이미지 클릭 시 상세 페이지로 이동") },
                                onVideoMoreClick = { println("Video more clicked") },
                                onBlogMoreClick = { println("Blog more clicked") },
                                onNewsMoreClick = { println("News more clicked") }
                            )
                        }
                    }
                    "이미지" -> {
                        items(30) { index -> // LazyColumn에서 직접 이미지 아이템을 추가
                            ImageBig(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        println("이미지 $index 클릭됨")
//                                        navController.navigate("imageDetail/$index") // 클릭 시 상세 페이지 이동 가능
                                    }
                            )
                        }
                    }
                    "동영상" -> {
                        items(30) { index -> // LazyColumn에서 직접 이미지 아이템을 추가
                            VideoBig(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        println("동영상 $index 클릭됨")
//                                        navController.navigate("imageDetail/$index") // 클릭 시 상세 페이지 이동 가능
                                    }
                            )
                        }
                    }
                    "블로그" -> {
                        items(30) { index -> // LazyColumn에서 직접 이미지 아이템을 추가
                            BlogBig(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        println("블로그 $index 클릭됨")
//                                        navController.navigate("imageDetail/$index") // 클릭 시 상세 페이지 이동 가능
                                    }
                            )
                        }
                    }
                    "뉴스" -> {
                        items(30) { index -> // LazyColumn에서 직접 이미지 아이템을 추가
                            NewsBig(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
//                                    .clickable {
//                                        println("뉴스 $index 클릭됨")
////                                        navController.navigate("imageDetail/$index") // 클릭 시 상세 페이지 이동 가능
//                                    }
                                        onClick = {
                                    println("뉴스 $index 클릭됨") // ✅ 클릭 이벤트 정상 실행 확인
                                }
                            )
                        }
                    }
//                    "동영상" -> item { VideoTabCard(navController) }
//                    "블로그" -> item { BlogTabCard(navController) }
//                    "뉴스" -> item { NewsTabCard(navController) }

                }
            }
        }
    }
}
