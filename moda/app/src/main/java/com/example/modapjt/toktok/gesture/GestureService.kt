package com.example.modapjt.toktok.gesture

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.modapjt.toktok.BrowserAccessibilityService
import com.example.modapjt.toktok.ScreenCaptureManager
import com.example.modapjt.toktok.ScreenCaptureManager.capturedBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.roundToInt

class GestureService : LifecycleService(), SavedStateRegistryOwner {
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private val context = this@GestureService
    private var windowManager: WindowManager? = null

    // 메인 제스처 뷰 (사용자가 제스처할 수 있는 배경)
    private var gestureView: ComposeView? = null
    private lateinit var gestureParams: WindowManager.LayoutParams

    // 화면 캡처 애니메이션을 표시할 뷰
    private var captureView: ComposeView? = null
    private lateinit var captureParams: WindowManager.LayoutParams

    private val repository = CardRepository()

    // 현재 화면 캡처 중인지 여부
    private val isCapturingAtomic = AtomicBoolean(false)
    private val _isCapturedState = MutableStateFlow(false)
    private val isCapturedState = _isCapturedState.asStateFlow()

    // 애니메이션 관련 설정
    private val duration = if (_isCapturedState.value) 1 else 1000

    // 화면 크기 관련 변수들
    private val screenWidth by lazy { resources.displayMetrics.widthPixels }
    private val screenHeight by lazy { resources.displayMetrics.heightPixels }
    private val screenDensity by lazy { resources.displayMetrics.densityDpi }

    // 화면 캡처를 위한 MediaProjection 관련 변수들
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null

