//package com.example.modapjt
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import android.widget.Toast
//
//class ShareReceiverActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // 공유된 데이터 받기
//        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
//            handleSharedText(intent)
//        } else if (intent?.action == Intent.ACTION_SEND_MULTIPLE && intent.type == "text/plain") {
//            handleSharedMultipleText(intent)
//        }
//
//        finish() // 액티비티 종료 (UI 없이 백그라운드에서 처리)
//    }
//
//    private fun handleSharedText(intent: Intent) {
//        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
//        if (!sharedText.isNullOrBlank()) {
//            saveToDatabase(sharedText)
//        }
//    }
//
//    private fun handleSharedMultipleText(intent: Intent) {
//        val sharedTexts = intent.getStringArrayListExtra(Intent.EXTRA_TEXT)
//        sharedTexts?.forEach { text ->
//            saveToDatabase(text)
//        }
//    }
//
//    private fun saveToDatabase(text: String) {
//        // 여기서 DB 저장 로직 추가 (예: Room DB에 URL 저장)
//        Toast.makeText(this, "링크 저장됨: $text", Toast.LENGTH_SHORT).show()
//    }
//}
//
//// Unresolved reference: app
//// Unresolved reference: AppCompatActivity
//// 'onCreate' overrides nothing
//// Unresolved reference: onCreate
//// Unresolved reference: intent
//// <html>None of the following functions can be called with the arguments supplied:<br/>public open fun makeText(context: Context!, text: CharSequence!, duration: Int): Toast! defined in android.widget.Toast<br/>public open fun makeText(context: Context!, resId: Int, duration: Int): Toast! defined in android.widget.Toast
//
//




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
                saveToDatabase(it) // ✅ DB에 저장
            }
        }

        finish() // 액티비티 종료 (UI 없이 백그라운드에서 처리)
    }

    private fun saveToDatabase(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.insert(url) // ✅ RoomDB에 URL 저장
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



// Unresolved reference: repository
// <html>Unresolved reference. None of the following candidates is applicable because of receiver type mismatch:<br/>public inline fun kotlin.text.StringBuilder /* = java.lang.StringBuilder */.insert(index: Int, value: Byte): kotlin.text.StringBuilder /* = java.lang.StringBuilder */ defined in kotlin.text<br/>public inline fun kotlin.text.StringBuilder /* = java.lang.StringBuilder */.insert(index: Int, value: Short): kotlin.text.StringBuilder /* = java.lang.StringBuilder */ defined in kotlin.text