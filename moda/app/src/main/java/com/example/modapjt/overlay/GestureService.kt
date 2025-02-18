package com.example.modapjt.overlay

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class GestureService : LifecycleService(), SavedStateRegistryOwner {
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private val context = this@GestureService
    private var windowManager: WindowManager? = null

    // 메인 제스처 뷰 (사용자가 제스처할 수 있는 배경)
    private var view: ComposeView? = null
    private lateinit var params: WindowManager.LayoutParams

    // 화면 크기 관련 변수들
    private val screenWidth by lazy { resources.displayMetrics.widthPixels }
    private val screenHeight by lazy { resources.displayMetrics.heightPixels }
    private val screenDensity by lazy { resources.displayMetrics.densityDpi }

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

        Log.d("GestureService", "제스처 서비스 시작됨")
    }

    private fun handleDoubleTap() {
        Log.d("GestureService", "Received double tap")

        windowManager?.addView(view, params)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setupGestureView() {
        params = WindowManager.LayoutParams(
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

        view = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setViewTreeLifecycleOwner(this@GestureService)
            setViewTreeSavedStateRegistryOwner(this@GestureService)

            setContent {
                // 시스템 다크모드 감지
                val isDarkTheme = isSystemInDarkTheme()

                // 다크모드에 따른 색상 설정
                val backgroundColor = if (isDarkTheme) {
                    Color(0x80000000) // 다크모드: 반투명 검정 (알파값 50%)
                } else {
                    Color(0x80FFFFFF) // 라이트모드: 반투명 흰색 (알파값 50%)
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = backgroundColor,
                            shape = RectangleShape
                        )
                )
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
    }
}