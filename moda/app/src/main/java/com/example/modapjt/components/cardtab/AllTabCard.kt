
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modapjt.domain.model.Card


@Composable
fun AllTabCard(
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
            .padding(16.dp), // 전체 패딩 추가
        verticalArrangement = Arrangement.spacedBy(24.dp) // 섹션 간 간격 설정
    ) {
        // 이미지 섹션 : 가로 스크롤 버전
//        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//            SectionHeader("이미지")
//            if (imageCards.isEmpty()) {
//                Text(
//                    text = "저장된 이미지가 없습니다",
//                    color = Color.Gray,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    textAlign = TextAlign.Center
//                )
//            } else {
//                Row(
//                    modifier = Modifier
//                        .horizontalScroll(rememberScrollState()), // 가로 스크롤 가능하도록 설정
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    imageCards.take(10).forEach { card ->
//                        println("이미지 Card ID: ${card.cardId}, Bookmark: ${card.bookMark}") // 로그 추가
//                        ImageSmall(
//                            imageUrl = card.thumbnailUrl ?: "",
//                            modifier = Modifier.size(120.dp), // 크기 고정
//                            onClick = {},
//                            isMine = card.isMine,  // isMine 값 전달
//                            bookMark = card.bookMark, // 즐겨찾기 여부 전달
//                        )
//                    }
//                }
//            }
//           SectionAdd(onImageMoreClick)
//        }
        // 이미지 섹션 (3열 × 2줄, 최대 6개)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("이미지") // 섹션 제목
            if (imageCards.isEmpty()) {
                // 데이터가 없을 경우 메시지 표시
                Text(
                    text = "저장된 이미지가 없습니다",
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // 최대 6개를 가져와서 3개씩 그룹으로 나누기
                    val rows = imageCards.take(6).chunked(3)

                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp) // 카드 간격
                        ) {
                            rowItems.forEach { card ->
                                println("이미지 Card ID: ${card.cardId}, Bookmark: ${card.bookMark}") // 로그 추가
                                // 각 이미지 카드 표시
                                ImageSmall(
                                    imageUrl = card.thumbnailUrl ?: "",
                                    modifier = Modifier
                                        .weight(1f) // 동일한 크기로 분배
                                        .size(100.dp), // 크기 조절
                                    onClick = {},
                                    isMine = card.isMine,
                                    bookMark = card.bookMark
                                )
                            }

                            // 빈 칸을 채우기 위한 Spacer 추가 (3개가 되지 않을 경우)
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
            SectionAdd(onImageMoreClick) // 더보기 버튼
        }

        // 동영상 섹션 : 가로 스크롤 버전
//        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//            SectionHeader("동영상")
//            if (videoCards.isEmpty()) {
//                Text(
//                    text = "저장된 영상이 없습니다",
//                    color = Color.Gray,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    textAlign = TextAlign.Center
//                )
//            } else {
//                Row(
//                    modifier = Modifier
//                        .horizontalScroll(rememberScrollState()), // 가로 스크롤 추가
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    videoCards.take(10).forEach { card ->
//                        VideoSmall(
//                            videoId = card.thumbnailUrl ?: "",
//                            title = card.title,
//                            modifier = Modifier.width(160.dp), // 가로 크기 유지
//                            onClick = {},
//                            isMine = card.isMine,  // isMine 값 전달
//                            bookMark = card.bookMark, // 즐겨찾기 여부 전달
//                            keywords = card.keywords.take(3), // 키워드 전달
//                        )
//                    }
//                }
//            }
//              SectionAdd(onVideoMoreClick)
//        }

        // 동영상 섹션 (3개만 표시)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("동영상") // 섹션 제목
            if (videoCards.isEmpty()) {
                EmptyMessageall("저장된 영상이 없습니다")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    videoCards.take(3).forEach { card ->
                        println("동영상 Card ID: ${card.cardId}, Bookmark: ${card.bookMark}") // 로그 추가
                        // 각 동영상 카드 표시
                        VideoSmall(
                            videoId = card.thumbnailUrl ?: "",
                            title = card.title,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {},
                            isMine = card.isMine,
                            bookMark = card.bookMark, // 즐겨찾기 여부 전달
                            keywords = card.keywords.take(3) // 키워드 3개 제한
                        )
                    }
                }
            }
            SectionAdd(onVideoMoreClick) // 더보기 버튼
        }

        // 블로그 섹션 (3개만 표시)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("블로그") // 세션 제목
            if (blogCards.isEmpty()) {
                Text(
                    text = "저장된 블로그가 없습니다",
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
//                blogCards.take(3).forEach { card ->
//                    println("블로그 Card ID: ${card.cardId}, Bookmark: ${card.bookMark}") // 로그 추가
//                    // 각 블로그 카드 표시
//                    BlogSmall(
//                        title = card.title,
//                        description = card.thumbnailContent ?: "",
//                        imageUrl = card.thumbnailUrl ?: "",
//                        onClick = {},
//                        isMine = card.isMine,  // isMine 값 전달
//                        bookMark = card.bookMark,  // 즐겨찾기 여부 전달
//                        keywords = card.keywords,
//                    )
//                }
                Column {
                    blogCards.take(3).forEachIndexed { index, card -> // 상위 3개만 가져오기
                        BlogSmall(
                            title = card.title,
                            description = card.thumbnailContent ?: "",
                            imageUrl = card.thumbnailUrl ?: "",
                            onClick = {},
                            isMine = card.isMine,
                            bookMark = card.bookMark,
                            keywords = card.keywords,
                        )

                        // 마지막 아이템이 아닌 경우에만 Divider 추가
                        if (index < 2) { // 3개까지만 표시하므로 index < 2까지만 줄 추가
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp), // 위아래 8dp 간격 추가
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
            SectionAdd(onBlogMoreClick) // 더보기 버튼
        }

        // 뉴스 섹션 (3개만 표시)
//        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//            SectionHeader("뉴스") // 섹션 제목
//            if (newsCards.isEmpty()) {
//                Text(
//                    text = "저장된 뉴스가 없습니다",
//                    color = Color.Gray,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    textAlign = TextAlign.Center
//                )
//            } else {
//                newsCards.take(3).forEach { card ->
//                    println("뉴스 Card ID: ${card.cardId}, Bookmark: ${card.bookMark}") // 로그 추가
//                    // 각 뉴스 아이템 표시
//                    NewsSmall(
//                        headline = card.title,
//                        keywords = card.keywords,
//                        imageUrl = card.thumbnailUrl ?: "",
//                        onClick = {},
//                        isMine = card.isMine,  // isMine 값 전달
//                        bookMark = card.bookMark,  // 즐겨찾기 여부 전달
//                    )
//                }
//            }
        // ✅ 뉴스 섹션 (각 카드 사이에 줄 추가)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("뉴스")
            if (newsCards.isEmpty()) {
                Text(
                    text = "저장된 뉴스가 없습니다",
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Column {
                    newsCards.take(3).forEachIndexed { index, card -> // 상위 3개만 가져오기
                        NewsSmall(
                            headline = card.title,
                            keywords = card.keywords,
                            imageUrl = card.thumbnailUrl ?: "",
                            onClick = {},
                            isMine = card.isMine,
                            bookMark = card.bookMark,
                        )

                        // 마지막 아이템이 아닌 경우에만 Divider 추가
                        if (index < 2) { // 3개까지만 표시하므로 index < 2까지만 줄 추가
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp), // 위아래 8dp 간격 추가
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }

        SectionAdd(onNewsMoreClick) // 더보기 버튼
        }
    }
}

// 섹션 제목 컴포넌트
@Composable
private fun SectionHeader(
    title: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold // 폰트 굵게 적용
        )
    }
}


// 더보기 버튼 컴포넌트
@Composable
private fun SectionAdd(
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(horizontal = 16.dp) // 좌우 여백 추가
            .height(48.dp) // 버튼 높이 조절
            .border(1.dp, Color(0xFFFFC107), shape = RoundedCornerShape(12.dp)), // 테두리 추가
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onMoreClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "더보기",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}



//// 공통으로 사용되는 빈 목록 메시지
@Composable
fun EmptyMessageall(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}