    // Bradcast를 받기 위한 수신기
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.modapjt.DOUBLE_TAP_ACTION") {
                // 여기서 더블탭에 대한 처리를 합니다
                handleDoubleTap()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)

        // BroadcastReceiver 등록
        val filter = IntentFilter().apply {
            addAction("com.example.modapjt.DOUBLE_TAP_ACTION")
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)

        //오버레이 화면 등록
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        setupGestureView()
        startForeground(
            NOTIFICATION_ID,
            createNotification()
        )

        Log.d("GestureService", "제스처 서비스 시작됨")
    }

    private fun handleDoubleTap() {
        Log.d("GestureService", "Received double tap")

        gestureView = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setViewTreeLifecycleOwner(this@GestureService)
            setViewTreeSavedStateRegistryOwner(this@GestureService)

            setContent {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color(0x80000000),
                            shape = RectangleShape
                        )
                ) {
                    GestureDrawingCanvas(
                        onGestureRecognized = { result ->
                            Log.d("GestureService", result.toString())

                            detectGesture(result)
                        }
                    )
                }
            }
        }

        windowManager?.addView(gestureView, gestureParams)
    }

    private fun closeDoubleTap() {
        windowManager?.removeView(gestureView)
    }

    /**
     * 서비스가 시작될 때 호출되는 함수
     * MediaProjection 관련 데이터를 받아 화면 캡처 기능을 초기화
     */
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

    /**
     * 포그라운드 서비스를 위한 알림 생성
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(): Notification {
        val channelId = "screen_capture_service"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel
        val channel = NotificationChannel(
            channelId,
            "모다",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("모다")
            .setContentText("화면 캡처 기능을 활성화하였습니다.")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .build()
    }

    /**
     * MediaProjection을 시작하여 화면 캡처 기능 초기화
     */
    private fun startProjection(resultCode: Int, data: Intent) {
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

    /**
     * 가상 디스플레이 설정 및 이미지 캡처 리스너 등록
     */
    private fun setupVirtualDisplay() {
        imageReader?.close()  // 기존 ImageReader 정리
        imageReader = ImageReader.newInstance(
            screenWidth, screenHeight,
            PixelFormat.RGBA_8888, 2  // 버퍼 크기를 2로 증가
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

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setupGestureView() {
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
            setViewTreeLifecycleOwner(this@GestureService)
            setViewTreeSavedStateRegistryOwner(this@GestureService)

            setContent {
                val isCaptured by isCapturedState.collectAsState()

                val scale by animateFloatAsState(
                    targetValue = if (isCaptured) 0f else 1f,
                    animationSpec = tween(
                        durationMillis = duration,
                        easing = EaseInOutCirc
                    )
                )

                val bounce by animateFloatAsState(
                    targetValue = if (isCaptured) 1f else 1.1f,
                    animationSpec = tween(
                        durationMillis = duration / 2,
                        easing = EaseInOutCirc
                    )
                )

                val offsetX by animateFloatAsState(
                    targetValue =
                    if (isCaptured)
                        (screenWidth / 2).toFloat()
                    else
                        0f,
                    animationSpec = tween(
                        durationMillis = duration,
                        easing = EaseInOutCirc
                    )
                )

                val offsetY by animateFloatAsState(
                    targetValue =
                    if (isCaptured)
                        screenHeight.toFloat()
                    else
                        0f,
                    animationSpec = tween(
                        durationMillis = duration,
                        easing = EaseInOutCirc
                    )
                )

                if (isCaptured) {
                    Box(
                        modifier = Modifier.offset {
                            IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        capturedBitmap?.let { bmp ->
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size((screenWidth * scale).dp, (screenHeight * scale).dp)
                                    .graphicsLayer(
                                        scaleX = bounce,
                                        scaleY = bounce,
                                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                                    ),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }

        gestureParams = WindowManager.LayoutParams(
            screenWidth,
            screenHeight,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            dimAmount = 0.5f //배경 투명도 설정 (0.0f ~ 1.0f)
            flags = flags or WindowManager.LayoutParams.FLAG_BLUR_BEHIND //블러 효과 설정
            blurBehindRadius = 25 //블러 효과의 강도 설정 (1 ~ 100)
        }

        windowManager?.addView(captureView, captureParams)
    }

    private fun detectGesture(gesture: Pair<String, Double>?) {
        try {
            //제스처가 감지되지 않았거나, 기준을 넘은 제스처가 없는 경우
            if (gesture == null || gesture.second < 4) {
                Toast.makeText(context, "제스처가 취소되었습니다.", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            //제스처 이름이 circle이 포함하는 경우, 전체 url 저장
            if (gesture.first.contains("circle")) {
                if (gesture.second < 6) {
                    Toast.makeText(context, "원을 더 정확하게 그려주세요.", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                captureImage()
                Log.d("GestureService", "이미지 캡처")
            }

            //제스처 이름이 check를 포함하는 경우, 해당 이미지 저장
            if (gesture.first.contains("check")) {

                captureUrl()
                Log.d("GestureService", "URL 캡처")
            }
        } finally {
            closeDoubleTap()
        }
    }

    private fun captureImage() {
        if (!isCapturingAtomic.compareAndSet(false, true)) {
            Log.d("OverlayService", "캡처가 불가능합니다.")
            Toast.makeText(applicationContext, "잠시 후 시도해 주십시오.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {  // 또는 lifecycleScope, 상황에 맞는 스코프 사용
            try {
                Toast.makeText(applicationContext, "이미지 캡처가 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val file = ScreenCaptureManager.bitmapToFile(context)
                repository.createCardWithImage(file)
            } finally {
                isCapturingAtomic.set(false)
            }
        }
    }

    /**
     * 현재 URL 캡처 및 저장
     * 화면 캡처 후 애니메이션을 통해 시각적 피드백 제공
     */
    private fun captureUrl() {
        if (!isCapturingAtomic.compareAndSet(false, true)) {
            Log.d("OverlayService", "캡처가 불가능합니다.")
            Toast.makeText(applicationContext, "잠시 후 시도해 주십시오.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    ScreenCaptureManager.captureBitmap() // 현재 화면을 캡처
                    _isCapturedState.value = true
                    delay(duration.toLong())
                    _isCapturedState.value = false
                    delay(duration.toLong())
                    ScreenCaptureManager.clearCapturedBitmap() // 캡처된 비트맵 정리
                }

                var url = BrowserAccessibilityService.currentUrl ?: "Unknown URL"
                if (!url.let { Patterns.WEB_URL.matcher(it).matches() }) {
                    Log.d("OverlayService", "URL 감지 실패")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "링크를 감지할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                Log.d("OverlayService", "요청할 URL: $url")
                saveToDatabase(url)
            } finally {
                isCapturingAtomic.set(false)
            }
        }
    }

    private fun saveToDatabase(url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var finalUrl = url

                // URL이 http:// 또는 https://로 시작하지 않으면 자동으로 추가
                if (!finalUrl.startsWith("https://")) {
                    finalUrl = "https://$finalUrl"
                    Log.d("OverlayService", "URL 보정됨: $finalUrl") // URL 수정된 것 로그로 확인
                }

                // 보정 후에도 여전히 URL 형식이 맞지 않으면 저장하지 않음
                if (!finalUrl.startsWith("https://")) {
                    Log.e("OverlayService", "잘못된 URL 형식 (보정 실패): $finalUrl")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "링크를 감지할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                repository.createCard(finalUrl) // 백엔드 서버와 통신하여 카드 저장
                Log.d("OverlayService", "URL 저장 완료: $finalUrl") // 저장 로그 추가
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(applicationContext, "정보 저장 성공!", Toast.LENGTH_SHORT).show()
//                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("OverlayService", "URL 저장 중 오류 발생: ${e.message}")
                withContext(Dispatchers.Main) {
//                    Toast.makeText(applicationContext, "링크 저장 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // BroadcastReceiver 해제
        unregisterReceiver(receiver)

        imageReader?.close()
        imageReader = null
        virtualDisplay?.release()
        mediaProjection?.stop()
        ScreenCaptureManager.clearCapturedBitmap()

        if (captureView?.isAttachedToWindow == true) {
            windowManager?.removeView(captureView)
            Log.d("GestureService", "캡처 뷰 제거")
        }

        if (gestureView?.isAttachedToWindow == true) {
            windowManager?.removeView(gestureView)
            Log.d("GuestureService", "제스처 뷰 제거")
        }

        Log.d("GestureService", "제스처 서비스 종료됨")
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        const val EXTRA_RESULT_CODE = "extra_result_code"
        const val EXTRA_DATA = "extra_data"
    }
}