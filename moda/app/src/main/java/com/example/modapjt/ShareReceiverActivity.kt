////package com.example.modapjt
////
////
////import android.content.Intent
////import android.os.Bundle
////import android.util.Log
////import android.widget.Toast
////import androidx.activity.ComponentActivity
////import androidx.lifecycle.lifecycleScope
////import com.example.modapjt.data.repository.CardRepository
////import kotlinx.coroutines.Dispatchers
////import kotlinx.coroutines.delay
////import kotlinx.coroutines.launch
////import kotlinx.coroutines.withContext
////import java.net.HttpURLConnection
////import java.net.URL
////
////class ShareReceiverActivity : ComponentActivity() {
////    private val repository = CardRepository()
////
////    // onCreate 메서드 - 공유된 URL 확인 및 처리 시작
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
////        Log.d("ShareReceiver", "onCreate 시작")
////
////        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
////            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
////                Log.d("ShareReceiver", "공유된 URL: $url")
////                lifecycleScope.launch {
////                    val expandedUrl = expandShortUrl(url)
////                    Log.d("ShareReceiver", "변환된 URL: $expandedUrl")
////                    handleUrl(expandedUrl)
////                }
////            } ?: run {
////                Log.d("ShareReceiver", "공유된 URL이 null")
////                finish()
////            }
////        } else {
////            Log.d("ShareReceiver", "Intent 조건 불일치")
////            finish()
////        }
////    }
////
////    // expandShortUrl - 단축 URL 확장 기능
////    private suspend fun expandShortUrl(shortUrl: String): String {
////        return withContext(Dispatchers.IO) {
////            try {
////                val url = URL(shortUrl)
////                val connection = url.openConnection() as HttpURLConnection
////                connection.instanceFollowRedirects = false
////                connection.connect()
////
////                val expandedUrl = connection.getHeaderField("Location")
////                connection.disconnect()
////
////                if (expandedUrl != null) {
////                    return@withContext expandedUrl
////                }
////            } catch (e: Exception) {
////                Log.e("ShareReceiver", "URL 확장 실패", e)
////            }
////            return@withContext shortUrl // 실패하면 원래 URL 반환
////        }
////    }
////
////    // handleUrl - 최종 URL 저장
////    private fun handleUrl(url: String) {
////        lifecycleScope.launch {
////            try {
////                Log.d("ShareReceiver", "최종 처리할 URL: $url")
////
////                withContext(Dispatchers.IO) {
////                    repository.createCard(url)
////                }
////
////                Toast.makeText(
////                    applicationContext,
////                    "공유하기 버튼으로 정보 저장 성공!",
////                    Toast.LENGTH_SHORT
////                ).show()
////
////                delay(1000)
////
////            } catch (e: Exception) {
////                Log.e("ShareReceiver", "URL 저장 실패", e)
////                Toast.makeText(
////                    applicationContext,
////                    "URL 저장 실패: ${e.message}",
////                    Toast.LENGTH_SHORT
////                ).show()
////            } finally {
////                finish()
////            }
////        }
////    }
////}
////
////
//////
//////import android.content.Intent
//////import android.os.Bundle
//////import android.util.Log
//////import android.widget.Toast
//////import androidx.activity.ComponentActivity
//////import androidx.lifecycle.lifecycleScope
//////import com.example.modapjt.data.repository.CardRepository
//////import kotlinx.coroutines.Dispatchers
//////import kotlinx.coroutines.delay
//////import kotlinx.coroutines.launch
//////import kotlinx.coroutines.withContext
//////
//////
//////class ShareReceiverActivity : ComponentActivity() {
//////    private val repository = CardRepository()
//////
//////    override fun onCreate(savedInstanceState: Bundle?) {
//////        super.onCreate(savedInstanceState)
//////
//////        Log.d("ShareReceiver", "onCreate 시작")
//////
//////        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
//////            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
//////                Log.d("ShareReceiver", "공유된 URL: $url")
//////                handleUrl(url)
//////            } ?: run {
//////                Log.d("ShareReceiver", "공유된 URL이 null")
//////                finish()
//////            }
//////        } else {
//////            Log.d("ShareReceiver", "Intent 조건 불일치")
//////            finish()
//////        }
//////    }
//////
//////    private fun handleUrl(url: String) {
//////        lifecycleScope.launch {
//////            try {
//////                val finalUrl = if (!url.startsWith("http")) "https://$url" else url
//////                Log.d("ShareReceiver", "처리할 URL: $finalUrl")
//////
//////                withContext(Dispatchers.IO) {
//////                    repository.createCard(finalUrl)
//////                }
//////
//////                Toast.makeText(
//////                    applicationContext,
//////                    "공유하기 버튼으로 정보 저장 성공!",
//////                    Toast.LENGTH_SHORT
//////                ).show()
//////
//////                delay(1000)
//////
//////            } catch (e: Exception) {
//////                Log.e("ShareReceiver", "URL 저장 실패", e)
//////                Toast.makeText(
//////                    applicationContext,
//////                    "URL 저장 실패: ${e.message}",
//////                    Toast.LENGTH_SHORT
//////                ).show()
//////            } finally {
//////                finish()
//////            }
//////        }
//////    }
//////}
////
//package com.example.modapjt
//
//import android.content.Intent
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.modapjt.data.repository.CardRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//import java.net.HttpURLConnection
//import java.net.URL
//
//class ShareReceiverActivity : ComponentActivity() {
//    private val repository = CardRepository()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        Log.d("ShareReceiver", "onCreate 시작")
//
//        when {
//            intent?.action == Intent.ACTION_SEND && intent.type == "text/plain" -> {
//                handleSharedText(intent)
//            }
//            intent?.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true -> {
//                handleSharedImage(intent)
//            }
//            else -> {
//                Log.d("ShareReceiver", "지원되지 않는 공유 타입")
//                finish()
//            }
//        }
//    }
//
//    /**
//     * 텍스트(URL) 공유 처리
//     */
//    private fun handleSharedText(intent: Intent) {
//        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
//            Log.d("ShareReceiver", "공유된 URL: $url")
//            lifecycleScope.launch {
//                val expandedUrl = expandShortUrl(url)
//                Log.d("ShareReceiver", "변환된 URL: $expandedUrl")
//                handleUrl(expandedUrl)
//            }
//        } ?: run {
//            Log.d("ShareReceiver", "공유된 URL이 null")
//            finish()
//        }
//    }
//
//    /**
//     * 이미지 공유 처리
//     */
//    private fun handleSharedImage(intent: Intent) {
//        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
//
//        if (imageUri != null) {
//            Log.d("ShareReceiver", "공유된 이미지 URI: $imageUri")
//            lifecycleScope.launch {
//                val imageFile = saveImageToFile(imageUri)
//                if (imageFile != null) {
//                    uploadImage(imageFile)
//                } else {
//                    Log.e("ShareReceiver", "이미지 저장 실패")
//                    Toast.makeText(applicationContext, "이미지 저장 실패", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        } else {
//            Log.d("ShareReceiver", "공유된 이미지 URI가 null")
//            finish()
//        }
//    }
//
//    /**
//     * 단축 URL 확장 (기존 로직 유지)
//     */
//    private suspend fun expandShortUrl(shortUrl: String): String {
//        return withContext(Dispatchers.IO) {
//            try {
//                val url = URL(shortUrl)
//                val connection = url.openConnection() as HttpURLConnection
//                connection.instanceFollowRedirects = false
//                connection.connect()
//
//                val expandedUrl = connection.getHeaderField("Location")
//                connection.disconnect()
//
//                if (expandedUrl != null) {
//                    return@withContext expandedUrl
//                }
//            } catch (e: Exception) {
//                Log.e("ShareReceiver", "URL 확장 실패", e)
//            }
//            return@withContext shortUrl // 실패하면 원래 URL 반환
//        }
//    }
//
//    /**
//     * URL 저장 처리 (기존 로직 유지)
//     */
//    private fun handleUrl(url: String) {
//        lifecycleScope.launch {
//            try {
//                Log.d("ShareReceiver", "최종 처리할 URL: $url")
//
//                withContext(Dispatchers.IO) {
//                    repository.createCard(url)
//                }
//
//                Toast.makeText(applicationContext, "URL 저장 성공!", Toast.LENGTH_SHORT).show()
//                delay(1000)
//
//            } catch (e: Exception) {
//                Log.e("ShareReceiver", "URL 저장 실패", e)
//                Toast.makeText(applicationContext, "URL 저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
//            } finally {
//                finish()
//            }
//        }
//    }
//
//    /**
//     * 공유된 이미지를 내부 캐시에 저장하는 함수
//     */
//    private suspend fun saveImageToFile(uri: Uri): File? {
//        return withContext(Dispatchers.IO) {
//            try {
//                val inputStream = contentResolver.openInputStream(uri) ?: return@withContext null
//                val file = File(cacheDir, "shared_image_${System.currentTimeMillis()}.jpg")
//
//                val outputStream = FileOutputStream(file)
//                inputStream.copyTo(outputStream)
//
//                inputStream.close()
//                outputStream.close()
//
//                Log.d("ShareReceiver", "이미지 저장 완료: ${file.absolutePath}")
//                file
//            } catch (e: IOException) {
//                Log.e("ShareReceiver", "이미지 저장 실패", e)
//                null
//            }
//        }
//    }
//
//    /**
//     * 서버로 이미지 업로드
//     */
//    private fun uploadImage(imageFile: File) {
//        lifecycleScope.launch {
//            try {
//                Log.d("ShareReceiver", "이미지 서버 업로드 시작: ${imageFile.absolutePath}")
//
//                val result = withContext(Dispatchers.IO) {
//                    repository.createCardWithImage(imageFile)
//                }
//
//                if (result.isSuccess) {
//                    Toast.makeText(applicationContext, "이미지 저장 성공!", Toast.LENGTH_SHORT).show()
//                    Log.d("ShareReceiver", "이미지 저장 성공")
//                } else {
//                    Toast.makeText(applicationContext, "이미지 저장 실패", Toast.LENGTH_SHORT).show()
//                    Log.e("ShareReceiver", "이미지 저장 실패: ${result.exceptionOrNull()?.message}")
//                }
//            } catch (e: Exception) {
//                Log.e("ShareReceiver", "이미지 업로드 실패", e)
//                Toast.makeText(applicationContext, "이미지 업로드 실패: ${e.message}", Toast.LENGTH_SHORT).show()
//            } finally {
//                finish()
//            }
//        }
//    }
//}

