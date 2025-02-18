package com.example.modapjt.overlay

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi

class GestureService : Service() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.example.modapjt.DOUBLE_TAP_ACTION") {
                // 여기서 더블탭에 대한 처리를 합니다
                handleDoubleTap()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        Log.d("GestureService", "Service created")
        // BroadcastReceiver 등록
        val filter = IntentFilter().apply {
            addAction("com.example.modapjt.DOUBLE_TAP_ACTION")
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    private fun handleDoubleTap() {
        // 더블탭 처리 로직 구현
        Log.d("GestureService", "Received double tap")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // BroadcastReceiver 해제
        unregisterReceiver(receiver)
    }
}