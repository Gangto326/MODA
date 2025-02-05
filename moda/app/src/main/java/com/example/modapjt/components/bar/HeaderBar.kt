import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HeaderBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)  // 헤더 높이 설정
            .background(Color(0xFFB3E5FC)),  // 파스텔 톤 연한 파랑 (#B3E5FC)
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "헤더 😊",  // 텍스트 변경
            color = Color(0xFF01579B),  // 진한 파랑으로 텍스트 색상 변경
            fontSize = 18.sp,  // 글씨 크기를 살짝 줄임
            fontWeight = FontWeight.Medium  // 부드러운 느낌의 폰트 굵기
        )
    }
}
