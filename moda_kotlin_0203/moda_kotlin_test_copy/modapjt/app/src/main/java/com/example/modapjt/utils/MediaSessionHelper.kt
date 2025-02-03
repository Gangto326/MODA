package com.example.modapjt.utils

import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.util.Log

class MediaSessionHelper(context: Context) {
    private val mediaSessionManager =
        context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

    fun getCurrentYouTubeVideoTitle(): String? {
        val sessions = mediaSessionManager.getActiveSessions(null)

        for (controller in sessions) {
            if (controller.packageName == "com.google.android.youtube") { // 유튜브 앱 감지
                val metadata = controller.metadata
                val title = metadata?.getString("android.media.metadata.TITLE")

                Log.d("MediaSessionHelper", "현재 유튜브 재생 중 영상 제목: $title")

                return title
            }
        }
        Log.e("MediaSessionHelper", "유튜브 영상 제목을 찾을 수 없음")
        return null
    }
}
