package com.example.modapjt

import android.app.ComponentCaller
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modapjt.data.storage.TokenManager
import com.example.modapjt.domain.viewmodel.AuthViewModel
import com.example.modapjt.domain.viewmodel.AuthViewModelFactory
import com.example.modapjt.navigation.NavGraph
import com.example.modapjt.overlay.BrowserAccessibilityService
import com.example.modapjt.overlay.OverlayService
import com.example.modapjt.ui.theme.ModapjtTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null  // 이 줄을 추가해야 합니다


    @RequiresApi(Build.VERSION_CODES.O)
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

                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(tokenManager)
                )

                val navController = rememberAnimatedNavController()
                this@MainActivity.navController = navController  // 이 줄을 추가해야 합니다


                LaunchedEffect(Unit) {
                    intent?.getStringExtra("cardId")?.let { cardId ->
                        navController.navigate("cardDetail/$cardId")
                    }
                }

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

    override fun onNewIntent(intent: Intent) {  // nullable 제거
        super.onNewIntent(intent)
        intent.getStringExtra("cardId")?.let { cardId ->
            navController?.navigate("cardDetail/$cardId")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, OverlayService::class.java))
    }
}
