package com.example.modapjt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.modapjt.data.AppDatabase
import com.example.modapjt.data.CaptureRepository
import kotlinx.coroutines.*


class ShareReceiverActivity : AppCompatActivity() {
    private lateinit var repository: CaptureRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // RoomDB 초기화
        val db = AppDatabase.getDatabase(this)
        repository = CaptureRepository(db.captureDao())

        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            sharedText?.let {
                saveToDatabase(it) // DB에 저장
            }
        }

        finish() // 액티비티 종료 (UI 없이 백그라운드에서 처리)
    }

    private fun saveToDatabase(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.insert(url) // RoomDB에 URL 저장
                runOnUiThread {
                    Toast.makeText(applicationContext, "공유된 링크 저장 완료", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(applicationContext, "URL 저장 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

