package com.example.modapjt

//import android.app.AppOpsManager
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.provider.Settings
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import com.example.modapjt.overlay.OverlayService
//
//class Deprecated : ComponentActivity() {
//
//    private val OVERLAY_PERMISSION_REQUEST_CODE = 1234
//    private val USAGE_STATS_PERMISSION_REQUEST_CODE = 1235  // 사용 기록 액세스 권한 요청 코드 추가
//
//    //  자동으로 알림 액세스 권한을 요청하는 코드 추가
//    private fun checkNotificationAccess() {
//        val enabledListeners =
//            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
//        val packageName = packageName
//        if (!enabledListeners.contains(packageName)) {
//            requestNotificationAccess()
//        }
//    }
//
//    private fun requestNotificationAccess() {
//        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
//        Toast.makeText(
//            this,
//            "유튜브 영상을 감지하려면 알림 액세스 권한이 필요합니다. 설정에서 활성화해주세요.",
//            Toast.LENGTH_LONG
//        ).show()
//        startActivity(intent)
//    }
//
//    // 필수 권한들을 확인하는 함수 (오버레이 & 사용 기록 액세스)
//    private fun checkPermissions() {
//        if (!Settings.canDrawOverlays(this)) {
////            requestOverlayPermission()
//        }
//
//        if (!hasUsageStatsPermission()) {
////            requestUsageStatsPermission()
//        }
//    }
//
//    // 사용 기록 액세스(Usage Stats) 권한이 있는지 확인하는 함수
//    private fun hasUsageStatsPermission(): Boolean {
//        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
//        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            appOps.unsafeCheckOpNoThrow(
//                AppOpsManager.OPSTR_GET_USAGE_STATS,
//                android.os.Process.myUid(),
//                packageName
//            )
//        } else {
//            appOps.checkOpNoThrow(
//                AppOpsManager.OPSTR_GET_USAGE_STATS,
//                android.os.Process.myUid(),
//                packageName
//            )
//        }
//        return mode == AppOpsManager.MODE_ALLOWED
//    }
//
//    // 권한 요청 후 결과 처리
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            OVERLAY_PERMISSION_REQUEST_CODE -> {
//                if (Settings.canDrawOverlays(this)) {
//                    startOverlayService()
//                } else {
//                    Toast.makeText(this, "오버레이 권한이 필요합니다", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            USAGE_STATS_PERMISSION_REQUEST_CODE -> { // 사용 기록 액세스 권한 요청 후 처리
//                if (hasUsageStatsPermission()) {
//                    Toast.makeText(this, "앱 사용 기록 액세스 권한이 허용되었습니다", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "앱 사용 기록 액세스 권한이 필요합니다", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    // 오버레이 서비스 실행 (변경 없음)
//    private fun startOverlayService() {
//        val intent = Intent(this, OverlayService::class.java)
//        startService(intent)
//    }
//
//    // 사용 기록 액세스(Usage Stats) 권한이 없으면 설정 화면으로 이동하여 요청하는 함수
//    private fun requestUsageStatsPermission() {
//        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS) // 사용 기록 액세스 설정 화면으로 이동
//        Toast.makeText(
//            this,
//            "앱 사용 기록 액세스 권한이 필요합니다. 설정에서 권한을 허용해주세요.",
//            Toast.LENGTH_LONG
//        ).show()
//        startActivityForResult(intent, USAGE_STATS_PERMISSION_REQUEST_CODE)
//    }
//}