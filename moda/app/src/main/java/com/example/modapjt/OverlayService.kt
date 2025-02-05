package com.example.modapjt

import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.LifecycleService
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import androidx.compose.runtime.*
import kotlinx.coroutines.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

class OverlayService : LifecycleService(), SavedStateRegistryOwner {
    private var windowManager: WindowManager? = null
    private var overlayView: ComposeView? = null

    private val repository by lazy { (application as ModapApplication).repository }
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
        overlayView = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setViewTreeLifecycleOwner(this@OverlayService)
            setViewTreeSavedStateRegistryOwner(this@OverlayService)

            setContent {
                var isSuccess by remember { mutableStateOf(false) }
                var isError by remember { mutableStateOf(false) }

                OverlayIcon(
                    onDoubleTab = { captureUrl() },
                    isSuccess = isSuccess,
                    isError = isError,
                    onAnimationComplete = {
                        isSuccess = false
                        isError = false
                    }
                )
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 100
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

            saveToLocalDatabase(url)
        }
    }

    private fun showSuccessFeedback() {
        overlayView?.setContent {
            var isSuccess by remember { mutableStateOf(true) }
            OverlayIcon(
                onDoubleTab = { captureUrl() },
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
                isSuccess = false,
                isError = isError,
                onAnimationComplete = { isError = false }
            )
        }
    }

    private fun saveToLocalDatabase(url: String) {
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

                repository.insert(finalUrl) // 데이터베이스에 저장
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
        windowManager?.removeView(overlayView)
        Log.d("OverlayService", "오버레이 서비스 종료됨") // 로그 추가
    }
}





//package com.example.modapjt
//
//import android.app.Application
//import android.content.Context
//import android.graphics.PixelFormat
//import android.util.Log
//import android.view.Gravity
//import android.view.WindowManager
//import androidx.compose.ui.platform.ComposeView
//import androidx.lifecycle.LifecycleService
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelStore
//import androidx.lifecycle.ViewModelStoreOwner
//import androidx.lifecycle.setViewTreeLifecycleOwner
//import androidx.savedstate.setViewTreeSavedStateRegistryOwner
//import androidx.compose.runtime.*
//import androidx.compose.ui.platform.ViewCompositionStrategy
//import kotlinx.coroutines.*
//import androidx.savedstate.SavedStateRegistry
//import androidx.savedstate.SavedStateRegistryController
//import androidx.savedstate.SavedStateRegistryOwner
//import com.example.modapjt.utils.MediaSessionHelper
//import com.example.modapjt.viewmodel.SavedUrlsViewModel
//import com.example.modapjt.viewmodel.SourceType
//
//class OverlayService : LifecycleService(), SavedStateRegistryOwner, ViewModelStoreOwner {
//    private var windowManager: WindowManager? = null
//    private var overlayView: ComposeView? = null
//
//    private val savedStateRegistryController = SavedStateRegistryController.create(this)
//    override val savedStateRegistry: SavedStateRegistry
//        get() = savedStateRegistryController.savedStateRegistry
//
//    private val viewModelStoreInstance = ViewModelStore() // 추가
//    override val viewModelStore: ViewModelStore
//        get() = viewModelStoreInstance
//
//    private lateinit var viewModel: SavedUrlsViewModel
//
//    override fun onCreate() {
//        super.onCreate()
//        savedStateRegistryController.performRestore(null)
//        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//
//        // ViewModel 초기화 코드 수정
//        viewModel = ViewModelProvider(
//            this, // `this`는 `OverlayService`, `ViewModelStoreOwner` 구현
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application as Application)
//        )[SavedUrlsViewModel::class.java]
//
//        setupOverlayView()
//        Log.d("OverlayService", "오버레이 서비스 시작됨")
//    }
//
//    private fun setupOverlayView() {
//        overlayView = ComposeView(this).apply {
//            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
//            setViewTreeLifecycleOwner(this@OverlayService)
//            setViewTreeSavedStateRegistryOwner(this@OverlayService)
//
//            setContent {
//                var isSuccess by remember { mutableStateOf(false) }
//                var isError by remember { mutableStateOf(false) }
//
//                OverlayIcon(
//                    onDoubleTab = { captureUrl() },
//                    isSuccess = isSuccess,
//                    isError = isError,
//                    onAnimationComplete = {
//                        isSuccess = false
//                        isError = false
//                    }
//                )
//            }
//        }
//
//        val params = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//            else WindowManager.LayoutParams.TYPE_PHONE,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            PixelFormat.TRANSLUCENT
//        ).apply {
//            gravity = Gravity.TOP or Gravity.START
//            x = 100
//            y = 200
//        }
//
//        windowManager?.addView(overlayView, params)
//    }
//
//    private fun captureUrl() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val currentApp = getCurrentForegroundApp()
//            Log.d("OverlayService", "현재 실행 중인 앱: $currentApp")
//
//            if (currentApp == "com.google.android.youtube") {
//                val videoTitle = NotificationListener().getCurrentYouTubeVideoTitle()
//
//                Log.d("OverlayService", "유튜브 영상 제목: $videoTitle")
//
//                if (!videoTitle.isNullOrEmpty()) {
//                    val youtubeUrl = "https://www.youtube.com/results?search_query=${videoTitle.replace(" ", "+")}"
//                    Log.d("OverlayService", "유튜브 영상 저장: $youtubeUrl")
//                    saveToLocalDatabase(youtubeUrl, SourceType.OVERLAY)
//                } else {
//                    Log.e("OverlayService", "유튜브 영상 감지 실패")
//                    withContext(Dispatchers.Main) { showErrorFeedback() }
//                }
//            } else {
//                var url: String? = null
//                for (i in 1..5) {
//                    url = BrowserAccessibilityService.currentUrl
//                    if (!url.isNullOrEmpty() && url != "Unknown URL") break
//                    delay(500)
//                }
//                url = url ?: "Unknown URL"
//                saveToLocalDatabase(url, SourceType.OVERLAY)
//            }
//        }
//    }
//
//    private fun saveToLocalDatabase(url: String, source: SourceType) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                viewModel.addOverlayCapture(url) // ViewModel을 통해 저장
//                Log.d("OverlayService", "URL 저장 완료 ($source): $url")
//
//                withContext(Dispatchers.Main) {
//                    showSuccessFeedback()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.e("OverlayService", "URL 저장 중 오류 발생: ${e.message}")
//
//                withContext(Dispatchers.Main) {
//                    showErrorFeedback()
//                }
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        windowManager?.removeView(overlayView)
//        Log.d("OverlayService", "오버레이 서비스 종료됨")
//    }
//}

// Unresolved reference: getCurrentForegroundApp
// Unresolved reference: showErrorFeedback
// Unresolved reference: showSuccessFeedback



//package com.example.modapjt
//
//import android.app.Service
//import android.content.Intent
//import android.os.IBinder
//import android.util.Log
//import com.example.modapjt.data.CaptureRepository
//import com.example.modapjt.data.AppDatabase
//import kotlinx.coroutines.*
//
//class OverlayService : Service() {
//    private lateinit var repository: CaptureRepository
//
//    override fun onCreate() {
//        super.onCreate()
//        val db = AppDatabase.getDatabase(this)
//        repository = CaptureRepository(db.captureDao())
//
//        Log.d("OverlayService", "오버레이 서비스 시작됨")
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        captureUrl()
//        return START_STICKY
//    }
//
//    private fun captureUrl() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val url = BrowserAccessibilityService.currentUrl ?: "Unknown URL"
//            repository.insert(url)
//            Log.d("OverlayService", "오버레이로 저장된 URL: $url")
//        }
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//}

