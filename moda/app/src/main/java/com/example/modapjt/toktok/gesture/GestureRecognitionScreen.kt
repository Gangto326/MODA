package com.example.modapjt.toktok.gesture

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun GestureRecognitionScreen() {
    val context = LocalContext.current
    var recognizedGesture by remember { mutableStateOf<Pair<String, Double>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GestureDrawingCanvas(
            onGestureRecognized = { result ->
                recognizedGesture = result

                Toast.makeText(context, recognizedGesture.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        )

//        if (recognizedGesture != null) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        "인식된 제스처: ${recognizedGesture?.first}",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                    Text(
//                        "인식 점수: ${String.format("%.2f", recognizedGesture?.second)}",
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//            }
//        }
    }
}