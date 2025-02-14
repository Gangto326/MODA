package com.example.modapjt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.example.modapjt.navigation.NavGraph
import com.example.modapjt.ui.theme.ModapjtTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 오버레이 및 접근성 서비스를 위한 권한 요청
        checkAccessibilityPermission()
        checkOverlayPermission()

        setContent {
            ModapjtTheme {
                val navController = rememberAnimatedNavController()
                NavGraph(
                    navController = navController,
                    onStartOverlay = { checkOverlayPermission() }
                )
            }

        }
    }

    private val OVERLAY_PERMISSION_REQUEST_CODE = 1234

    // 오버레이 권한 요청 함수
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

    // 접근성 서비스 권한이 있는지 확인하고 요청하는 함수 (변경 없음)
    private fun checkAccessibilityPermission() {
        val accessibilityEnabled = Settings.Secure.getInt(
            contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED, 0
        )

        if (accessibilityEnabled == 0) {
            // 접근성 설정 화면으로 이동
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
}
