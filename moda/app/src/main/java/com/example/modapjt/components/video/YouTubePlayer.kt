package com.example.modapjt.components.video

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
fun YouTubePlayer(videoId: String, modifier: Modifier = Modifier, isTopVideo: Boolean = false) {
    val context = LocalContext.current
    val youTubePlayerView = remember {
        YouTubePlayerView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    // 생명주기 관리 추가
//    DisposableEffect(Unit) {
//        // 리소스가 필요할 때만 추가하기
//        (context as? androidx.lifecycle.LifecycleOwner)?.lifecycle?.addObserver(youTubePlayerView)
//
//        onDispose {
//            youTubePlayerView.release()  // 재생 종료 후 리소스를 해제
//        }
//    }

    DisposableEffect(youTubePlayerView) {
        val lifecycleOwner = context as? androidx.lifecycle.LifecycleOwner
        lifecycleOwner?.lifecycle?.addObserver(youTubePlayerView)

        onDispose {
            try {
                // 라이프사이클 옵저버 제거
                lifecycleOwner?.lifecycle?.removeObserver(youTubePlayerView)
                // release() 호출 전에 약간의 지연을 줘서 리소스가 적절히 정리되도록 함
                youTubePlayerView.release()
            } catch (e: Exception) {
                // release 과정에서 발생할 수 있는 예외 처리
                e.printStackTrace()
            }
        }
    }

    AndroidView(
        factory = { youTubePlayerView },
        modifier = modifier
    ) { view ->
        view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // 상단 비디오일 때만 자동 재생하고 음소거
                if (isTopVideo) {
                    youTubePlayer.loadVideo(videoId, 0f)  // 상단 비디오만 자동 재생
                    youTubePlayer.mute()  // 음소거
                } else {
                    // 다른 비디오는 자동으로 재생되지 않도록 하고 멈춤
                    youTubePlayer.cueVideo(videoId, 0f)  // 자동재생은 하지 않고 대기 상태로 유지
                }
            }
        })
    }
}



// 추가 기능
// - 자동 재생을 방지하고 싶다면
// youTubePlayer.cueVideo(videoId, 0f)
// cueVideo() 사용하면 자동 재생 없이 대기 상태!
// - 유저가 앱을 나가면 재생을 중지하고 싶다면
// onStop()에서 youTubePlayer.pause() 실행
