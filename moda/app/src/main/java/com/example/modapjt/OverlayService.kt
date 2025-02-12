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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Termination Zone Icon",
                        modifier = Modifier
                            .size(60.dp)
                            .offset(y = (-60).dp)
                            .alpha(0.5f),
                        tint = Color.Red,
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

        Log.d("OverlayService", "${params.x}, ${params.y}")

        if (
            params.x in 501..699 &&
            params.y in 2401..2449
        )
            Log.d("OverlayService", "겹침")
        else
            Log.d("OverlayService", "안겹침")

//        val screenWidth = resources.displayMetrics.widthPixels
//        val screenHeight = resources.displayMetrics.heightPixels
//        val iconSize = 60 * resources.displayMetrics.density
//
//        // 오버레이 아이콘의 중심 좌표 계산
//        val overlayIconCenterX = params.x + iconSize/2
//        val overlayIconCenterY = params.y + iconSize/2
//
//        // 종료 영역의 중심 좌표 (백그라운드 뷰의 BottomCenter 기준)
//        val terminationZoneCenterX = screenWidth/2
//        val terminationZoneCenterY = screenHeight - iconSize
//
//        // 두 중심점 사이의 거리 계산
//        val isOverlapping =
//            Math.abs(overlayIconCenterX - terminationZoneCenterX) < iconSize &&
//                    Math.abs(overlayIconCenterY - terminationZoneCenterY) < iconSize
//
//        Log.d("OverlayService", """
//        오버레이 중심: ($overlayIconCenterX, $overlayIconCenterY)
//        종료영역 중심: ($terminationZoneCenterX, $terminationZoneCenterY)
//        겹침: $isOverlapping
//    """.trimIndent())

//        val metrics = resources.displayMetrics
//        val screenWidth = resources.displayMetrics.widthPixels
//        val screenHeight = resources.displayMetrics.heightPixels
//        val iconSize = 60 * this.resources.displayMetrics.density // 60.dp icon size
//        val paddingSize = 60 * this.resources.displayMetrics.density // 60.dp padding
//        Log.d("OverlayService", "가로: $screenWidth, 세로: $screenHeight")
//        Log.d("OverlayService", "=x좌표\n   ${screenWidth/2 - iconSize} < ${params.x} < ${screenWidth/2 + iconSize}\n" +
//                "=y좌표\n   ${(screenHeight - paddingSize - iconSize / 2) - iconSize} < ${params.y} < ${(screenHeight - paddingSize - iconSize / 2) + iconSize}")
//
//        // OverlayIcon이 Close Icon 영역에 있는지 확인
//        val isOverlapping = params.y > (screenHeight - paddingSize - iconSize / 2) - iconSize &&
//                params.y < (screenHeight - paddingSize - iconSize / 2) + iconSize &&
//                params.x > screenWidth/2 - iconSize &&
//                params.x < screenWidth/2 + iconSize
//
//        if (isOverlapping) {
//            Log.d("OverlayService", "겹침")
//            // 여기서 원하는 동작 수행
//        }
//        else
//            Log.d("OverlayService", "안겹침")
    }

    private fun showBackground() {
        windowManager?.addView(backgroundView, backgroundParams)
    }

    private fun hideBackground() {
        windowManager?.removeView(backgroundView)

        if (
            params.x in 501..699 &&
            params.y in 2401..2449
            )
            onDestroy()
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
        super.onDestroy()
        try {
            if (overlayView?.isAttachedToWindow == true) {
                windowManager?.removeView(overlayView)
                Log.d("OverlayService", "오버레이 서비스 종료됨") // 로그 추가
            }
        } catch (e: Exception) {
            Log.d("OverlayService", "오버레이 서비스 이미 종료되어 있음")
        }
    }
}




