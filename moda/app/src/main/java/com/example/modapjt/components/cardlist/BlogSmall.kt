import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BlogSmall(
    title: String,
    description: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // ✅ 더 부드러운 카드 느낌
            .background(Color.White) // ✅ 흰 배경으로 차별화
            .clickable(onClick = onClick) // ✅ 클릭 가능하도록 추가
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ✅ 텍스트 영역 (제목 + 설명)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold // ✅ 제목 Bold 스타일 적용
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(12.dp)) // ✅ 텍스트와 이미지 사이 간격

        // ✅ 썸네일 이미지 (오른쪽에 배치)
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "이미지",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}
