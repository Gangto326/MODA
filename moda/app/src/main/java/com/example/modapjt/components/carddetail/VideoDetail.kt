import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modapjt.components.video.YouTubePlayer
import com.example.modapjt.domain.model.CardDetail
import com.example.modapjt.utils.extractYouTubeVideoId

@Composable
fun VideoDetailScreen(cardDetail: CardDetail) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = cardDetail.title, style = MaterialTheme.typography.headlineMedium)

        // 유튜브 영상 플레이어 (썸네일에서 ID 추출)
        val videoId = extractYouTubeVideoId(cardDetail.originalUrl)
        if (!videoId.isNullOrEmpty()) {
            YouTubePlayer(videoId = videoId, modifier = Modifier.height(200.dp))
        }

        Text(text = "키워드: ${cardDetail.keywords.joinToString(", ")}", style = MaterialTheme.typography.bodyLarge)

        Text(text = "본문: ${cardDetail.content}", style = MaterialTheme.typography.bodyLarge)

        Text(text = "링크: ${cardDetail.originalUrl}", style = MaterialTheme.typography.bodyLarge)

        Text(text = "타임라인: ${cardDetail.subContents.joinToString(", ")}", style = MaterialTheme.typography.bodyLarge)

        Text(text = "생성 날짜: ${cardDetail.createdAt}", style = MaterialTheme.typography.bodySmall)
    }
}
