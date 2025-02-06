import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Composable
//fun AllTabCard(
//    onImageMoreClick: () -> Unit = {},
//    onVideoMoreClick: () -> Unit = {},
//    onBlogMoreClick: () -> Unit = {},
//    onNewsMoreClick: () -> Unit = {},
//    onImageClick: (Int) -> Unit = {}  // 이미지 클릭 핸들러 추가
//
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {
//        // 이미지 섹션
//        item {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                // 제목과 더보기 버튼
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "이미지",
//                        fontSize = 16.sp
//                    )
//                    TextButton(onClick = onImageMoreClick) {
//                        Text(text = "더보기")
//                    }
//                }
//
//                // 이미지 컴포넌트들
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    repeat(3) { index ->
//                        ImageSmall(
//                            onClick = { onImageClick(index) }  // 각 이미지별 클릭 이벤트
//                        )
//                    }
//                }
//            }
//        }
//
//        // 동영상 섹션
//        item {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "동영상",
//                        fontSize = 16.sp
//                    )
//                    TextButton(onClick = onVideoMoreClick) {
//                        Text(text = "더보기")
//                    }
//                }
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    repeat(3) {
//                        VideoSmall()
//                    }
//                }
//            }
//        }
//
//        // 블로그 섹션
//        item {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "블로그",
//                        fontSize = 16.sp
//                    )
//                    TextButton(onClick = onBlogMoreClick) {
//                        Text(text = "더보기")
//                    }
//                }
//
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    repeat(2) {
//                        BlogSmall()
//                    }
//                }
//            }
//        }
//
//        // 뉴스 섹션
//        item {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "뉴스",
//                        fontSize = 16.sp
//                    )
//                    TextButton(onClick = onNewsMoreClick) {
//                        Text(text = "더보기")
//                    }
//                }
//
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    repeat(2) {
//                        NewsSmall()
//                    }
//                }
//            }
//        }
//    }
//}


@Composable
fun AllTabCard(
    onImageMoreClick: () -> Unit = {},
    onVideoMoreClick: () -> Unit = {},
    onBlogMoreClick: () -> Unit = {},
    onNewsMoreClick: () -> Unit = {},
    onImageClick: (Int) -> Unit = {}  // 이미지 클릭 핸들러 추가
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // 모든 섹션 간격 동일하게 설정
    ) {
        // 이미지 섹션
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 제목과 더보기 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "이미지",
                    fontSize = 16.sp
                )
                TextButton(onClick = onImageMoreClick) {
                    Text(text = "더보기")
                }
            }

            // 이미지 컴포넌트들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    ImageSmall(
                        onClick = { onImageClick(index) }  // 각 이미지별 클릭 이벤트
                    )
                }
            }
        }

        // 동영상 섹션
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "동영상",
                    fontSize = 16.sp
                )
                TextButton(onClick = onVideoMoreClick) {
                    Text(text = "더보기")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(2) {
                    VideoSmall()
                }
            }
        }

//        // 블로그 섹션
//        Column(
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "블로그",
//                    fontSize = 16.sp
//                )
//                TextButton(onClick = onBlogMoreClick) {
//                    Text(text = "더보기")
//                }
//            }
//
//            Column(
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                repeat(2) {
//                    BlogSmall()
//                }
//            }
//        }
        // 블로그 섹션 (변경됨)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "블로그",
                    fontSize = 16.sp
                )
                TextButton(onClick = onBlogMoreClick) {
                    Text(text = "더보기")
                }
            }

            Row( // ✅ 동영상과 동일하게 Row로 변경하여 간격 유지
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BlogSmall(
                        title = "침착맨 침투부 1차 굿즈 내돈내산 후기",
                        description = "침착맨 굿즈 구매 후 솔직한 리뷰!",
                        imageUrl = "sample_url",
                        onClick = { println("블로그 클릭됨!") }
                    )

                    BlogSmall(
                        title = "'OOTD of 침착맨' 방문 후기",
                        description = "침착맨 팝업스토어에서 다양한 굿즈 체험!",
                        imageUrl = "sample_url",
                        onClick = { println("블로그 클릭됨!") }
                    )

                }
            }
        }


        // 뉴스 섹션
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "뉴스",
                    fontSize = 16.sp
                )
                TextButton(onClick = onNewsMoreClick) {
                    Text(text = "더보기")
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
//                repeat(2) {
                NewsSmall(
                    headline = "AI 기술 혁신, 글로벌 산업에 미치는 영향",
                    keywords = listOf("AI", "기술혁신", "산업 변화"),
                    imageUrl = "sample_url",
                    onClick = { println("뉴스 클릭됨!") }
                )

                NewsSmall(
                    headline = "전기차 충전소 확장, 새로운 정부 지원 발표",
                    keywords = listOf("전기차", "충전소", "정부 지원"),
                    imageUrl = "sample_url",
                    onClick = { println("뉴스 클릭됨!") }
                )


//                }
            }
        }
    }
}


