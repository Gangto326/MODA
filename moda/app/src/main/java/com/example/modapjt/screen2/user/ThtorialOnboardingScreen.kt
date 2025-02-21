package com.example.modapjt.screen2.user

// TutorialOnboardingScreen.kt
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TutorialOnboardingScreen(
    navController: NavController,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { tutorialPages.size })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "MODA 200% 활용하기",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            TutorialPage(
                tutorialContent = tutorialPages[page]
            )
        }

        // Bottom Navigation Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageIndicator(
                pageCount = pagerState.pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = { onFinish() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "확인",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun TutorialPage(
    tutorialContent: TutorialContent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFBF6F0))
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = tutorialContent.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = tutorialContent.title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = tutorialContent.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { page ->
            Surface(
                modifier = Modifier.size(8.dp),
                shape = MaterialTheme.shapes.small,
                color = if (page == currentPage) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                }
            ) {}
        }
    }
}

data class TutorialContent(
    val imageRes: Int,
    val title: String,
    val description: String
)

val tutorialPages = listOf(
    TutorialContent(
        imageRes = R.drawable.onboarding_3,
        title = "링크 저장",
        description = "웹페이지를 둘러보다가 저장하고 싶은 정보가 있나요?\n제스처나 오버레이로 간편하게 저장하세요!"
    ),
    TutorialContent(
        imageRes = R.drawable.onboarding_4,
        title = "자동 분류",
        description = "저장한 링크는 AI가 자동으로 분류해줍니다.\n카테고리별로 쉽게 찾아보세요."
    ),
    TutorialContent(
        imageRes = R.drawable.onboarding_5,
        title = "내용 요약",
        description = "긴 글도 걱정하지 마세요.\nAI가 핵심 내용을 요약해서 보여드립니다."
    ),
    TutorialContent(
        imageRes = R.drawable.onboarding_6,
        title = "링크 검색",
        description = "찾고 싶은 정보가 있나요?\n키워드로 쉽고 빠르게 검색하세요."
    ),
    TutorialContent(
        imageRes = R.drawable.onboarding_7,
        title = "나만의 포털",
        description = "모든 정보를 한눈에 보고 관리하세요.\n나만의 맞춤형 포털을 만들어보세요."
    )
)