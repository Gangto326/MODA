package com.example.modapjt.overlay

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

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
