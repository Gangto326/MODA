package com.example.modapjt

import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.modapjt.data.repository.CardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverlayService : LifecycleService(), SavedStateRegistryOwner {
    private var windowManager: WindowManager? = null
    private var overlayView: ComposeView? = null
    private lateinit var params: WindowManager.LayoutParams

    private var backgroundView: ComposeView? = null
    private var backgroundParams: WindowManager.LayoutParams? = null

    private val repository = CardRepository()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private val _isCollapsedState = MutableStateFlow(false)
    private val isCollapsedState = _isCollapsedState.asStateFlow()

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        setupOverlayView()
        Log.d("OverlayService", "오버레이 서비스 시작됨") // 로그 추가
    }

    private fun setupOverlayView() {
        backgroundParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        backgroundView = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setViewTreeLifecycleOwner(this@OverlayService)
            setViewTreeSavedStateRegistryOwner(this@OverlayService)

            setContent {
                val isCollapsed by isCollapsedState.collectAsState()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Termination Zone Icon",
                        modifier = Modifier
                            .size(60.dp)
                            .offset(y = (-60).dp)
                            .alpha(0.5f),
                        tint = if (isCollapsed) Color.Red else Color.Gray
                    )
                }
            }
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 40
            y = 80
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

        windowManager?.addView(overlayView, params)
    }

    private fun captureUrl() {
        CoroutineScope(Dispatchers.IO).launch {
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

    private fun movePosition(offset: IntOffset) {
        params.x += offset.x
        params.y += offset.y

        windowManager?.updateViewLayout(overlayView, params)

        _isCollapsedState.value = isPointInCollapsedTrash(params.x, params.y)
    }

    private fun showBackground() {
        windowManager?.addView(backgroundView, backgroundParams)
    }

    private fun hideBackground() {
        windowManager?.removeView(backgroundView)

        if (isCollapsedState.value) {
            onDestroy()
        }
    }

    private fun isPointInCollapsedTrash(x: Int, y: Int): Boolean {
        return x in 481..740 && y in 2351..2600
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
        try {
            if (overlayView?.isAttachedToWindow == true) {
                windowManager?.removeView(overlayView)
                Log.d("OverlayService", "오버레이 뷰 제거")
            }
        } catch (e: Exception) {
            Log.d("OverlayService", "오버레이 서비스 이미 종료되어 있음")
        }

        super.onDestroy()
        Log.d("OverlayService", "오버레이 서비스 종료됨")
    }
}




