package com.example.modapjt.overlay

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import com.example.modapjt.R

@Composable
fun OverlayIcon(
    onDoubleTab: () -> Unit,
    onDrag: (IntOffset) -> Unit = {},
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    Image(
        painter = painterResource(R.drawable.icon_round),
        contentDescription = "Overlay Icon",
        modifier = modifier
            .fillMaxSize()
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
    )
}