import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment

@Composable
fun WeeklyKeyword(keyword: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(50))  // 타원형 배경
            .padding(horizontal = 16.dp, vertical = 8.dp),  // 내부 패딩으로 타원형 만들기
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = keyword,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
