package com.example.modapjt.toktok

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {
    companion object {
        const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "screen_capture_service"
    }

    /**
     * 포그라운드 서비스를 위한 알림 생성
     */
    fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("모다")
            .setContentText("화면 캡처 기능을 활성화하였습니다.")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "모다",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
