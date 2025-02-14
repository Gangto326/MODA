
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// ImageSmall: 정사각형 형태의 이미지 카드를 표시하는 컴포저블 함수
@Composable
fun ImageSmall(
    imageUrl: String, // 표시할 이미지 URL
    modifier: Modifier = Modifier,
    isMine: Boolean,  // 내가 저장한 이미지 여부 (true면 흰색, false면 회색 배경)
    bookMark: Boolean, // 즐겨찾기 여부
    onClick: () -> Unit = {}// 이미지 클릭 시 실행될 동작
) {
    // 화면 너비를 가져와서 이미지 크기 계산
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageSize = screenWidth / 3  // 화면 너비의 1/3 크기로 설정

    // Box 컨테이너: 이미지와 즐겨찾기 아이콘을 겹쳐서 표시
    Box(
        modifier = modifier
            .size(imageSize) // 계산된 크기로 설정
            .clip(RoundedCornerShape(8.dp)) // 모서리 둥글게 처리
            .background(if (!isMine) Color.Gray else Color.White) // 저장 여부에 따른 배경색
            .clickable(onClick = onClick) // 클릭 가능하도록 설정
    ) {
        // 메인 이미지
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop, // 이미지 비율 유지하며 채우기
            modifier = Modifier.fillMaxSize() // Box 전체 크기 채우기
        )

        // 즐겨찾기 아이콘 (내 이미지이고 즐겨찾기된 경우에만 표시)
        if (bookMark && isMine) {
            println("이미지즐겨찾기") // 디버깅용 로그
            Icon(
                imageVector = Icons.Filled.Star, // 별 모양 아이콘
                contentDescription = "즐겨찾기됨",
                tint = Color(0xFFFFCD69), // 노란색 별표
                modifier = Modifier
                    .size(20.dp) // 아이콘 크기
                    .align(Alignment.BottomEnd) // 우측 하단에 배치
                    .padding(6.dp) // 여백 추가
            )
        }
    }
}
