package com.example.modapjt

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OverlayIcon(
    onDoubleTab: () -> Unit,
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
            else -> Color(0xFF6200EE)  // 기본 보라색
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
            .size(48.dp)
            .clickable(onClick = onDoubleTab),
        tint = iconColor
    )
}