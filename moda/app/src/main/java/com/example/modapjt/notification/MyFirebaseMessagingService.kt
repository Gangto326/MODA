package com.example.modapjt.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.example.modapjt.MainActivity
import com.example.modapjt.R
import com.example.modapjt.data.api.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@ExperimentalMaterial3Api
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

        val cardId = remoteMessage.data["cardId"]  // Extract cardId from data
        val imageUrl = remoteMessage.data["imageUrl"]  // Extract imageUrl from data

        println("cardId : $cardId")
        println("imageUrl : $imageUrl")

        println("Notification data:")


        remoteMessage.notification?.let { notification ->
            println("Title: ${notification.title}")
            println("Body: ${notification.body}")

            sendNotification(
                title = notification.title ?: "모다모다",
                body = notification.body ?: "",
                cardId = cardId,
                imageUrl = imageUrl
            )
        }
    }

    private fun sendNotification(title: String, body: String, cardId: String?, imageUrl: String?) {
        val channelId = "modamoda_default"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "모다모다 알림", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

            // cardId를 인텐트에 추가
            putExtra("cardId", cardId)
        }



        val pendingIntent = PendingIntent.getActivity(
            this,
            cardId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon_round) // 나중에 바뀌어야 할 부분.
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        if (!imageUrl.isNullOrEmpty()) {
            try {
                val bitmap = Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()

                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null as Bitmap?)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FCMTokenManager.setToken(token)
    }
}