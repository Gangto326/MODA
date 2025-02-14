package com.example.modapjt.overlay

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object OverlayStateManager {
    private val _isOverlayActive = MutableStateFlow(false)
    val isOverlayActive = _isOverlayActive.asStateFlow()

    fun setOverlayActive(active: Boolean) {
        _isOverlayActive.value = active
    }

    init {
        // 코루틴 스코프를 생성하여 값 변경 감지
        CoroutineScope(Dispatchers.Main).launch {
            isOverlayActive.collect { value ->
                Log.d("OverlayStateManager", "오버레이 상태 변경: $value")
            }
        }
    }
}