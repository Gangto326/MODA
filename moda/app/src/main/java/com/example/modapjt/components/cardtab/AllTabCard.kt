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
        verticalArrangement = Arrangement.spacedBy(24.dp)
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
                repeat(3) {
                    VideoSmall()
                }
            }
        }

        // 블로그 섹션
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

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(2) {
                    BlogSmall()
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
                repeat(2) {
                    NewsSmall()
                }
            }
        }
    }
}
