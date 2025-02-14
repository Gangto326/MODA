package com.example.modapjt.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

data class ImageItem(
    val cardId: String,
    val thumbnailUrl: String
)

@Composable
fun ImageList(navController: NavController, images: List<ImageItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // ✅ 가로 스크롤 가능하게 설정
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ✅ 2행 배치를 위해 리스트를 두 개의 그룹으로 나눔
            val firstRow = images.filterIndexed { index, _ -> index % 2 == 0 }
            val secondRow = images.filterIndexed { index, _ -> index % 2 == 1 }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                firstRow.forEach { image ->
                    ImageThumbnail(image, navController)
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                secondRow.forEach { image ->
                    ImageThumbnail(image, navController)
                }
            }
        }
    }
}

@Composable
fun ImageThumbnail(image: ImageItem, navController: NavController) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .clickable { navController.navigate("cardDetail/${image.cardId}") } // ✅ 클릭 시 상세 이동
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = image.thumbnailUrl),
            contentDescription = "Thumbnail Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
