
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.domain.model.Card
import com.example.modapjt.screen2.EmptyMessage

@Composable
fun AllTabCard(
    navController: NavController, // NavController 추가
    imageCards: List<Card>,
    videoCards: List<Card>,
    blogCards: List<Card>,
    newsCards: List<Card>,
    onImageMoreClick: () -> Unit = {},
    onVideoMoreClick: () -> Unit = {},
    onBlogMoreClick: () -> Unit = {},
    onNewsMoreClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(24.dp) // 섹션 간 간격 설정
    ) {
        // 동영상 섹션
        SectionBlock(
            title = "동영상",
            items = videoCards,
            isImage = false,
            onMoreClick = onVideoMoreClick,
            navController = navController // NavController 전달
        )
        SectionDivider() // 동영상 섹션 후 구분선

        // 블로그 섹션
        SectionBlock(
            title = "블로그",
            items = blogCards,
            isImage = false,
            onMoreClick = onBlogMoreClick,
            navController = navController // NavController 전달
        )
        SectionDivider() // 블로그 섹션 후 구분선

        // 뉴스 섹션
        SectionBlock(
            title = "뉴스",
            items = newsCards,
            isImage = false,
            onMoreClick = onNewsMoreClick,
            navController = navController // NavController 전달
        )
        SectionDivider() // 이미지 섹션 후 구분선

        // 이미지 섹션
        SectionBlock(
            title = "이미지",
            items = imageCards,
            isImage = true,
            onMoreClick = onImageMoreClick,
            navController = navController // NavController 전달
        )
    }
}

// 각 섹션을 공통적으로 처리하는 컴포넌트
@Composable
fun SectionBlock(title: String, items: List<Card>, isImage: Boolean, onMoreClick: () -> Unit, navController: NavController ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // 섹션 간 간격 유지
    ) {
        SectionHeader(title)

        if (items.isEmpty()) {
            val message = when (title) {
                "동영상" -> "저장된 ${title}이 없습니다"
                else -> "저장된 ${title}가 없습니다"
            }
            EmptyMessage(message) // ✅ 동영상만 "이 없습니다", 나머지는 "가 없습니다"
        }
        else {
            if (isImage) {
                // 이미지 섹션 (3x2 레이아웃, 간격 8dp 적용)
                val rows = items.take(6).chunked(3) // 3개씩 나누어 2줄 생성

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) { // 이미지 행(row) 간 간격 8dp 유지
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp) // 이미지 간 간격 8dp 유지
                        ) {
                            rowItems.forEach { card ->
                                ImageSmall(
                                    imageUrl = card.thumbnailUrl ?: "",
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(100.dp),
                                    onClick = { navController.navigate("cardDetail/${card.cardId}") }, // 클릭 시 이동
                                    isMine = card.isMine,
                                    bookMark = card.bookMark
                                )
                            }

                            // 빈 공간을 채우기 위한 Spacer 추가 (3개가 안될 경우)
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                // 동영상, 블로그, 뉴스 섹션을 처리하는 부분
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items.take(3).forEachIndexed { index, card ->
                        Column {
                            when (title) {
                                "동영상" -> VideoSmall(
                                    videoId = card.thumbnailUrl ?: "",
                                    title = card.title,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { navController.navigate("cardDetail/${card.cardId}") },
                                    isMine = card.isMine,
                                    bookMark = card.bookMark,
                                    thumbnailContent = card.thumbnailContent ?: "",
                                    keywords = card.keywords.take(3)
                                )

                                "블로그" -> BlogSmall(
                                    title = card.title,
                                    description = card.thumbnailContent ?: "",
                                    imageUrl = card.thumbnailUrl ?: "",
                                    onClick = { navController.navigate("cardDetail/${card.cardId}") },
                                    isMine = card.isMine,
                                    bookMark = card.bookMark,
                                    keywords = card.keywords
                                )

                                "뉴스" -> NewsSmall(
                                    headline = card.title,
                                    keywords = card.keywords,
                                    description = card.thumbnailContent ?: "",
                                    imageUrl = card.thumbnailUrl ?: "",
                                    onClick = { navController.navigate("cardDetail/${card.cardId}") },
                                    isMine = card.isMine,
                                    bookMark = card.bookMark
                                )
                            }

                            // 🔥 마지막 아이템이 아닐 때만 Divider 추가
                            if (index < items.take(3).size - 1) {
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp), // ✅ 구분선 위에 8dp 패딩 추가
                                    color = Color(0xFFF1F1F1),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
                SectionAdd("$title 더보기", onMoreClick)
        }


    }
}

// 섹션 구분선
@Composable
fun SectionDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp), // 패딩 추가
        color = Color(0xFFF1F1F1),
        thickness = 6.dp // 구분선 두께 설정
    )
}

// ✅ 섹션 제목 컴포넌트
@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .background(Color.Yellow) // 배경 색
            .height(IntrinsicSize.Min), // 높이를 내부 요소에 맞춤 (불필요한 공간 제거)
        verticalAlignment = Alignment.CenterVertically, // 아이콘과 텍스트를 세로 중앙 정렬
        horizontalArrangement = Arrangement.Start // 텍스트는 왼쪽 정렬
    ) {
        // 섹션별 아이콘 추가
        val iconResId = when (title) {
            "이미지" -> R.drawable.ic_image // 이미지 섹션 아이콘
            "동영상" -> R.drawable.ic_video // 동영상 섹션 아이콘
            "블로그" -> R.drawable.ic_blog // 블로그 섹션 아이콘
            "뉴스" -> R.drawable.ic_news // 뉴스 섹션 아이콘
            else -> null
        }

        // 아이콘이 있을 경우, 아이콘을 왼쪽에 추가
        iconResId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "$title 아이콘",
                modifier = Modifier
                    .size(24.dp) // 아이콘 크기 조정
                    .padding(end = 6.dp) // 아이콘과 텍스트 간의 간격
            )
        }

        // 섹션 제목 텍스트
        Text(
            text = title,
            color = Color(0xFF2B2826),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold // 텍스트는 굵게 설정
        )
    }
}

// ✅ 더보기 버튼
@Composable
private fun SectionAdd(
    text: String,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .border(1.dp, Color(0xFFFFC107), shape = RoundedCornerShape(16.dp)), // 테두리 추가
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onMoreClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = Color(0xFF2B2826)
            )
        }
    }
}
