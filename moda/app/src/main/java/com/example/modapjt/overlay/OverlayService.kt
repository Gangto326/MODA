package com.example.modapjt.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.modapjt.data.repository.CardRepository
import com.example.modapjt.overlay.ScreenCaptureManager.capturedBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class OverlayService : LifecycleService(), SavedStateRegistryOwner {
    private var windowManager: WindowManager? = null
    private var overlayView: ComposeView? = null
    private lateinit var params: WindowManager.LayoutParams

    private var backgroundView: ComposeView? = null
    private var backgroundParams: WindowManager.LayoutParams? = null

    private var captureView: ComposeView? = null
    private lateinit var captureParams: WindowManager.LayoutParams

    private val repository = CardRepository()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private val _isCollapsedState = MutableStateFlow(false)
    private val isCollapsedState = _isCollapsedState.asStateFlow()
    private val _isCapturedState = MutableStateFlow(false)
    private val isCapturedState = _isCapturedState.asStateFlow()
    private val _targetXState = MutableStateFlow(0)
    private val targetXState = _targetXState.asStateFlow()
    private val _targetYState = MutableStateFlow(0)
    private val targetYState = _targetYState.asStateFlow()

    private val screenWidth by lazy { resources.displayMetrics.widthPixels.toFloat() }
    private val screenHeight by lazy { resources.displayMetrics.heightPixels.toFloat() }
    private val duration = 1000
    private val easing = EaseInOutCirc
    private val iconSize by lazy { screenWidth.roundToInt() / 6 }

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        setupOverlayView()
        startForeground(
            NOTIFICATION_ID,
            createNotification()
        )
        Log.d("OverlayService", "오버레이 서비스 시작됨") // 로그 추가
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            val resultCode = it.getIntExtra(EXTRA_RESULT_CODE, 0)
            val data = it.getParcelableExtra<Intent>(EXTRA_DATA)

            if (resultCode != 0 && data != null) {
                startProjection(resultCode, data)
            }
        }
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(): Notification {
        val channelId = "screen_capture_service"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel
        val channel = NotificationChannel(
            channelId,
            "Screen Capture Service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Screen Capture Service")
            .setContentText("Recording your screen")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .build()
    }

    fun startProjection(resultCode: Int, data: Intent) {
        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)

        // Register callback before starting capture
        mediaProjection?.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                virtualDisplay?.release()
                mediaProjection = null
            }
        }, null)

        setupVirtualDisplay()
    }

    private fun setupVirtualDisplay() {
        val metrics = Resources.getSystem().displayMetrics
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        val screenDensity = metrics.densityDpi

        val imageReader = ImageReader.newInstance(
            screenWidth, screenHeight,
            PixelFormat.RGBA_8888, 1
        )

        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "ScreenCapture",
            screenWidth, screenHeight, screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface, null, null
        )

        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()

            image?.let {
                ScreenCaptureManager.saveImage(image)
                it.close()
            }
        }, null)
    }

    private fun setupOverlayView() {
        backgroundParams = WindowManager.LayoutParams(
            iconSize,
            iconSize,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = ((screenWidth - iconSize) / 2).roundToInt()
            y = (screenHeight * 0.8).roundToInt()
        }

        backgroundView = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setViewTreeLifecycleOwner(this@OverlayService)
            setViewTreeSavedStateRegistryOwner(this@OverlayService)

            setContent {
                val isCollapsed by isCollapsedState.collectAsState()

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Termination Zone Icon",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.5f),
                    tint = if (isCollapsed) Color.Red else Color.Gray
                )
            }
        }

        captureParams = WindowManager.LayoutParams(
            screenWidth.toInt(),
            screenHeight.toInt(),
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        captureView = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setViewTreeLifecycleOwner(this@OverlayService)
            setViewTreeSavedStateRegistryOwner(this@OverlayService)

            setContent {
                val isCaptured by isCapturedState.collectAsState()
                val targetX by targetXState.collectAsState()
                val targetY by targetYState.collectAsState()

                // 크기 변동
                val sizeX by animateFloatAsState(
                    targetValue = if (isCaptured) 1f else screenWidth,
                    animationSpec = tween(
                        durationMillis = (duration * 0.8).toInt(),
                        easing = easing
                    )
                )
                val sizeY by animateFloatAsState(
                    targetValue = if (isCaptured) 1f else screenHeight,
                    animationSpec = tween(
                        durationMillis = (duration * 0.8).toInt(),
                        easing = easing
                    )
                )

                //위치 이동
                val offsetX by animateFloatAsState(
                    targetValue = if (isCaptured) targetX.toFloat() + iconSize / 2 else 0f,
                    animationSpec = tween(
                        durationMillis = duration,
                        easing = easing
                    )
                )

                val offsetY by animateFloatAsState(
                    targetValue = if (isCaptured) targetY.toFloat() + iconSize / 2 else 0f,
                    animationSpec = tween(
                        durationMillis = duration,
                        easing = easing
                    )
                )

                val corner by animateIntAsState(
                    targetValue = if (isCaptured) 100 else 0,
                    animationSpec = tween(
                        durationMillis = duration,
                        easing = easing
                    )
                )

                captureParams.x = (offsetX).roundToInt()
                captureParams.y = (offsetY).roundToInt()
                windowManager?.updateViewLayout(captureView, captureParams)

                if (isCaptured) {
                    Box() {
                        capturedBitmap?.let { bmp ->
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(sizeX.dp, sizeY.dp)
                                    .clip(RoundedCornerShape(corner)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }

        params = WindowManager.LayoutParams(
            iconSize,
            iconSize,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = (screenWidth * 0.05).toInt()
            y = (screenHeight * 0.08).toInt()
        }

        overlayView = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setViewTreeLifecycleOwner(this@OverlayService)
            setViewTreeSavedStateRegistryOwner(this@OverlayService)

            setContent {
                var isSuccess by remember { mutableStateOf(false) }
                var isError by remember { mutableStateOf(false) }

                OverlayIcon(
                    onDoubleTab = { captureUrl() },
                    onDrag = { offset ->
                        movePosition(offset)
                    },
                    onDragStart = { showBackground() },
                    onDragEnd = { hideBackground() },
                    isSuccess = isSuccess,
                    isError = isError,
                    onAnimationComplete = {
                        isSuccess = false
                        isError = false
                    }
                )
            }
        }

        windowManager?.addView(captureView, captureParams)
        windowManager?.addView(overlayView, params)

        lifecycleScope.launch {
            BrowserAccessibilityService.canOverlayState.collect { canOverlay ->
                if (canOverlay) {
                    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    overlayView?.alpha = 1f
                } else {
                    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    overlayView?.alpha = 0f
                }

                overlayView?.let { view ->
                    if (view.isAttachedToWindow) {
                        windowManager?.updateViewLayout(view, params)
                    }
                }
            }
        }
    }

    private fun movePosition(offset: IntOffset) {
        params.x += offset.x
        params.y += offset.y

        _targetXState.value = params.x
        _targetYState.value = params.y
        _isCollapsedState.value = isPointInCollapsedTrash(params.x, params.y)

        Log.d("OverlayService", if (_isCollapsedState.value) "겹침" else "안겹침")

        windowManager?.updateViewLayout(overlayView, params)
    }

    private fun showBackground() {
        if (backgroundView?.isAttachedToWindow == false)
            windowManager?.addView(backgroundView, backgroundParams)
    }

    private fun hideBackground() {
        if (backgroundView?.isAttachedToWindow == true)
            windowManager?.removeView(backgroundView)

        if (isCollapsedState.value) {
            stopSelf()
        }
    }

    private fun isPointInCollapsedTrash(x: Int, y: Int): Boolean {
        return x in backgroundParams!!.x - iconSize / 2..backgroundParams!!.x + iconSize / 2 &&
                y in backgroundParams!!.y - iconSize / 2..backgroundParams!!.y + iconSize / 2
    }

    private fun showSuccessFeedback() {
        overlayView?.setContent {
            var isSuccess by remember { mutableStateOf(true) }
            OverlayIcon(
                onDoubleTab = { captureUrl() },
                onDrag = { offset ->
                    movePosition(offset)
                },
                onDragStart = { showBackground() },
                onDragEnd = { hideBackground() },
                isSuccess = isSuccess,
                isError = false,
                onAnimationComplete = { isSuccess = false }
            )
        }
    }

    private fun showErrorFeedback() {
        overlayView?.setContent {
            var isError by remember { mutableStateOf(true) }
            OverlayIcon(
                onDoubleTab = { captureUrl() },
                onDrag = { offset ->
                    movePosition(offset)
                },
                onDragStart = { showBackground() },
                onDragEnd = { hideBackground() },
                isSuccess = false,
                isError = isError,
                onAnimationComplete = { isError = false }
            )
        }
    }

    private fun captureUrl() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                ScreenCaptureManager.captureBitmap() // 현재 화면을 캡처
                _isCapturedState.value = true
                delay(duration.toLong())
                _isCapturedState.value = false
                delay(duration.toLong())
                ScreenCaptureManager.clearCapturedBitmap() // 캡처된 비트맵 정리
            }

            var url: String? = null
            for (i in 1..5) { // 5번까지 재시도
                url = BrowserAccessibilityService.currentUrl
                if (!url.isNullOrEmpty() && url != "Unknown URL") break
                Log.d("OverlayService", "URL 가져오기 시도 $i: $url") // 로그 추가
                delay(500) // 0.5초 대기 후 다시 확인
            }

            url = url ?: "Unknown URL"
            Log.d("OverlayService", "최종 URL 저장: $url") // 로그 추가

            saveToDatabase(url)
        }
    }

    private fun saveToDatabase(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var finalUrl = url

                // URL이 http:// 또는 https://로 시작하지 않으면 자동으로 추가
                if (!finalUrl.startsWith("http")) {
                    finalUrl = "https://$finalUrl"
                    Log.d("OverlayService", "URL 보정됨: $finalUrl") // URL 수정된 것 로그로 확인
                }

                // 보정 후에도 여전히 URL 형식이 맞지 않으면 저장하지 않음
                if (!finalUrl.startsWith("http")) {
                    Log.e("OverlayService", "잘못된 URL 형식 (보정 실패): $finalUrl")
                    withContext(Dispatchers.Main) { showErrorFeedback() }
                    return@launch
                }

                repository.createCard(finalUrl) // 백엔드 서버와 통신하여 카드 저장
                Log.d("OverlayService", "URL 저장 완료: $finalUrl") // 저장 로그 추가

                withContext(Dispatchers.Main) {
                    showSuccessFeedback()
                    Toast.makeText(applicationContext, "정보 저장 성공 ! $url", Toast.LENGTH_SHORT).show() //  Toast 추가
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("OverlayService", "URL 저장 중 오류 발생: ${e.message}")
                withContext(Dispatchers.Main) {
                    showErrorFeedback()
                    Toast.makeText(applicationContext, "URL 저장 실패", Toast.LENGTH_SHORT).show() // 실패 시에도 Toast 추가
                }
            }
        }
    }


    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        virtualDisplay?.release()
        mediaProjection?.stop()
        ScreenCaptureManager.clearCapturedBitmap()

        try {
            if (overlayView?.isAttachedToWindow == true) {
                windowManager?.removeView(overlayView)
                Log.d("OverlayService", "오버레이 뷰 제거")
            }
            if (captureView?.isAttachedToWindow == true) {
                windowManager?.removeView(captureView)

                Log.d("OverlayService", "캡처 뷰 제거")
            }
        } catch (e: Exception) {
            Log.d("OverlayService", "오버레이 서비스 이미 종료되어 있음")
        }
        super.onDestroy()

        OverlayStateManager.setOverlayActive(false)

        Log.d("OverlayService", "오버레이 서비스 종료됨")
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        const val EXTRA_RESULT_CODE = "extra_result_code"
        const val EXTRA_DATA = "extra_data"
    }
}




