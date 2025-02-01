package com.example.moda_extractyoutube

import android.app.Notification
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class YoutubeNotificationListenerService : NotificationListenerService() {

    /*
    바인더 설정

    LocalBinder를 이용하여 다른 컴포넌트(예: 액티비티)가 이 서비스에 바인딩하여 직접 통신할 수 있게 됨. (like 다리 건설)
    `this@YoutubeNotificationListenerService`는 현재 서비스의 인스턴스를 반환하는 코드
     */
    private val binder = LocalBinder()
    inner class LocalBinder : Binder() {
        fun getService(): YoutubeNotificationListenerService = this@YoutubeNotificationListenerService
    }

    //시스템이나 다른 앱이 이 서비스에 바인딩하려고 할 때 호출되는 메서드
    override fun onBind(intent: Intent): IBinder? {
        return if (intent.action == NotificationListenerService.SERVICE_INTERFACE) {
            super.onBind(intent)
        } else {
            binder
        }
    }

    /*
    유튜브 알림에 대한 정보 제공

    활성화된 알림(activeNotifications) 중에서
        1. filter: 유튜브 알림만 필터링
        2. map: 알림의 제목(Notification.EXTRA_TITLE)과 내용(Notification.EXTRA_TEXT)을 추출
        3. map: YoutubeNotificationData 객체로 변환
        4. firstOrNull: 해당 알림을 반환(없으면 null)
    ps. activeNotifications는 NotificationListenerService()가 가지고 있는 필드
     */
    fun getYoutubeNotification(): YoutubeNotificationData? {
        return activeNotifications
            .filter { it.packageName == "com.google.android.youtube" }
            .map { sbn ->
                val extras = sbn.notification?.extras
                YoutubeNotificationData(
                    title = extras?.getCharSequence(Notification.EXTRA_TITLE)?.toString(),
                    youtuber = extras?.getCharSequence(Notification.EXTRA_TEXT).toString()
                )
            }
            .firstOrNull()
    }

    //모든 알림 log 확인하기
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val packageName: String = sbn?.packageName ?: "Null"
        val extras = sbn?.notification?.extras

        val extraTitle: String = extras?.getCharSequence(Notification.EXTRA_TITLE).toString()
        val extraText: String = extras?.getCharSequence(Notification.EXTRA_TEXT).toString()
        val extraBigText: String = extras?.getCharSequence(Notification.EXTRA_BIG_TEXT).toString()
        val extraInfoText: String = extras?.getCharSequence(Notification.EXTRA_INFO_TEXT).toString()
        val extraSubText: String = extras?.getCharSequence(Notification.EXTRA_SUB_TEXT).toString()
        val extraSummaryText: String = extras?.getCharSequence(Notification.EXTRA_SUMMARY_TEXT).toString()

        Log.d(
            "알림", "onNotificationPosted:\n" +
                    "PackageName: $packageName\n" +
                    "Title: $extraTitle\n" +
                    "Text: $extraText\n" +
                    "BigText: $extraBigText\n" +
                    "InfoText: $extraInfoText\n" +
                    "SubText: $extraSubText\n" +
                    "SummaryText: $extraSummaryText\n"
        )
    }
}