package com.example.modapjt

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.modapjt.worker.ImageUploadWorker
import com.example.modapjt.worker.UrlUploadWorker
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ShareReceiverActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 투명 배경 설정
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when {
            intent?.action == Intent.ACTION_SEND && intent.type == "text/plain" -> {
                handleSharedText(intent)
            }
            intent?.action == Intent.ACTION_SEND && intent.type?.startsWith("image/") == true -> {
                handleSharedImage(intent)
            }
            else -> {
                Log.d("ShareReceiver", "지원되지 않는 공유 타입")
                finish()
            }
        }
    }

    private fun handleSharedText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
            Log.d("ShareReceiver", "공유된 URL: $url")

            // WorkManager로 URL 저장 작업 시작
            val uploadWorkRequest = OneTimeWorkRequestBuilder<UrlUploadWorker>()
                .setInputData(workDataOf("url" to url))
                .build()

            WorkManager.getInstance(applicationContext)
                .enqueue(uploadWorkRequest)

            Toast.makeText(this, "URL 저장을 시작합니다", Toast.LENGTH_SHORT).show()

        } ?: run {
            Log.d("ShareReceiver", "공유된 URL이 null")
            Toast.makeText(this, "URL이 없습니다", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    private fun handleSharedImage(intent: Intent) {
        val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)

        if (imageUri != null) {
            Log.d("ShareReceiver", "공유된 이미지 URI: $imageUri")
            lifecycleScope.launch {
                try {
                    val file = saveImageToFile(imageUri)
                    if (file != null) {
                        val uploadWorkRequest = OneTimeWorkRequestBuilder<ImageUploadWorker>()
                            .setInputData(workDataOf("image_path" to file.absolutePath))
                            .build()

                        WorkManager.getInstance(applicationContext)
                            .enqueue(uploadWorkRequest)

                        Toast.makeText(this@ShareReceiverActivity,
                            "이미지 업로드를 시작합니다",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Log.e("ShareReceiver", "이미지 처리 실패", e)
                    Toast.makeText(this@ShareReceiverActivity,
                        "이미지 처리 실패: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                finish()
            }
        } else {
            Log.d("ShareReceiver", "공유된 이미지 URI가 null")
            finish()
        }
    }

    private suspend fun saveImageToFile(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(cacheDir, "shared_image_${System.currentTimeMillis()}.jpg")

            FileOutputStream(file).use { output ->
                inputStream.use { input ->
                    input.copyTo(output)
                }
            }

            Log.d("ShareReceiver", "이미지 저장 완료: ${file.absolutePath}")
            file
        } catch (e: IOException) {
            Log.e("ShareReceiver", "이미지 저장 실패", e)
            null
        }
    }
}
