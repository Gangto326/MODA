
package com.example.modapjt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.data.storage.TokenManager
import com.example.modapjt.domain.viewmodel.AuthViewModel
import com.example.modapjt.domain.viewmodel.AuthViewModelFactory
import com.example.modapjt.navigation.NavGraph
import com.example.modapjt.ui.theme.ModapjtTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


//@OptIn(ExperimentalAnimationApi::class)
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // 권한 요청
//        checkAccessibilityPermission()
//        checkOverlayPermission()
//
//        setContent {
//
//
//            ModapjtTheme {
//                val navController = rememberAnimatedNavController()
//                NavGraph(
//                    navController = navController,
//                    onStartOverlay = { checkOverlayPermission() }
//                )
//            }
//
//        }
//    }
//
//    // 오버레이 권한 요청 함수
//    private val OVERLAY_PERMISSION_REQUEST_CODE = 1234
//    private fun checkOverlayPermission() {
//        if (!Settings.canDrawOverlays(this)) {
//            val intent = Intent(
//                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                Uri.parse("package:$packageName")
//            )
//            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
//            Toast.makeText(
//                this,
//                "오버레이를 위해 권한이 필요합니다.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//    // 접근성 서비스 권한이 있는지 확인하는 함수
//    private fun isAccessibilityServiceEnabled(): Boolean {
//        val enabledServices = Settings.Secure.getString(
//            contentResolver,
//            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
//        )
//        val serviceComponent = ComponentName(packageName, BrowserAccessibilityService::class.java.name)
//        return enabledServices?.contains(serviceComponent.flattenToString()) == true
//    }
//
//    // 접근성 서비스 권한을 요청하는 함수
//    private fun checkAccessibilityPermission() {
//        if (!isAccessibilityServiceEnabled()) {
//            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            })
//            Toast.makeText(
//                this,
//                "URL 캡처를 위해 접근성 권한이 필요합니다. '${getString(R.string.app_name)}'을 활성화해주세요.",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        stopService(Intent(this, OverlayService::class.java))
//    }
//}

// -> 기존 코드

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TokenManager 초기화
        val tokenManager = TokenManager(this)

        setContent {
            ModapjtTheme {
                // 상태를 관찰하기 위한 ViewModel 생성
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(tokenManager)
                )

                // NavController 생성
                val navController = rememberAnimatedNavController()

                // 오버레이 상태 관리
                var isOverlayActive by remember { mutableStateOf(false) }
                val onStartOverlay: () -> Unit = { isOverlayActive = true }

                // 로그인 상태에 따라 시작 화면 결정
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // NavGraph에 authViewModel 전달
                    NavGraph(
                        navController = navController,
                        onStartOverlay = onStartOverlay,
                        tokenManager = tokenManager,
                        authViewModel = authViewModel
                    )

                    // 오버레이가 활성화되었을 때 표시
//                    if (isOverlayActive) {
//                        OverlayScreen(onDismiss = { isOverlayActive = false })
//                    }
                }
            }
        }
    }
}

