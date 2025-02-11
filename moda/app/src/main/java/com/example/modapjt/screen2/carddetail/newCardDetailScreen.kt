import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TopBarDetail
import com.example.modapjt.components.bar.NewsDetail

@Composable
fun newCardDetailScreen(navController: NavController, currentRoute: String, cardId: String) {
    val articleContent = """
        주요 내용
        이 글에서는 저자가 리들에 가는 습관을 가지게 되고, 그곳에서 전남편의 사촌 누나 마리를 만나게 됩니다.
        이 자리에서 마리가 시어머니를 데리러 왔음을 알게 되면서, 저자는 일년 넘도록 비즈니스 관계였던 시어머니와 마주치는 어색함을 경험하게 됩니다.
        
        중요 포인트
        - 사람들과의 관계 변화: 전남편과의 분쟁 후 가족들과의 관계 변화가 나타납니다.
        - 사회적 압박: 저자가 런던에서 잘 정착하고 있지만, 사회적 압박을 받는 모습을 보여줍니다.
        - 부모세대와의 관계: 시어머니의 행동 변화를 통해 가족 간의 어색한 관계가 전달됩니다.
        
        결론
        저자는 런던에서 잘 정착하고 있지만, 여전히 사회적 압박과 과거 관계의 변화 때문에 씁쓸함을 느낍니다.
        이러한 경험은 사회적 압박과 개인의 적응 과정을 보여줍니다.
    """.trimIndent()

    Scaffold(
        topBar = { TopBarDetail(navController) },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NewsDetail(
                title = "기사 제목 예시",
                date = "2025-02-11",
                selectedTag = "유재석",
                onTagSelected = { /* 태그 선택 로직 */ },
                onFontSizeClick = { /* 글자 크기 변경 로직 */ },
                onShareClick = { /* 공유 로직 */ },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = articleContent,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Start
            )
        }
    }
}
