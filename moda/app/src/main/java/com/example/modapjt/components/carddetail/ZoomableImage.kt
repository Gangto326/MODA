import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


@Composable
fun ZoomableImage(imageUrl: String, imageOriginalWidth: Float, imageOriginalHeight: Float) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val maxScale = 3f
    val minScale = 1f
    val imageHeight = 500f  // 이미지 높이 고정

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = if (scale == 1f) 2f else 1f // 더블탭 시 1배 → 2배 or 2배 → 1배
                        offsetX = 0f
                        offsetY = 0f
                    }
                )
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "확대 가능한 이미지",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(minScale, maxScale)

                        val maxOffsetX = ((imageOriginalWidth * newScale) - imageOriginalWidth) / 2
                        val maxOffsetY = ((imageOriginalHeight * newScale) - imageOriginalHeight) / 2

                        scale = newScale
                        offsetX = (offsetX + pan.x).coerceIn(-maxOffsetX, maxOffsetX)
                        offsetY = (offsetY + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                    }
                },
            contentScale = ContentScale.Fit
        )
    }
}
