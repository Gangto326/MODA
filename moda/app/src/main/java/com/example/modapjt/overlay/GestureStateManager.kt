package com.example.modapjt.overlay

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object GestureStateManager {
    private val _isGestureActive = MutableStateFlow(false)
    val isGestureActive = _isGestureActive.asStateFlow()

    fun setOverlayActive(active: Boolean) {
        _isGestureActive.value = active
    }

    init {
        // 코루틴 스코프를 생성하여 값 변경 감지
        CoroutineScope(Dispatchers.Main).launch {
            isGestureActive.collect { value ->
                Log.d("GestureStateManager", "제스처 상태 변경: $value")
            }
        }
    }
}