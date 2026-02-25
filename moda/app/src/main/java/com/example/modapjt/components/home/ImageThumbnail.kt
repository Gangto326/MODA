package com.example.modapjt.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.R

// 🔹 이미지 데이터를 담는 데이터 클래스 (이미지 ID, 썸네일 URL, 북마크 여부)
data class ImageItem(
    val cardId: String,        // 해당 카드의 ID (디테일 화면으로 이동할 때 사용)
    val thumbnailUrl: String,  // 썸네일 이미지 URL
    val bookmark: Boolean = false // 북마크 여부 (기본값 false)
)

// 🔹 이미지 리스트를 수평 스크롤로 표시하는 컴포저블 함수
@Composable
fun ImageList(navController: NavController, images: List<ImageItem>) {
    val limitedImages = images.take(20) // 최대 20개까지만 표시하여 성능 최적화

    LazyRow(
        modifier = Modifier
            .fillMaxWidth() // 가로 전체 차지
            .padding(horizontal = 16.dp), // 좌우 여백 추가
        horizontalArrangement = Arrangement.spacedBy(6.dp) // 아이템 간격 8dp
    ) {
        // 2개씩 묶어서 한 Column으로 배치 (세로 2줄 레이아웃)
        itemsIndexed(limitedImages.chunked(2)) { _, rowImages ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp) // 2줄 간격 조절
            ) {
                rowImages.forEach { image ->
                    ImageThumbnail(image, navController) // 개별 이미지 썸네일 표시
                }
            }
        }
    }
}

// 🔹 개별 이미지 썸네일을 표시하는 컴포저블 함수
@Composable
fun ImageThumbnail(image: ImageItem, navController: NavController) {
    Box(
        modifier = Modifier
            .size(100.dp) // ✅ 크기 설정 (120x120)
//            .background(Color.LightGray, shape = RoundedCornerShape(16.dp)) // ✅ 라운딩된 배경 추가
            .clip(RoundedCornerShape(8.dp)) // 🔥 외곽을 둥글게 만들기
            .clickable (
                indication = null, // 클릭 효과 제거
                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
            ){ navController.navigate("cardDetail/${image.cardId}") } // 클릭 시 상세 페이지로 이동
    ) {
        // 🔹 네트워크에서 이미지를 불러와서 표시
        Image(
            painter = rememberAsyncImagePainter(model = image.thumbnailUrl), // 썸네일 URL을 이용해 이미지 로딩
            contentDescription = "Thumbnail Image", // 접근성을 위한 설명
            contentScale = ContentScale.Crop, // 이미지 크롭하여 꽉 차게 표시
            modifier = Modifier
                .fillMaxSize() // 부모 크기만큼 채우기
                .background(Color.White, shape = RoundedCornerShape(12.dp)) // ✅ 내부도 라운딩 맞추기
        )


        //다크모드 색상 추가
        val iconResource = if (image.bookmark) {
            if (isSystemInDarkTheme()) R.drawable.ic_d_bookmark else R.drawable.ic_bookmark_filled
        } else {
            R.drawable.ic_bookmark_outline
        }

        // 🔹 북마크 아이콘 추가 (북마크 여부에 따라 다른 아이콘 표시)
        androidx.compose.material3.Icon(
            painter = painterResource(iconResource),
            contentDescription = "Bookmark Icon", // 접근성을 위한 설명
            tint = if (image.bookmark) Color.Unspecified else Color.Transparent, // ✅ 북마크된 경우 오렌지 색상 적용
            modifier = Modifier
                .align(Alignment.TopEnd) // 🔥 오른쪽 상단에 위치
                .padding(5.dp) // 아이콘과 모서리 사이 여백 추가
                .size(20.dp) // 아이콘 크기 지정
        )
    }
}
