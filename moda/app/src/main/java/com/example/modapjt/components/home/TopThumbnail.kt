package com.example.modapjt.components.home
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TopThumbnail(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 10f)  // 16:10 비율 유지
            .background(color)
    )
}
