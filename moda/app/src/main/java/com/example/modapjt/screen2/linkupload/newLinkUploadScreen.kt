import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.components.bar.SearchBar
import com.example.modapjt.components.home.TopThumbnail
import com.example.modapjt.components.home.BottomThumbnail
import com.example.modapjt.components.bar.BottomBar
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.HeaderBar
import com.example.modapjt.components.bar.LinkAddHeaderBar
import com.example.modapjt.components.button.LinkAddButton
import com.example.modapjt.components.home.CategoryItem

@Composable
fun newLinkUploadScreen(navController: NavController, currentRoute: String) {
    // linkText는 사용자가 입력한 텍스트를 저장하는 상태 변수
    var linkText by remember { mutableStateOf("") }

    // Scaffold: 화면의 기본 레이아웃을 설정하는 컴포넌트
    Scaffold(
        // topBar: 화면 상단에 표시할 컴포넌트 (헤더 바)
        topBar = {
            HeaderBar(modifier = Modifier) // HeaderBar 컴포넌트가 상단 바를 표시
        },

        // bottomBar: 화면 하단에 표시할 컴포넌트 (하단 바)
        bottomBar = { BottomBarComponent(navController, currentRoute) } // BottomBarComponent가 하단 바를 표시
    ) { paddingValues -> // Scaffold가 자동으로 추가한 패딩 값을 받음
        // 본문(content) 영역을 설정하는 Column 컴포넌트
        Column(
            modifier = Modifier
                .fillMaxSize()  // 화면 전체 크기 사용
                .background(Color(0xFFFAFAFA))  // 배경색을 연한 회색으로 설정
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
                    /* 클릭 시 실행될 동작을 여기에 정의 */
                    // 예를 들어, linkText 값을 서버에 전송하거나 링크 목록에 추가하는 코드가 들어갈 수 있음
                }
            )
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun PreviewLinkAdd() {
//    LinkAdd()
//}
