package com.example.modapjt.components.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomThumbnailList() {
    val thumbnails = listOf(
        Color(0xFFFFCDD2),
        Color(0xFFC8E6C9),
        Color(0xFFBBDEFB),
        Color(0xFFFFF9C4),
        Color(0xFFD1C4E9)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (color in thumbnails) {
                BottomThumbnail(backgroundColor = color)
            }
        }
    }
}
