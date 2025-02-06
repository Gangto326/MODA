import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.modapjt.R

@Composable
fun SearchBar2(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxWidth(), //반응형
        color = MaterialTheme.colorScheme.surface
    ) {
        Row( //내부 Row 배치
            modifier = Modifier
                .fillMaxWidth() //fillMaxWidth()로 화면 크기에 맞춰 조정됨
                .height(56.dp) // 헤더 높이 일관된 시각적 표현을 위해 dp 값으로 고정
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, //SpaceBetween으로 로고와 아이콘들이 양끝에 적절히 배치됨
            verticalAlignment = Alignment.CenterVertically //화면 크기가 변해도 요소들 간의 간격이 자동으로 조정됨
        ) {

            // 좌측 아이콘들
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 메뉴 아이콘 (예시)
                IconButton(
                    onClick = { /* 메뉴 클릭 이벤트 */ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_backbotton),
                        contentDescription = "Back Button", // 비워두면 안됨.
                        modifier = Modifier.size(24.dp) // 아이콘 크기
                    )
                }
            }



            // 우측 아이콘들
            Row(
                horizontalArrangement = Arrangement.spacedBy(-5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 알림 아이콘
                IconButton(
                    onClick = { /* 알림 클릭 이벤트 */ }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        modifier = Modifier.size(24.dp) // 아이콘 크기
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBar2Preview() {
    MaterialTheme {
        SearchBar2(modifier = Modifier.padding(8.dp))
    }
}