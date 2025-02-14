package com.example.modapjt.overlay

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BrowserAccessibilityService : AccessibilityService() {
    companion object {
        var currentUrl: String? = null
        private val _canOverlayState = MutableStateFlow(false)
        val canOverlayState = _canOverlayState.asStateFlow()

        private val ALLOWED_PACKAGES = listOf(
            "com.android.chrome",
            "org.mozilla.firefox",
            "com.microsoft.emmx",
            "com.sec.android.app.sbrowser",
            "com.example.modapjt"
        )
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                _canOverlayState.value = ALLOWED_PACKAGES.contains(rootInActiveWindow?.packageName?.toString())
                Log.d(
                    "BrowserAccessibilityService",
                    "현재 앱: ${event.packageName?.toString()}, 오버레이 여부: ${_canOverlayState.value}"
                )
            }
        }

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
