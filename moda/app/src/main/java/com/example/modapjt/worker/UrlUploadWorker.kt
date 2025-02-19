package com.example.modapjt.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.modapjt.data.repository.CardRepository
import java.net.HttpURLConnection
import java.net.URL

class UrlUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val repository = CardRepository()

    override suspend fun doWork(): Result {
        val url = inputData.getString("url") ?: return Result.failure()

        return try {
            Log.d("UrlUploadWorker", "URL 저장 시작: $url")
            val expandedUrl = expandShortUrl(url)

            repository.createCard(expandedUrl)
                .fold(
                    onSuccess = {
                        showNotification("URL이 성공적으로 저장되었습니다")
                        Result.success()
                    },
                    onFailure = { e ->
//                        showNotification("URL 저장 실패: ${e.message}")
                        Result.failure()
                    }
                )
        } catch (e: Exception) {
//            showNotification("URL 저장 실패: ${e.message}")
            Result.failure()
        }
    }

    private suspend fun expandShortUrl(shortUrl: String): String {
        return try {
            val url = URL(shortUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.connect()

            val expandedUrl = connection.getHeaderField("Location")
            connection.disconnect()

            expandedUrl ?: shortUrl
        } catch (e: Exception) {
            Log.e("UrlUploadWorker", "URL 확장 실패", e)
            shortUrl
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "url_upload",
                "URL 저장",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "url_upload")
            .setSmallIcon(android.R.drawable.ic_menu_save)
            .setContentTitle("모다")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(2, notification)
    }
}
