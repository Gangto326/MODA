
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.app.ui.theme.customTypography


// NewsSmall: 뉴스 컨텐츠를 카드 형태로 표시하는 컴포저블 함수
@Composable
fun NewsSmall(
    headline: String, // 뉴스 제목
    description: String, // 뉴스 내용
    imageUrl: String, // 썸네일 이미지 URL
    isMine: Boolean, // 내가 저장한 컨텐츠 여부 (true면 흰색, false면 회색 배경)
    modifier: Modifier = Modifier,
    bookMark: Boolean, // 즐겨찾기 여부
    keywords: List<String>, // 뉴스 관련 키워드 목록
    onClick: () -> Unit = {} // 카드 클릭 시 실행될 동작
) {
    // 전체 컨테이너: 세로 방향으로 컨텐츠 배치
    Column(
        modifier = modifier
            .fillMaxWidth() // 가로 전체 너비 사용
//            .clip(RoundedCornerShape(12.dp)) // 모서리 둥글게 처리
            .background(if (!isMine) Color.Gray else Color.White) // 저장 여부에 따른 배경색
            .clickable(
                onClick = onClick,
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                )  // 클릭 가능하도록 설정
//            .padding(12.dp) // 내부 여백 설정


    ) {
        // 상단 영역: 제목과 이미지를 가로로 배치
        Row(
            verticalAlignment = Alignment.Top, // 상단 맞춤
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
        ) {
            // 🔥 제목 + 키워드를 감싸는 Column
            Column(
                modifier = Modifier.weight(1f) // 🔥 남은 공간 모두 차지해서 왼쪽 정렬
            ) {
                // 뉴스 제목 텍스트
                Text(
                    text = headline,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
//                    style = customTypography.titleMedium ,
                    maxLines = 2, // 최대 2줄까지 표시
                    overflow = TextOverflow.Ellipsis, // 넘치는 텍스트는 ...으로 표시
                )

                Text(
                    text = description,
                    style = customTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 6.dp)
                )

                // 🔥 키워드 리스트 (제목 아래 정렬)
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(10.dp),
//                    modifier = Modifier.padding(top = 4.dp)
//                ) {
//                    keywords.take(3).forEach { keyword ->
//                        Text(
//                            text = "# $keyword",
////                            fontSize = 12.sp,
//                            style = customTypography.bodySmall ,
//                            color = Color(0xFFBAADA4) // 🔥 키워드 색상
//                        )
//                    }
//                }
            }

            // 썸네일 이미지
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop, // 이미지 비율 유지하며 채우기
                modifier = Modifier
                    .size(80.dp) // 70dp x 70dp 크기
                    .clip(RoundedCornerShape(8.dp))// 이미지 모서리 둥글게
            )
        }

        // 하단 영역: 키워드와 즐겨찾기 아이콘을 가로로 배치 (🔥 키워드가 제목 아래로 이동했으므로 삭제 가능)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp), // 상단 여백 추가
            horizontalArrangement = Arrangement.SpaceBetween, // 요소들을 양끝으로 정렬
            verticalAlignment = Alignment.CenterVertically // 세로 방향 중앙 정렬
        ) {
            // 키워드 목록 (최대 3개)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp) // 키워드 간 간격
            ) {
                keywords.take(3).forEach { keyword ->
                    Text(
                        text = "# $keyword",
//                        fontSize = 12.sp,
                        style = customTypography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary, // 키워드는 파란색으로 표시( 고민 )
                    )
                }
            }

//            // 즐겨찾기 아이콘 (내 컨텐츠이고 즐겨찾기된 경우에만 표시)
//            if (bookMark && isMine) {
//                Icon(
//                    imageVector = Icons.Filled.Star,
//                    contentDescription = "즐겨찾기됨",
//                    tint = Color(0xFFFFCD69), // 노란색 별표 아이콘
//                    modifier = Modifier.size(20.dp) // 20dp x 20dp 크기
//                )
//            }
//        }


        }
    }
}