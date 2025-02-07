import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 이미지 섹션
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("이미지", onImageMoreClick)
            if (imageCards.isEmpty()) {
                Text(
                    text = "저장된 이미지가 없습니다",
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    imageCards.take(3).forEach { card ->
                        ImageSmall(
                            imageUrl = card.thumbnailUrl ?: "",
//                            modifier = Modifier.weight(1f), // Row 내부에서 각 아이템이 같은 비율(1:1:1)로 공간을 차지
                            modifier = Modifier.size(120.dp), // 크기 고정
                            onClick = {}
                        )
                    }
                }
            }
        }

        // 동영상 섹션
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("동영상", onVideoMoreClick)
            if (videoCards.isEmpty()) {
                Text(
                    text = "저장된 영상이 없습니다",
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    videoCards.take(2).forEach { card ->
                        VideoSmall(
                            videoId = card.thumbnailUrl ?: "",
                            title = card.title,
                            modifier = Modifier.weight(1f),
                            onClick = {}
                        )
                    }
                }
            }
        }

        // 블로그 섹션
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("블로그", onBlogMoreClick)
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
                blogCards.take(2).forEach { card ->
                    BlogSmall(
                        title = card.title,
                        description = card.thumbnailContent ?: "",
                        imageUrl = card.thumbnailUrl ?: "",
                        onClick = {}
                    )
                }
            }
        }

        // 뉴스 섹션
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeader("뉴스", onNewsMoreClick)
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
                newsCards.take(2).forEach { card ->
                    NewsSmall(
                        headline = card.title,
                        keywords = card.keywords,
                        imageUrl = card.thumbnailUrl ?: "",
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp
        )
        TextButton(onClick = onMoreClick) {
            Text(text = "더보기")
        }
    }
}


