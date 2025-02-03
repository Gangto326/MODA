//package com.example.modapjt
//
//import android.accessibilityservice.AccessibilityService
//import android.util.Log
//import android.view.accessibility.AccessibilityEvent
//import android.view.accessibility.AccessibilityNodeInfo
//
//class BrowserAccessibilityService : AccessibilityService() {
//    companion object {
//        var currentUrl: String? = null
//    }
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
//            event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
//
//            val nodeInfo = event.source ?: return
//
//            // 여러 브라우저의 URL 바 ID를 탐색
//            val possibleIds = listOf(
//                "com.android.chrome:id/url_bar",      // Chrome
//                "org.mozilla.firefox:id/url_bar",    // Firefox
//                "com.microsoft.emmx:id/url_bar",     // Edge
//                "com.sec.android.app.sbrowser:id/url_bar" // 삼성 인터넷
//            )
//
//            for (id in possibleIds) {
//                val url = nodeInfo.findAccessibilityNodeInfosByViewId(id)
//                    ?.firstOrNull()?.text?.toString()
//                if (!url.isNullOrEmpty()) {
//                    currentUrl = url
//                    Log.d("BrowserAccessibilityService", "URL 감지됨: $currentUrl") // 로그 추가
//                    break
//                }
//            }
//
//            if (currentUrl == null) {
//                Log.d("BrowserAccessibilityService", "URL을 찾을 수 없음") // 로그 추가
//            }
//
//            nodeInfo.recycle()
//        }
//    }
//
//    override fun onInterrupt() {}
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//        Log.d("BrowserAccessibilityService", "접근성 서비스 연결됨") // 로그 추가
//    }
//}





//package com.example.modapjt
//
//import android.accessibilityservice.AccessibilityService
//import android.util.Log
//import android.view.accessibility.AccessibilityEvent
//import android.view.accessibility.AccessibilityNodeInfo
//
//class BrowserAccessibilityService : AccessibilityService() {
//    companion object {
//        var currentUrl: String? = null
//    }
//
//    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        val packageName = event.packageName?.toString() ?: return
//
//        // 유튜브 앱이 실행 중이면 현재 URL을 초기화하여 이전 브라우저 URL이 저장되지 않도록 설정
//        if (packageName == "com.google.android.youtube") {
//            currentUrl = null
//            Log.d("BrowserAccessibilityService", "유튜브 앱 실행 중 - URL 초기화")
//            return
//        }
//
//        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
//            event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
//
//            val nodeInfo = event.source ?: return
//
//            // 여러 브라우저의 URL 바 ID를 탐색하여 현재 웹사이트 URL 가져오기
//            val possibleIds = listOf(
//                "com.android.chrome:id/url_bar",      // Chrome
//                "org.mozilla.firefox:id/url_bar",    // Firefox
//                "com.microsoft.emmx:id/url_bar",     // Edge
//                "com.sec.android.app.sbrowser:id/url_bar" // 삼성 인터넷
//            )
//
//            for (id in possibleIds) {
//                val url = nodeInfo.findAccessibilityNodeInfosByViewId(id)
//                    ?.firstOrNull()?.text?.toString()
//                if (!url.isNullOrEmpty()) {
//                    currentUrl = url
//                    Log.d("BrowserAccessibilityService", "URL 감지됨: $currentUrl") // 로그 추가
//                    break
//                }
//            }
//
//            if (currentUrl == null) {
//                Log.d("BrowserAccessibilityService", "URL을 찾을 수 없음") // 로그 추가
//            }
//
//            nodeInfo.recycle()
//        }
//    }
//
//    override fun onInterrupt() {}
//
//    override fun onServiceConnected() {
//        super.onServiceConnected()
//        Log.d("BrowserAccessibilityService", "접근성 서비스 연결됨") // 로그 추가
//    }
//}



package com.example.modapjt

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class BrowserAccessibilityService : AccessibilityService() {
    companion object {
        var currentUrl: String? = null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
            event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

            val nodeInfo = event.source ?: return
            val possibleIds = listOf(
                "com.android.chrome:id/url_bar",
                "org.mozilla.firefox:id/url_bar",
                "com.microsoft.emmx:id/url_bar",
                "com.sec.android.app.sbrowser:id/url_bar"
            )

            for (id in possibleIds) {
                val url = nodeInfo.findAccessibilityNodeInfosByViewId(id)
                    ?.firstOrNull()?.text?.toString()
                if (!url.isNullOrEmpty()) {
                    currentUrl = url
                    Log.d("BrowserAccessibilityService", "URL 감지됨: $currentUrl")
                    break
                }
            }

            nodeInfo.recycle()
        }
    }

    override fun onInterrupt() {}
}
