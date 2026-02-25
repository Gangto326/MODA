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
import java.io.File

class ImageUploadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val repository = CardRepository()

    override suspend fun doWork(): Result {
        val imagePath = inputData.getString("image_path") ?: return Result.failure()
        val file = File(imagePath)

        if (!file.exists()) {
            Log.e("ImageUploadWorker", "파일이 존재하지 않음: $imagePath")
            return Result.failure()
        }

        return try {
            Log.d("ImageUploadWorker", "이미지 업로드 시작: $imagePath")
            val result = repository.createCardWithImage(file)

            result.fold(
                onSuccess = {
                    showNotification("이미지가 성공적으로 저장되었습니다")
                    Log.d("ImageUploadWorker", "이미지 업로드 성공")
                    Result.success()
                },
                onFailure = { e ->
//                    showNotification("이미지 저장 실패: ${e.message}")
                    Log.e("ImageUploadWorker", "이미지 업로드 실패", e)
                    Result.failure()
                }
            )
        } catch (e: Exception) {
//            showNotification("이미지 저장 실패: ${e.message}")
            Log.e("ImageUploadWorker", "이미지 업로드 실패", e)
            Result.failure()
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "image_upload",
                "이미지 업로드",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "image_upload")
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setContentTitle("모다")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }
}
