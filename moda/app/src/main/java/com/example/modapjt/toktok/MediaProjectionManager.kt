package com.example.modapjt.toktok

import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager

class MediaProjectionManager(private val context: Context) {
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null

    private val screenWidth = context.resources.displayMetrics.widthPixels
    private val screenHeight = context.resources.displayMetrics.heightPixels
    private val screenDensity = context.resources.displayMetrics.densityDpi

    fun startProjection(resultCode: Int, data: Intent) {
        val projectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection = projectionManager.getMediaProjection(resultCode, data)

        mediaProjection?.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                virtualDisplay?.release()
                mediaProjection = null
            }
        }, null)

        setupVirtualDisplay()
    }

    private fun setupVirtualDisplay() {
        imageReader?.close()
        imageReader = ImageReader.newInstance(
            screenWidth, screenHeight,
            android.graphics.PixelFormat.RGBA_8888, 2
        )

        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "ScreenCapture",
            screenWidth, screenHeight, screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader?.surface, null, null
        )

        imageReader?.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            image?.use {
                ScreenCaptureManager.saveImage(image)
            }
        }, null)
    }

    fun release() {
        ScreenCaptureManager.clearCapturedBitmap()
        imageReader?.close()
        imageReader = null
        virtualDisplay?.release()
        mediaProjection?.stop()
    }
}
