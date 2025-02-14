package com.example.modapjt

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.view.WindowCompat
import com.example.modapjt.navigation.NavGraph
import com.example.modapjt.ui.theme.ModapjtTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전체화면 활성화
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 오버레이 및 접근성 서비스를 위한 권한 요청
        checkOverlayPermission()
        checkAccessibilityPermission()

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
    private val USAGE_STATS_PERMISSION_REQUEST_CODE = 1235  // 사용 기록 액세스 권한 요청 코드 추가


    // 필수 권한들을 확인하는 함수 (오버레이 & 사용 기록 액세스)
    private fun checkPermissions() {
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission()
        }

        if (!hasUsageStatsPermission()) {
            requestUsageStatsPermission()
        }
    }

    // 사용 기록 액세스(Usage Stats) 권한이 있는지 확인하는 함수
    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    // 사용 기록 액세스(Usage Stats) 권한이 없으면 설정 화면으로 이동하여 요청하는 함수
    private fun requestUsageStatsPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS) // 사용 기록 액세스 설정 화면으로 이동
        Toast.makeText(
            this,
            "앱 사용 기록 액세스 권한이 필요합니다. 설정에서 권한을 허용해주세요.",
            Toast.LENGTH_LONG
        ).show()
        startActivityForResult(intent, USAGE_STATS_PERMISSION_REQUEST_CODE)
    }

    // 기존 오버레이 권한 요청 함수 (변경 없음)
    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            requestOverlayPermission()
        } else {
            startOverlayService()
        }
    }

    // 오버레이 서비스 실행 (변경 없음)
    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        startService(intent)
    }

    // 권한 요청 후 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OVERLAY_PERMISSION_REQUEST_CODE -> {
                if (Settings.canDrawOverlays(this)) {
                    startOverlayService()
                } else {
                    Toast.makeText(this, "오버레이 권한이 필요합니다", Toast.LENGTH_SHORT).show()
                }
            }

            USAGE_STATS_PERMISSION_REQUEST_CODE -> { // 사용 기록 액세스 권한 요청 후 처리
                if (hasUsageStatsPermission()) {
                    Toast.makeText(this, "앱 사용 기록 액세스 권한이 허용되었습니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "앱 사용 기록 액세스 권한이 필요합니다", Toast.LENGTH_SHORT).show()
                }
            }
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

    //  자동으로 알림 액세스 권한을 요청하는 코드 추가
    private fun checkNotificationAccess() {
        val enabledListeners =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val packageName = packageName
        if (!enabledListeners.contains(packageName)) {
            requestNotificationAccess()
        }
    }

    private fun requestNotificationAccess() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        Toast.makeText(
            this,
            "유튜브 영상을 감지하려면 알림 액세스 권한이 필요합니다. 설정에서 활성화해주세요.",
            Toast.LENGTH_LONG
        ).show()
        startActivity(intent)
    }
}
