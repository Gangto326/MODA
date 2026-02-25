
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.HeaderBar
import com.example.modapjt.components.bar.LinkAddHeaderBar
import com.example.modapjt.components.bar.TitleHeaderBar
import com.example.modapjt.components.button.LinkAddButton
import com.example.modapjt.data.repository.CardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun newLinkUploadScreen(navController: NavController, currentRoute: String, repository: CardRepository) {
    // linkText는 사용자가 입력한 텍스트를 저장하는 상태 변수
    var linkText by remember { mutableStateOf("") }

    // 저장 요청 중인지 여부를 나타내는 상태 변수 (로딩 상태)
    var isLoading by remember { mutableStateOf(false) }

    // 성공 또는 실패 메시지를 저장하는 상태 변수
    var message by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope() // CoroutineScope 획득
    var showWaitingMessage by remember { mutableStateOf(false) } // "카드 생성 중입니다" 표시 여부


    // Scaffold: 화면의 기본 레이아웃을 설정하는 컴포넌트
    Scaffold(
        // topBar: 화면 상단에 표시할 컴포넌트 (헤더 바)
        topBar = {
            TitleHeaderBar(titleName = "링크 추가")
        },

        // bottomBar: 화면 하단에 표시할 컴포넌트 (하단 바)
        bottomBar = { BottomBarComponent(navController, currentRoute) } // BottomBarComponent가 하단 바를 표시
    ) { paddingValues -> // Scaffold가 자동으로 추가한 패딩 값을 받음
        // 본문(content) 영역을 설정하는 Column 컴포넌트
        Column(
            modifier = Modifier
                .fillMaxSize()  // 화면 전체 크기 사용
//                .background(Color(0xFFFAFAFA))  // 배경색을 연한 회색으로 설정
                .padding(paddingValues) // Scaffold에서 전달한 paddingValues 적용
                .padding(16.dp),  // 본문 패딩 적용
            verticalArrangement = Arrangement.Center // Column 내부 요소들을 세로로 중앙 정렬
        ) {
            // LinkAddHeaderBar: 링크 입력 필드를 포함하는 컴포넌트
            LinkAddHeaderBar(
                value = linkText, // TextField에 현재 입력된 linkText 값을 전달
                onValueChange = { linkText = it }  // 텍스트가 변경될 때 linkText 업데이트
            )

            Spacer(modifier = Modifier.height(8.dp)) // 사이 간격 설정

            // LinkAddButton: "링크 추가하기" 버튼 컴포넌트
            LinkAddButton(
                onClick = {
                    // 입력값이 비어 있는 경우 처리
                    if (linkText.isNotBlank()) {
                        isLoading = true // 로딩 상태 활성화
                        message = null // 메시지 초기화

                        // 비동기 작업 실행 (Coroutine 사용)
                        coroutineScope.launch  {
                            val result = repository.createCard(linkText) // 카드 추가 요청
                            isLoading = false // 요청 완료 후 로딩 해제

                            if (result.isSuccess) {
                                isLoading = false
                                message = "✅ URL 저장 성공!"
                                linkText = ""
                            } else {
                                // 실패 응답을 받으면 1분간 "카드 생성 중입니다..." 표시
                                showWaitingMessage = true
                                delay(600000) // ⏳ 1분(60초) 대기
                                isLoading = false
                                showWaitingMessage = false
                                message = "❌ 저장 실패: ${result.exceptionOrNull()?.message}"
                            }
                        }
                    } else {
                        message = "⚠️ URL을 입력해주세요." // 입력값이 없을 때 경고 메시지
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp)) // 버튼과 메시지 사이 간격 추가
            // 실패 응답을 받은 경우 1분 동안 "카드 생성 중입니다..." 표시
            if (showWaitingMessage) {
                Text(
                    text = "⏳ 카드 생성 중입니다...",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 성공 또는 실패 메시지 표시
            message?.let {
                Text(
                    text = it,
                    color = if (it.contains("성공")) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onError, // 성공이면 초록, 실패면 빨간색
                    fontSize = 14.sp
                )
            }

            // 로딩 중일 때 표시할 Progress Indicator
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp)) // 로딩 애니메이션 표시
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewLinkAdd() {
//    LinkAdd()
//}
