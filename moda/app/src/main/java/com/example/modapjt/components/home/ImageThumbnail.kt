package com.example.modapjt.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.R

data class ImageItem(
    val cardId: String,
    val thumbnailUrl: String,
    val bookmark: Boolean = false
)

@Composable
fun ImageList(navController: NavController, images: List<ImageItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // ✅ 가로 스크롤 가능하게 설정 (한 줄 정렬)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            images.forEach { image ->
                ImageThumbnail(image, navController)
            }
        }
    }
}

@Composable
fun ImageThumbnail(image: ImageItem, navController: NavController) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(12.dp))
            .clickable { navController.navigate("cardDetail/${image.cardId}") }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = image.thumbnailUrl),
            contentDescription = "Thumbnail Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
        )

        // 🔥 북마크 아이콘 추가
        androidx.compose.material3.Icon(
            painter = painterResource(
                if (image.bookmark) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline
            ),
            contentDescription = "Bookmark Icon",
            tint = if (image.bookmark) Color(0xFFFFA500) else Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
        )
    }
}
