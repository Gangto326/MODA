package com.example.modapjt.overlay

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OverlayIcon(
    onDoubleTab: () -> Unit,
    onDrag: (IntOffset) -> Unit = {},
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    isSuccess: Boolean = false,
    isError: Boolean = false,
    onAnimationComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val iconColor by animateColorAsState(
        targetValue = when {
            isSuccess -> Color.Green
            isError -> Color.Red
            else -> Color(0xFF090808)  // 기본 검정색
        },
        animationSpec = tween(durationMillis = 500)
    )

    LaunchedEffect(isSuccess, isError) {
        if (isSuccess || isError) {
            coroutineScope.launch {
                delay(1000)  // 1초 동안 색상 유지
                onAnimationComplete()
            }
        }
    }

    Icon(
        imageVector = Icons.Default.AddCircle,
        contentDescription = "Overlay Icon",
        modifier = modifier
            .fillMaxSize()
            .alpha(0.3f) //투명도 설정
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        onDoubleTab()
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragCancel = {
                        onDragEnd()
                    },
                    onDragStart = {
                        onDragStart()
                    },
                    onDragEnd = {
                        onDragEnd()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(IntOffset(dragAmount.x.toInt(), dragAmount.y.toInt()))
                    }
                )
            },
        tint = iconColor
    )
}