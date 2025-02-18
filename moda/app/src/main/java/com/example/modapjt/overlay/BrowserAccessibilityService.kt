package com.example.modapjt.overlay

import android.accessibilityservice.AccessibilityGestureEvent
import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BrowserAccessibilityService : AccessibilityService() {
    companion object {
        private val ALLOWED_PACKAGES = listOf(
            "com.android.chrome",
            "com.example.modapjt"
        )

        var currentUrl: String? = null

        private val _canOverlayState = MutableStateFlow(false)
        val canOverlayState = _canOverlayState.asStateFlow()

        private val _canGestureState = MutableStateFlow(false)
        val canGestureState = _canGestureState.asStateFlow()

        // 더블탭 감지용 변수
        private var lastClickTime: Long = 0
        private val DOUBLE_TAP_TIMEOUT = 300 // 더블탭으로 인정할 최대 시간 간격 (밀리초)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END -> {
                val gestureEvent = event.parcelableData as? AccessibilityGestureEvent
                if (gestureEvent != null) {
                    val gestureId = gestureEvent.gestureId
                    Log.d("BrowserAccessibilityService", "제스처 ID: $gestureId")

                    // 제스처 종류 확인
                    when (gestureId) {
                        AccessibilityService.GESTURE_SWIPE_UP -> Log.d("BrowserAccessibilityService", "위로 스와이프")
                        AccessibilityService.GESTURE_SWIPE_DOWN -> Log.d("BrowserAccessibilityService", "아래로 스와이프")
                        AccessibilityService.GESTURE_SWIPE_LEFT -> Log.d("BrowserAccessibilityService", "왼쪽으로 스와이프")
                        AccessibilityService.GESTURE_SWIPE_RIGHT -> Log.d("BrowserAccessibilityService", "오른쪽으로 스와이프")
                        AccessibilityService.GESTURE_DOUBLE_TAP -> Log.d("BrowserAccessibilityService", "더블 탭")
                        AccessibilityService.GESTURE_DOUBLE_TAP_AND_HOLD -> Log.d("BrowserAccessibilityService", "더블 탭 앤 홀드")
                    }
                }
            }
        }

        //오버레이 표시 가능 여부
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            _canOverlayState.value = ALLOWED_PACKAGES.contains(rootInActiveWindow?.packageName?.toString())
            Log.d(
                "BrowserAccessibilityService",
                "현재 앱: ${event.packageName?.toString()}, 오버레이 표시 여부: ${_canOverlayState.value}"
            )
        }

        //현재 url 링크 감지
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
            event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

            val nodeInfo = event.source ?: return
            val possibleIds = listOf(
                "com.android.chrome:id/url_bar"
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

        //더블 탭 감지
//        if (event.source?.actions?.and(AccessibilityNodeInfo.ACTION_CLICK) != 0 &&
//            event.packageName == "com.android.chrome") {
//            Log.d("BrowserAccessibilityService", "one click")
//            val currentTime = System.currentTimeMillis()
//            val timeDiff = currentTime - lastClickTime
//
//            if (timeDiff <= DOUBLE_TAP_TIMEOUT) {
//                lastClickTime = 0
//                Log.d("BrowserAccessibilityService", "Double tab detected!")
//
//                val intent = Intent()
//                intent.action = "com.example.modapjt.DOUBLE_TAP_ACTION"
//                sendBroadcast(intent)
//            } else {
//                lastClickTime = currentTime
//            }
//
//            event.source?.recycle()
//        }
        if (
            event.packageName == "com.android.chrome" &&
            event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED &&
            event.isScrollable.not()) {
            val currentTime = System.currentTimeMillis()
            val timeDiff = currentTime - lastClickTime

            if (timeDiff in 1..DOUBLE_TAP_TIMEOUT) {
                Log.d("BrowserAccessibilityService", "Double tab detected!")

                lastClickTime = 0

                val intent = Intent("com.example.modapjt.DOUBLE_TAP_ACTION").apply {
                    setPackage(applicationContext.packageName)
                    addCategory(Intent.CATEGORY_DEFAULT)
                }
                applicationContext.sendBroadcast(intent)
            }
            else {
                Log.d("BrowserAccessibilityService", "Single tab detected")
                lastClickTime = currentTime
            }
        }
    }

    override fun onInterrupt() {}
}
