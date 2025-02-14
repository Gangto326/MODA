
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// BlogSmall: 블로그 포스트를 카드 형태로 표시하는 컴포저블 함수
@Composable
fun BlogSmall(
    title: String,         // 블로그 제목
    description: String,   // 블로그 내용 요약
    imageUrl: String,      // 썸네일 이미지 URL
    isMine: Boolean,       // 내가 저장한 포스트 여부 (true면 흰색, false면 회색 배경)
    keywords: List<String>,// 블로그 관련 키워드 목록
    bookMark: Boolean,     // 즐겨찾기 여부
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}// 카드 클릭 시 실행될 동작
) {
    // 전체 컨테이너: 세로 방향으로 컨텐츠 배치
    Column(
        modifier = modifier
            .fillMaxWidth() // 가로 전체 너비 사용
            .clip(RoundedCornerShape(12.dp)) // 모서리 둥글게 처리
//            .background(if (!isMine) Color.Gray else Color.White) // 저장 여부에 따른 배경색
            .background(if (!isMine) Color.Gray.copy(alpha = 0.2f)	 else Color.White) // 저장 여부에 따른 배경색
            .clickable(onClick = onClick)          // 클릭 가능하도록 설정
//            .padding(12.dp)                        // 내부 여백 설정
    ) {
        // 상단 영역: 텍스트와 이미지를 가로로 배치
        Row(
            verticalAlignment = Alignment.CenterVertically // 세로 방향 중앙 정렬
        ) {
            // 제목과 설명 텍스트 영역
            Column(
                modifier = Modifier.weight(1f)     // 남은 공간 모두 차지
            ) {
                // 블로그 제목
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,                  // 한 줄로 제한
                    overflow = TextOverflow.Ellipsis // 넘치는 텍스트는 ...으로 표시
                )
                // 블로그 설명
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray,            // 회색으로 표시
                    maxLines = 2,                  // 최대 2줄까지 표시
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            // 썸네일 이미지
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,  // 이미지 비율 유지하며 채우기
                modifier = Modifier
                    .size(70.dp)                  // 70dp x 70dp 크기
                    .clip(RoundedCornerShape(8.dp))// 이미지 모서리 둥글게
            )
        }

        // 하단 영역: 키워드와 즐겨찾기 아이콘을 가로로 배치
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),             // 상단 여백 추가
            horizontalArrangement = Arrangement.SpaceBetween, // 요소들을 양끝으로 정렬
            verticalAlignment = Alignment.CenterVertically    // 세로 방향 중앙 정렬
        ) {
            // 키워드 목록 (최대 3개)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp) // 키워드 간 간격
            ) {
                keywords.take(3).forEach { keyword ->
                    Text(
                        text = "# $keyword",        // "# $keyword" : 해시태그 형식으로 표시
                        fontSize = 12.sp,
                        color = Color.Black         // 파란색으로 표시
                    )
                }
            }

            // 즐겨찾기 아이콘 (내 포스트이고 즐겨찾기된 경우에만 표시)
            if (bookMark && isMine) {
                print("블로그 즐겨찾기 !!")        // 디버깅용 로그
                Icon(
                    imageVector = Icons.Filled.Star, // 별 모양 아이콘
                    contentDescription = "즐겨찾기됨",
                    tint = Color(0xFFFFD700),      // 노란색 별표
                    modifier = Modifier.size(20.dp) // 20dp x 20dp 크기
                )
            }
        }
    }
}