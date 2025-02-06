import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

//@Composable
//fun ImageBig(
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit = {}
//) {
//    Box(
//        modifier = modifier
//            .size(100.dp)
//            .clip(RoundedCornerShape(8.dp))
//            .background(Color(0xFFE0E0E0))  // 회색 배경
//            .clickable(onClick = onClick)  // 클릭 가능하도록 수정
//    )
//}

@Composable
fun ImageBig(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF4A90E2))  // 배경
//            .clickable(onClick = onClick)  // 클릭 가능하도록 수정
            .clickable {
                println("imagebig -> Image 클릭됨") // 디버깅 로그 추가
                onClick()
            }
    )
}