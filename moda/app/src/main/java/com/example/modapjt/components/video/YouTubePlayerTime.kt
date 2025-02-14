//package com.example.modapjt.components.video
//
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
//
//
///**
// * YouTube 영상을 앱 내부에서 재생할 수 있도록 하는 Composable 함수
// *
// * @param videoId 유튜브 영상의 ID (예: "dQw4w9WgXcQ")
// * @param modifier Compose UI에서 크기 및 레이아웃을 조정하는 Modifier
// */
//
//@Composable
//fun YouTubePlayer(videoId: String, modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val youTubePlayerView = remember {
//        YouTubePlayerView(context).apply {
//            layoutParams = FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//        }
//    }
//
//    // 생명주기 관리 추가
//    DisposableEffect(Unit) {
//        (context as? androidx.lifecycle.LifecycleOwner)?.lifecycle?.addObserver(youTubePlayerView)
//
//        onDispose {
//            youTubePlayerView.release()
//        }
//    }
//
//    AndroidView(
//        factory = { youTubePlayerView },
//        modifier = modifier
//    ) { view ->
//        view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                youTubePlayer.loadVideo(videoId, 0f)
//            }
//        })
//    }
//}
//
//
//// 추가 기능
//// - 자동 재생을 방지하고 싶다면
//// youTubePlayer.cueVideo(videoId, 0f)
//// cueVideo() 사용하면 자동 재생 없이 대기 상태!
//// - 유저가 앱을 나가면 재생을 중지하고 싶다면
//// onStop()에서 youTubePlayer.pause() 실행


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
 * @param onReady 플레이어가 준비되었을 때 호출되는 콜백
 */
@Composable
fun YouTubePlayerTime(
    videoId: String,
    modifier: Modifier = Modifier,
    onPlayerReady: (YouTubePlayer) -> Unit
) {
    val context = LocalContext.current
    val youTubePlayerView = remember {
        YouTubePlayerView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    DisposableEffect(Unit) {
        (context as? androidx.lifecycle.LifecycleOwner)?.lifecycle?.addObserver(youTubePlayerView)

        onDispose {
            youTubePlayerView.release()
        }
    }

    AndroidView(
        factory = { youTubePlayerView },
        modifier = modifier
    ) { view ->
        view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
                onPlayerReady(youTubePlayer)
            }
        })
    }
}