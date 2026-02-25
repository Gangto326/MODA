package com.example.modapjt.components.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.components.video.YouTubePlayer

@Composable
fun VideoItem(videoUrl: String, title: String, cardId: String, navController: NavController) {
    Log.d("VideoItem", "Received URL: $videoUrl")
    Log.d("VideoItem", "Received title: $title")
    Log.d("VideoItem", "Received cardId: $cardId")

    val videoId = getYouTubeVideoId(videoUrl)
    Log.d("VideoItem", "Extracted videoId: $videoId")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // VideoItem.kt 수정
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            // key 파라미터 추가 - 이렇게 하면 videoId가 변경될 때 컴포넌트가 완전히 재구성됨
            key(videoId) {
                YouTubePlayer(
                    videoId = videoId,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    Log.d("VideoItem", "Navigating to cardDetail/$cardId")
                    navController.navigate("cardDetail/$cardId")
                }
        )
    }
}

private fun getYouTubeVideoId(url: String): String {
    Log.d("VideoItem", "Parsing URL: $url")
    return try {
        val result = when {
            url.contains("youtu.be/") -> {
                val id = url.substringAfter("youtu.be/").substringBefore("?")
                Log.d("VideoItem", "Extracted from youtu.be format: $id")
                id
            }
            url.contains("youtube.com/watch?v=") -> {
                val id = url.substringAfter("v=").substringBefore("&")
                Log.d("VideoItem", "Extracted from youtube.com/watch format: $id")
                id
            }
            url.contains("youtube.com/embed/") -> {
                val id = url.substringAfter("embed/").substringBefore("?")
                Log.d("VideoItem", "Extracted from youtube.com/embed format: $id")
                id
            }
            else -> {
                Log.d("VideoItem", "Using URL as ID: $url")
                url
            }
        }
        result
    } catch (e: Exception) {
        Log.e("VideoItem", "Error parsing URL: ${e.message}")
        ""
    }
}