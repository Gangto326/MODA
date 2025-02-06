import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*





@Composable
fun VideoSmall(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start // 텍스트를 중앙 정렬
    ) {
        Box(
            modifier = Modifier
                .size(width = 160.dp, height = 90.dp) // 16:9 비율 유지
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE0E0E0))
                .clickable(onClick = onClick)
        )

        Spacer(modifier = Modifier.height(4.dp)) // 이미지와 텍스트 사이 여백 추가

        Text(
            text = "Video", // 이미지 아래에 텍스트 배치
            fontSize = 14.sp,
            color = Color.Black // 가독성을 위해 검은색 적용
        )
    }
}

