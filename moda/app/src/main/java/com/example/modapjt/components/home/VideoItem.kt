package com.example.modapjt.components.home

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun VideoItem(
    videoUrl: String,
    title: String,
    cardId: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ✅ YouTube 영상 자동 재생 + 소리 ON (`mute=0`)
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    webChromeClient = WebChromeClient()
                    webViewClient = WebViewClient()
                    loadUrl("https://www.youtube.com/embed/${getYouTubeVideoId(videoUrl)}?autoplay=1&mute=0")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Black)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ 제목 클릭 시 상세 페이지 이동
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("cardDetail/$cardId")
                }
                .padding(4.dp)
        )
    }
}

// ✅ YouTube URL에서 Video ID 추출
fun getYouTubeVideoId(url: String): String {
    return url.substringAfter("v=").substringBefore("&")
}
