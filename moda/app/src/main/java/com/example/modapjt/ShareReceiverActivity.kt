package com.example.modapjt


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.modapjt.data.repository.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class ShareReceiverActivity : ComponentActivity() {
    private val repository = CardRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("ShareReceiver", "onCreate 시작")

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
                Log.d("ShareReceiver", "공유된 URL: $url")
                lifecycleScope.launch {
                    val expandedUrl = expandShortUrl(url)
                    Log.d("ShareReceiver", "변환된 URL: $expandedUrl")
                    handleUrl(expandedUrl)
                }
            } ?: run {
                Log.d("ShareReceiver", "공유된 URL이 null")
                finish()
            }
        } else {
            Log.d("ShareReceiver", "Intent 조건 불일치")
            finish()
        }
    }

    private suspend fun expandShortUrl(shortUrl: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(shortUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
                connection.connect()

                val expandedUrl = connection.getHeaderField("Location")
                connection.disconnect()

                if (expandedUrl != null) {
                    return@withContext expandedUrl
                }
            } catch (e: Exception) {
                Log.e("ShareReceiver", "URL 확장 실패", e)
            }
            return@withContext shortUrl // 실패하면 원래 URL 반환
        }
    }

    private fun handleUrl(url: String) {
        lifecycleScope.launch {
            try {
                Log.d("ShareReceiver", "최종 처리할 URL: $url")

                withContext(Dispatchers.IO) {
                    repository.createCard(url)
                }

                Toast.makeText(
                    applicationContext,
                    "공유하기 버튼으로 정보 저장 성공!",
                    Toast.LENGTH_SHORT
                ).show()

                delay(1000)

            } catch (e: Exception) {
                Log.e("ShareReceiver", "URL 저장 실패", e)
                Toast.makeText(
                    applicationContext,
                    "URL 저장 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                finish()
            }
        }
    }
}


//
//import android.content.Intent
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
//
//
//class ShareReceiverActivity : ComponentActivity() {
//    private val repository = CardRepository()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        Log.d("ShareReceiver", "onCreate 시작")
//
//        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
//            intent.getStringExtra(Intent.EXTRA_TEXT)?.let { url ->
//                Log.d("ShareReceiver", "공유된 URL: $url")
//                handleUrl(url)
//            } ?: run {
//                Log.d("ShareReceiver", "공유된 URL이 null")
//                finish()
//            }
//        } else {
//            Log.d("ShareReceiver", "Intent 조건 불일치")
//            finish()
//        }
//    }
//
//    private fun handleUrl(url: String) {
//        lifecycleScope.launch {
//            try {
//                val finalUrl = if (!url.startsWith("http")) "https://$url" else url
//                Log.d("ShareReceiver", "처리할 URL: $finalUrl")
//
//                withContext(Dispatchers.IO) {
//                    repository.createCard(finalUrl)
//                }
//
//                Toast.makeText(
//                    applicationContext,
//                    "공유하기 버튼으로 정보 저장 성공!",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                delay(1000)
//
//            } catch (e: Exception) {
//                Log.e("ShareReceiver", "URL 저장 실패", e)
//                Toast.makeText(
//                    applicationContext,
//                    "URL 저장 실패: ${e.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } finally {
//                finish()
//            }
//        }
//    }
//}

