package com.example.modapjt

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {
    companion object {
        private var lastYouTubeTitle: String? = null // 최근 감지한 유튜브 영상 제목 저장
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            Log.d("NotificationListener", "알림 감지됨: ${it.packageName}")

            if (it.packageName == "com.google.android.youtube") {
                Log.d("NotificationListener", "유튜브 알림 감지 성공!")

                // 🔹 알림 내용 확인 (모든 데이터 필드 출력)
                val extras = it.notification.extras
                for (key in extras.keySet()) {
                    Log.d("NotificationListener", "알림 필드: $key -> ${extras.get(key)}")
                }

                // 🔹 유튜브 영상 제목 가져오기
                val title = extras.getString("android.title") // ✅ 유튜브 제목 가져오기 시도
                Log.d("NotificationListener", "유튜브 현재 재생 중: $title")

                if (!title.isNullOrEmpty()) {
                    lastYouTubeTitle = title // 🔹 최근 감지한 유튜브 영상 제목 저장
                }
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        Log.d("NotificationListener", "알림 제거됨: ${sbn?.packageName}")
    }

    fun getCurrentYouTubeVideoTitle(): String? {
        Log.d("NotificationListener", "최근 감지한 유튜브 영상 제목 반환: $lastYouTubeTitle")
        return lastYouTubeTitle
    }
}
