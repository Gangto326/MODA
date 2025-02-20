package com.example.modapjt

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.modapjt.utils.GestureTracker
import com.example.modapjt.utils.GestureType

class GestureTrackingService : AccessibilityService() {
    private val gestureTracker = GestureTracker()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // 브라우저 화면 제스처 감지
        if (isBrowserEvent(event)) {
            handleBrowserGesture(event)
        }
    }

    private fun isBrowserEvent(event: AccessibilityEvent): Boolean {
        // 현재 브라우저 패키지인지 확인
        val browserPackages = listOf(
            "com.android.chrome",
            "org.mozilla.firefox",
            "com.opera.browser"
            // 다른 브라우저 패키지 추가
        )
        return event.packageName in browserPackages
    }

    private fun handleBrowserGesture(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> {
                // 제스처 시작 추적
                gestureTracker.startTracking(event)
            }
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> {
                // 제스처 종료 및 분석
                val gestureType = gestureTracker.analyzeGesture()
                // 제스처 타입에 따른 액션 수행
                processGestureAction(gestureType)
            }
        }
    }

    private fun processGestureAction(gestureType: GestureType) {
        when (gestureType) {
            GestureType.SLIDE_LEFT -> {
                // 왼쪽 슬라이드 액션
                Log.d("GestureTracking", "Slide Left Detected")
            }
            GestureType.SLIDE_RIGHT -> {
                // 오른쪽 슬라이드 액션
                Log.d("GestureTracking", "Slide Right Detected")
            }
            else -> {

            }
            // 다른 제스처 타입 추가
        }
    }

    // 필수 오버라이드 메서드
    override fun onInterrupt() {
        // 서비스 중단 시 처리
    }
}