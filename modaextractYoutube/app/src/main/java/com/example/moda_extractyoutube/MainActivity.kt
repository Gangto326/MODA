package com.example.moda_extractyoutube

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moda_extractyoutube.ui.theme.ModaextractYoutubeTheme

class MainActivity : ComponentActivity() {
    //YoutubeNotificationViewModel 타입의 ViewModel을 자동으로 생성하고, 이 화면에서 사용할 수 있도록 준비한다
    private val youtubeNotificationViewModel: YoutubeNotificationViewModel by viewModels()

    private var youtubeNotificationListenerService: YoutubeNotificationListenerService? = null

    /*
    서비스와 액티비티 간의 연결을 관리하는 ServiceConnection 인터페이스의 구현체
     */
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as YoutubeNotificationListenerService.LocalBinder
            youtubeNotificationListenerService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            youtubeNotificationListenerService = null
        }
    }

    //알림 리스너 권한을 가지고 있는지 확인하는 메서드
    private fun isNotificationPermissionGranted(): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.isNotificationListenerAccessGranted(ComponentName(application, YoutubeNotificationListenerService::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //알림 권한 체크
        if(!isNotificationPermissionGranted()) {
            //권한이 없다면 ACTION_NOTIFICATION_LISTENER_SETTINGS 권한 획득 화면 띄우기
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        // 서비스 바인딩
        Intent(this, YoutubeNotificationListenerService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        setContent {
            ModaextractYoutubeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        val notification by youtubeNotificationViewModel.notification.collectAsState()

                        // 재생 중인 유튜브 영상 정보 확인 버튼 추가
                        Button(
                            onClick = {
                                youtubeNotificationListenerService?.getYoutubeNotification()?.let {
                                    youtubeNotificationViewModel.updateNotification(it)
                                }
                            }
                        ) {
                            Text("현재 재생 중인 유튜브 정보 확인하기")
                        }

                        // 재생 중인 유튜브 영상 정보 표시
                        notification?.let { notificationData ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = notificationData.title.toString())
                                    Text(text = notificationData.youtuber.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}