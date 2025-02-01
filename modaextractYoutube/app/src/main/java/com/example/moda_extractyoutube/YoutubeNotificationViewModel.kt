package com.example.moda_extractyoutube

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class YoutubeNotificationViewModel : ViewModel() {
    //알림의 상태 관리를 위한 private하며 수정이 가능한 StateFlow
    private val _notification = MutableStateFlow<YoutubeNotificationData?>(null)
    /*
    외부에서 접근 가능한 읽기 전용 StateFlow

    StateFlow는 Observable 패턴을 구현하고, asStateFlow로 만든 Flow는 원본을 계속 관찰한다.
    따라서, 원본의 값이 변경되어도 자동으로 감지하고 해당 값을 전달해준다.
     */
    val notification = _notification.asStateFlow()

    //알림을 업데이트하기 위한 함수
    fun updateNotification(newNotification: YoutubeNotificationData) {
        _notification.value = newNotification
    }
}