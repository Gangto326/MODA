package com.example.modapjt.components.video

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


/**
 * YouTube 영상을 앱 내부에서 재생할 수 있도록 하는 Composable 함수
 *
 * @param videoId 유튜브 영상의 ID (예: "dQw4w9WgXcQ")
 * @param modifier Compose UI에서 크기 및 레이아웃을 조정하는 Modifier
 */

@Composable
fun YouTubePlayer(videoId: String, modifier: Modifier = Modifier) {
    // AndroidView를 사용하여 Compose 내에서 기존 Android 뷰(YouTubePlayerView)를 포함
    AndroidView(
        factory = { context ->
            // YouTubePlayerView 생성
            val youTubePlayerView = YouTubePlayerView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // 가로: 전체 화면
                    ViewGroup.LayoutParams.MATCH_PARENT  // 세로: 전체 화면
                )
            }

            // YouTubePlayerView에 리스너 추가 (YouTubePlayer가 준비되면 실행됨)
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    // 유튜브 영상 로드 및 자동 재생 (두 번째 인자는 재생 시작 시간)
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })

            // YouTubePlayerView 반환 (이 뷰가 화면에 표시됨)
            youTubePlayerView
        },
        modifier = modifier // 외부에서 전달된 Modifier 적용 (크기 조절 가능)
    )
}


// 추가 기능
// - 자동 재생을 방지하고 싶다면
// youTubePlayer.cueVideo(videoId, 0f) // 🚀 cueVideo() 사용하면 자동 재생 없이 대기 상태!
// - 유저가 앱을 나가면 재생을 중지하고 싶다면
// onStop()에서 youTubePlayer.pause() 실행