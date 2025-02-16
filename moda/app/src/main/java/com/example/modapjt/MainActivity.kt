package com.example.modapjt

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.data.storage.TokenManager
//import com.example.modapjt.data.storage.CookieManager
import com.example.modapjt.domain.viewmodel.AuthViewModel
import com.example.modapjt.domain.viewmodel.AuthViewModelFactory
import com.example.modapjt.navigation.NavGraph
import com.example.modapjt.overlay.BrowserAccessibilityService
import com.example.modapjt.overlay.OverlayService
import com.example.modapjt.ui.theme.ModapjtTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 권한 요청
        checkAccessibilityPermission()
        checkOverlayPermission()

        setContent {
            ModapjtTheme {
                // TokenManager 초기화 (Compose에서 remember로 관리)
                val context = LocalContext.current
                val tokenManager = remember { TokenManager(context) }

                // 직접 AuthViewModel 인스턴스를 생성 (팩토리 없이)
                // val authViewModel = remember { AuthViewModel(tokenManager) } // <-- 여기 직접 생성
                // <-> 수정된 ViewModel 초기화 (팩토리 사용)
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(tokenManager)
                )

                // NavController 생성
                val navController = rememberAnimatedNavController()

                // 로그인 상태에 따라 네비게이션 적용

                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    onStartOverlay = { checkOverlayPermission() }
                )

            }
        }
    }

    // 오버레이 권한 요청 함수
    private val OVERLAY_PERMISSION_REQUEST_CODE = 1234
    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
            Toast.makeText(
                this,
                "오버레이를 위해 권한이 필요합니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // 접근성 서비스 권한이 있는지 확인하는 함수
    private fun isAccessibilityServiceEnabled(): Boolean {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        val serviceComponent = ComponentName(packageName, BrowserAccessibilityService::class.java.name)
        return enabledServices?.contains(serviceComponent.flattenToString()) == true
    }

    // 접근성 서비스 권한을 요청하는 함수
    private fun checkAccessibilityPermission() {
        if (!isAccessibilityServiceEnabled()) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
            Toast.makeText(
                this,
                "URL 캡처를 위해 접근성 권한이 필요합니다. '${getString(R.string.app_name)}'을 활성화해주세요.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, OverlayService::class.java))
    }
}
