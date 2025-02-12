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

/**
 * 이미지 중심의 컨텐츠를 보여주는 상세 화면 컴포저블
 * 이미지와 관련 메타 정보를 세로로 배치
 *
 * @param cardDetail 표시할 이미지 컨텐츠의 상세 정보를 담은 객체
 */
@Composable
fun ImageDetailScreen(cardDetail: CardDetail) {
    Column(modifier = Modifier.padding(16.dp)) {
        // 썸네일 이미지가 있는 경우에만 표시
        cardDetail.thumbnailUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it), // 비동기적으로 이미지 로드
                contentDescription = "이미지",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        // 이미지 관련 키워드
        Text(text = "키워드: ${cardDetail.keywords.joinToString(", ")}", style = MaterialTheme.typography.bodyLarge)

        // 이미지 생성/업로드 날짜
        Text(text = "생성 날짜: ${cardDetail.createdAt}", style = MaterialTheme.typography.bodySmall)
    }
}