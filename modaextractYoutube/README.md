# 로직
- 안드로이드 휴대폰의 알림창에 위치하는 미디어 플레이어도 내부적으로 알림의 한 종류로 동작함
- 다른 앱의 알림을 모니터링하고 접근할 수 있게 해주는 서비스인 NotificationListenerService를 이용하여 유튜브의 제목과 유튜버의 이름을 추출

# 프로젝트 생성
- Templates: Empty Activity
- Minimum SDK: API 34
- Build configuration language: Kotlin DSL (build.gradle.kts)

# 생성 내역

- YoutubeNotificationData.kt
  - 유튜브 알림 정보를 담기 위한 data class

- YoutubeNotificationViewModel.kt
  - 유튜브 알림 정보를 저장하여 화면에 표시하기 위한 ViewModel

- YoutubeNotificationListenerService.kt
  - 사용자의 알림을 감지하기 위한 NotificationListenerService


# 수정 내역

- AndroidManifest.xml
  - 알림 정보를 얻기 위해 NotificationListenerService 서비스 등록

- MainActivity.kt
  - 버튼을 생성하여, 버튼 클릭 시 현재 재생중인 유튜브의 내용을 얻어와 화면에 출력하도록 작성