package com.example.modapjt.toktok.gesture

import android.content.Context
import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureStore
import android.gesture.Prediction
import android.util.Log
import com.example.modapjt.R
import java.io.File
import java.util.function.Consumer

class GestureManager(private val context: Context) {
    private var gestureLibrary: GestureLibrary

    companion object {
        private val RAW_GESTURE_FILE = R.raw.gestures
    }

    init {
        gestureLibrary = loadGestureLibrary()
    }

    private fun loadGestureLibrary(): GestureLibrary {
        val store = GestureStore()
        store.sequenceType = GestureStore.SEQUENCE_SENSITIVE
        store.orientationStyle = GestureStore.ORIENTATION_SENSITIVE

        context.resources.openRawResource(RAW_GESTURE_FILE).use { input ->
            val tempFile = File(context.cacheDir, "temp_gestures")
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }

            return GestureLibraries.fromFile(tempFile).apply {
                sequenceType = GestureStore.SEQUENCE_SENSITIVE
                orientationStyle = GestureStore.ORIENTATION_SENSITIVE
                load()

                // Raw 리소스에서 읽은 제스처 목록 로깅
                Log.d("GestureManager", "Loaded gestures from raw resource:")
                gestureEntries?.forEach { entry ->
                    Log.d("GestureManager", "Gesture entry: $entry")
                }
            }
        }
    }

    fun recognizeGesture(gesture: Gesture): Pair<String, Double>? {
        val predictions = gestureLibrary.recognize(gesture)

        Log.d("GestureManager", "제스처 인식")
        predictions.forEach(Consumer { prediction: Prediction ->
            Log.d("GestureManager", "${prediction.name} ${prediction.score}")
        })

        return predictions?.maxByOrNull { it.score }?.let {
            Pair(it.name, it.score)
        }
    }
}