package com.example.modapjt.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.modapjt.MainActivity
import com.example.modapjt.R
import com.example.modapjt.data.api.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class MyFirebaseMessagingService : FirebaseMessagingService()  {
    private val api = RetrofitInstance.fcmTokenApi
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override fun onCreate() {
        super.onCreate()
    }
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val cardId = remoteMessage.data["cardId"]  // data에서 cardId 추출

        println("cardId : " + cardId)
        remoteMessage.notification?.let { notification ->
            sendNotification(
                title = notification.title ?: "모다모다",
                body = notification.body ?: "",
                cardId = cardId
            )
        }
    }

    private fun sendNotification(title: String, body: String, cardId: String?) {
        val channelId = "modamoda_default"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android 8.0 이상 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "모다모다 알림", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // cardId를 포함한 Intent 생성
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("cardId", cardId)  // cardId 추가
        }

        // PendingIntent 생성
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)  // 알림 클릭 시 이동할 PendingIntent 설정

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FCMTokenManager.setToken(token)
    }
}