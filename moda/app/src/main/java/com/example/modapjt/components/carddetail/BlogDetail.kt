import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.domain.model.CardDetail

@Composable
fun BlogDetailScreen(cardDetail: CardDetail) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // ✅ 스크롤 가능하게 설정
    ) {
        Text(text = cardDetail.title, style = MaterialTheme.typography.headlineMedium)

        cardDetail.thumbnailUrl?.let {
            Image(painter = rememberAsyncImagePainter(it), contentDescription = "썸네일", modifier = Modifier.height(200.dp))
        }

        Text(text = "키워드: ${cardDetail.keywords.joinToString(", ")}", style = MaterialTheme.typography.bodyLarge)

        Text(text = "본문: ${cardDetail.content}", style = MaterialTheme.typography.bodyLarge)

        Text(text = "링크: ${cardDetail.originalUrl}", style = MaterialTheme.typography.bodyLarge)

        cardDetail.subContents.forEach { imageUrl ->
            Image(painter = rememberAsyncImagePainter(imageUrl), contentDescription = "블로그 이미지", modifier = Modifier.height(200.dp))
        }

        Text(text = "생성 날짜: ${cardDetail.createdAt}", style = MaterialTheme.typography.bodySmall)
    }
}
