package com.example.modapjt.utils

import android.view.accessibility.AccessibilityEvent
import kotlin.math.abs

class GestureTracker {
    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    fun startTracking(event: AccessibilityEvent) {
        // 제스처 시작 지점 추적
        startX = event.scrollX.toFloat()
        startY = event.scrollY.toFloat()
    }

    fun analyzeGesture(): GestureType {
        val deltaX = endX - startX
        val deltaY = endY - startY

        return when {
            abs(deltaX) > abs(deltaY) -> {
                if (deltaX > 0) GestureType.SLIDE_RIGHT
                else GestureType.SLIDE_LEFT
            }
            else -> {
                if (deltaY > 0) GestureType.SWIPE_DOWN
                else GestureType.SWIPE_UP
            }
        }
    }
}