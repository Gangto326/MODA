import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.domain.model.CardDetail

@Composable
fun ImageDetailScreen(cardDetail: CardDetail) {
    Column(modifier = Modifier.padding(16.dp)) {
        cardDetail.thumbnailUrl?.let {
            Image(painter = rememberAsyncImagePainter(it), contentDescription = "이미지", modifier = Modifier.fillMaxWidth().height(300.dp))
        }

        Text(text = "키워드: ${cardDetail.keywords.joinToString(", ")}", style = MaterialTheme.typography.bodyLarge)

        Text(text = "생성 날짜: ${cardDetail.createdAt}", style = MaterialTheme.typography.bodySmall)
    }
}
