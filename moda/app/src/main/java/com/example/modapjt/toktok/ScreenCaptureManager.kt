package com.example.modapjt.toktok

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ScreenCaptureManager {
    // 실시간으로 갱신되는 bitmap
    private var _currentBitmap by mutableStateOf<Bitmap?>(null)
    val currentBitmap: Bitmap? get() = _currentBitmap

    // 캡처 시점의 bitmap
    private var _capturedBitmap by mutableStateOf<Bitmap?>(null)
    val capturedBitmap: Bitmap? get() = _capturedBitmap

    @Synchronized
    fun saveImage(image: Image) {
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * image.width

        // 새 비트맵 생성
        val newBitmap = Bitmap.createBitmap(
            image.width + rowPadding / pixelStride,
            image.height,
            Bitmap.Config.ARGB_8888
        )
        newBitmap.copyPixelsFromBuffer(buffer)

        // 이전 비트맵 임시 저장
        val oldBitmap = _currentBitmap

        // 새 비트맵 설정
        _currentBitmap = newBitmap

        // 이전 비트맵 안전하게 해제
        oldBitmap?.recycle()
    }

    @Synchronized
    fun captureBitmap() {
        // 현재 bitmap을 복사하여 저장
        _currentBitmap?.let { bitmap ->
            val capturedBitmap = bitmap.copy(bitmap.config ?: Bitmap.Config.ARGB_8888, true)

            // 새 비트맵 설정
            _capturedBitmap = capturedBitmap
        }
    }

    fun clearCapturedBitmap() {
        _currentBitmap?.recycle()
        _currentBitmap = null
        _capturedBitmap?.recycle()
        _capturedBitmap = null
    }

    fun bitmapToFile(context: Context): File {
        // 임시 파일 생성
        val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")

        // Bitmap을 파일로 저장
        try {
            FileOutputStream(file).use { out ->
                currentBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
    }
}