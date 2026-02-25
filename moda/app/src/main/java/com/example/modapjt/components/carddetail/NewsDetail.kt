
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.modapjt.R
import com.example.modapjt.components.carddetail.ImageSlider
import com.example.modapjt.domain.model.CardDetail
import com.example.modapjt.domain.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 뉴스 기사의 상세 내용을 보여주는 화면 컴포저블
 * 제목, 썸네일, 본문 등을 세로로 배치
 *
 * @param cardDetail 표시할 뉴스 기사의 상세 정보를 담은 객체
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@ExperimentalMaterial3Api
@Composable
fun NewsDetailScreen(cardDetail: CardDetail, navController: NavController) {
    val searchViewModel: SearchViewModel = viewModel()
    val uriHandler = LocalUriHandler.current

    // 날짜 포맷 변경 (년-월-일)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formattedDate = LocalDateTime.parse(cardDetail.createdAt).format(formatter)

    var showImage by remember { mutableStateOf(true) } // 이미지 표시 여부 상태
    var showSectionModal by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 상단 고정 아이템 개수 (카테고리, 제목, 키워드, 구분선)
    val headerItemCount = 4

    // 키워드 List
    val limitedKeywords = cardDetail.keywords

    // 마크다운 콘텐츠를 섹션으로 분리
    val sections = remember(cardDetail.content) {
        splitMarkdownIntoSections(cardDetail.content)
    }

    // 현재 활성화된 섹션 인덱스 추적
    val activeIndex = remember(listState.firstVisibleItemIndex, listState.layoutInfo) {
        calculateActiveIndex(listState, headerItemCount)
    }

    var isExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    // 화면 크기에 따른 동적 패딩 계산
    val horizontalPadding = (screenWidth * 0.04f).dp  // 화면 너비의 4%
    val verticalPadding = (screenWidth * 0.03f).dp    // 화면 너비의 3%

    // 화면 크기에 따른 글자 크기 스케일 계산
    val fontScale = when {
        screenWidth > 600 -> 0.65f  // 태블릿
        screenWidth > 400 -> 0.65f  // 일반 폰
        else -> 0.45f              // 작은 폰
    }

    fun scrollToSection(index: Int) {
        coroutineScope.launch {
            val targetIndex = index + headerItemCount
            val viewportHeight = listState.layoutInfo.viewportEndOffset
            val itemOffset = (viewportHeight * 0.15).toInt()

            listState.animateScrollToItem(
                index = targetIndex,
                scrollOffset = -itemOffset
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

//            // 🔘 이미지 토글 버튼
//            Button(
//                onClick = { showImage = !showImage },
//                modifier = Modifier.padding(8.dp)
//            ) {
//                org.commonmark.node.Text(if (showImage) "이미지 숨기기" else "이미지 보기")
//            }


            // 🖼 이미지 슬라이더 (토글에 따라 표시)
            if (showImage && cardDetail.subContents.isNotEmpty()) {
                ImageSlider(imageUrls = cardDetail.subContents)
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // 📜 본문 내용
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    item {
                        // 카테고리와 날짜
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontalPadding)
                                .padding(top = 16.dp)
                                .padding(horizontal = 1.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when (cardDetail.categoryId) {
                                    1 -> "전체"
                                    2 -> "건강"
                                    3 -> "여행"
                                    4 -> "음식"
                                    5 -> "IT"
                                    6 -> "경제"
                                    7 -> "문화"
                                    8 -> "과학"
                                    9 -> "취미"
                                    10 -> "예술"
                                    else -> "기타"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                text = LocalDateTime.parse(cardDetail.createdAt)
                                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                    // 제목
                    item {
                        Text(
                            text = cardDetail.title,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = MaterialTheme.typography.headlineLarge.fontSize * fontScale,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 30.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontalPadding)
                                .padding(vertical = 8.dp)
                                .wrapContentWidth(Alignment.Start),  // 컨텐츠는 중앙, 텍스트는 왼쪽 정렬
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }


                    // 🔥 키워드 간격 적용
                    item {
                        // 키워드와 공유버튼
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontalPadding),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FlowRow(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.Start,
                                maxItemsInEachRow = 3
                            ) {
                                cardDetail.keywords.take(3).forEach { keyword ->
                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f)),
                                        color = Color.Transparent,
                                        modifier = Modifier
                                            .padding(end = 8.dp, bottom = 16.dp)
                                            .clickable (
                                                indication = null, // 클릭 효과 제거
                                                interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                                            ){
                                                if (keyword.isNotBlank()) {
                                                    navController.navigate("newSearchCardListScreen/$keyword")
                                                }
                                            }
                                    ) {
                                        Text(
                                            text = keyword,
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 6.dp
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }

                                }
                            }
                            // 🔘 이미지 토글 버튼
                            // 🖼️ 이미지 토글 아이콘
                            IconButton(
                                onClick = { showImage = !showImage },
//                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (showImage) {
                                            if (isSystemInDarkTheme()) R.drawable.ic_d_not_image else R.drawable.ic_not_image
                                        } else {
                                            R.drawable.ic_a_image
                                        }
                                    ),
                                    contentDescription = if (showImage) "이미지 숨기기" else "이미지 보기",
                                    modifier = Modifier.padding(bottom = 17.dp).size(
                                        if (showImage) 100.dp else 24.dp  // ic_not_image일 때 더 크게 표시
                                    ),
                                    tint = Color.Unspecified

                                )
                            }
                            IconButton(onClick = { uriHandler.openUri(cardDetail.originalUrl) }) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    item {
                        Divider(
                            color = MaterialTheme.colorScheme.onTertiary,
                            thickness = 6.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    items(sections.withIndex().toList()) { (index, section) ->
                        Column(modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(vertical = 8.dp)
                        ) {
                            // 섹션 제목 추출
                            val sectionTitle = getSectionTitle(section)

                            ShakingTitle(
                                text = sectionTitle,
                                isActive = index == activeIndex,
                                isFirst = index == 0,
                                isExpanded = isExpanded,
                                keywords = limitedKeywords,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .padding(bottom = 14.dp)
                            )

                            MarkdownText(
                                markdown = section,
                                modifier = Modifier.padding(end = 4.dp),
                                keywords = limitedKeywords,
                                onKeywordClick = { keyword ->
                                    searchViewModel.onKeywordClick(keyword)
                                }
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                        .pointerInput(Unit) {
                            detectTapGestures   (
                                onLongPress = {
                                    isExpanded = true
                                }
                            )
                        }
                ) {

                    var sliderPosition by remember { mutableStateOf(selectedIndex.toFloat()) }
                    // sliderPosition이 변경될 때마다 타이머를 재설정하는 로직 추가
                    var lastInteractionTime by remember { mutableStateOf(0L) }

                    // 1.5초 후 자동으로 닫히는 effect
                    LaunchedEffect(sliderPosition) {
                        lastInteractionTime = System.currentTimeMillis()

                        while (isExpanded) {
                            delay(100) // 100ms 간격으로 체크
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastInteractionTime >= 1500) { // 1.5초
                                isExpanded = false
                                break
                            }
                        }
                    }

                    if (!isExpanded) {
                        // 기본 목차 버튼들
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            sections.forEachIndexed { index, _ ->
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = if (index == activeIndex) {
                                                Color(0xFFFFCD69)
                                            } else {
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                            },
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    } else {
                        // 내부 Box에서 회전 처리
                        Box(
                            modifier = Modifier
                                .zIndex(1f)
                                .rotate(90f)
                                .width(150.dp)
                                .height(60.dp)
                                .offset(y = (screenWidth-480).dp)  // 부모 Box 내에서 중앙 정렬
                        ) {
                            Slider(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .width(150.dp)
                                    .offset(x = (2).dp),
                                value = sliderPosition,
                                onValueChange = { newValue ->
                                    lastInteractionTime = System.currentTimeMillis()
                                    val roundedValue = newValue.roundToInt()
                                    if (selectedIndex != roundedValue) {
                                        sliderPosition = newValue
                                        selectedIndex = roundedValue
                                        scrollToSection(selectedIndex)
                                    }
                                },
                                valueRange = 0f..(sections.size - 1).toFloat(),
                                steps = sections.size - 2,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFFFDEEB0),
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                    inactiveTrackColor = Color.Transparent
                                ),
                                thumb = {
                                    SliderDefaults.Thumb(
                                        modifier = Modifier.size(12.dp),  // thumb 크기 조정
                                        interactionSource = remember { MutableInteractionSource() },
                                        colors = SliderDefaults.colors(thumbColor = Color(0xFFFFCD69))
                                    )
                                },
                                track = { sliderPositions ->
                                    Canvas (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(10.dp)  // track의 높이 설정
                                            .offset(x = (-3).dp, y = (-4).dp)
                                    ) {
                                        val yCenter = size.height / 2
                                        val tickCount = (sections.size)
                                        val tickSpacing = size.width / (tickCount - 1)

                                        // 각 tick 위치에 원 그리기
                                        for (i in 0 until tickCount) {
                                            val x = i * tickSpacing
                                            drawCircle(
//                                                color = Color(0xFF000000).copy(alpha = 0.3f),
                                                color = Color(0xFFFDEEB0),
                                                radius = 10f,  // tick 크기 조절
                                                center = Offset(x, yCenter)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        item {
//            Column(modifier = Modifier.padding(16.dp)) {
//
//                // 썸네일 이미지 표시
//                cardDetail.thumbnailUrl?.let { url ->
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(200.dp)
//                            .background(
//                                color = MaterialTheme.colorScheme.surfaceVariant,
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                    ) {
//                        var isLoading by remember { mutableStateOf(true) }
//                        var isError by remember { mutableStateOf(false) }
//
//                        Image(
//                            painter = rememberAsyncImagePainter(
//                                model = url,
//                                onState = { state ->
//                                    isLoading = state is AsyncImagePainter.State.Loading
//                                    isError = state is AsyncImagePainter.State.Error
//                                }
//                            ),
//                            contentDescription = "썸네일",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier.fillMaxSize()
//                        )
//
//                        // 로딩 중 표시
//                        if (isLoading) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//
//                        // 에러 표시
//                        if (isError) {
//                            Text(
//                                text = "이미지를 불러올 수 없습니다",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.error,
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//
//                // 뉴스 제목
//                Text(
//                    text = cardDetail.title,
//                    style = MaterialTheme.typography.headlineMedium,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//
//                // 뉴스 관련 키워드
//                val limitedKeywords = cardDetail.keywords
//                Text(
//                    text = "키워드: ${limitedKeywords.take(3).joinToString(", ")}",
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//
//                // 뉴스 본문
////                Text(
////                    text = cardDetail.content,
////                    style = MaterialTheme.typography.bodyLarge,
////                    modifier = Modifier.padding(vertical = 8.dp)
////                )
//                MarkdownText(
//                    markdown = cardDetail.content,
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    keywords = limitedKeywords,
//                    onKeywordClick = { keyword ->
//                        searchViewModel.onKeywordClick(keyword)
//                    }
//                )
//
//
//
//                // 원본 뉴스 링크 -> URL 버튼
//                Button(
//                    onClick = { uriHandler.openUri(cardDetail.originalUrl) },
//                    modifier = Modifier.padding(vertical = 8.dp)
//                ) {
//                    Text("원문 보기")
//                }
//
//                // 뉴스 작성 날짜
//                Text(
//                    text = "생성 날짜: $formattedDate",
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(top = 8.dp)
//                )
//            }
//        }
//    }
//}

private fun splitMarkdownIntoSections(markdown: String): List<String> {
    return markdown.split(Regex("(?=^#\\s)", RegexOption.MULTILINE))
        .filter { it.isNotBlank() }
        .map { it.trim() }
}

// 섹션에서 제목 추출하는 함수
private fun getSectionTitle(section: String): String {
    // 첫 번째 '#' 으로 시작하는 라인을 찾아 제목으로 사용
    val firstLine = section.lines().firstOrNull { it.trimStart().startsWith("#") }
    return firstLine?.replace(Regex("^#+\\s*"), "") ?: "섹션"
}

private fun calculateActiveIndex(
    listState: LazyListState,
    headerItemCount: Int
): Int {
    val visibleItems = listState.layoutInfo.visibleItemsInfo
    if (visibleItems.isEmpty()) return 0

    val viewportHeight = listState.layoutInfo.viewportEndOffset.toFloat() - listState.layoutInfo.viewportStartOffset.toFloat()
    val viewportTop = listState.layoutInfo.viewportStartOffset.toFloat()

    // viewport의 25%-75% 영역을 "중앙 영역"으로 정의
    val centralZoneStart = viewportTop + (viewportHeight * 0.25f)
    val centralZoneEnd = viewportTop + (viewportHeight * 0.75f)

    // 중앙 영역에 가장 많이 포함된 아이템 찾기
    val centralItem = visibleItems.maxByOrNull { item ->
        val itemStart = item.offset.toFloat()
        val itemEnd = itemStart + item.size.toFloat()
        val overlapStart = maxOf(itemStart, centralZoneStart)
        val overlapEnd = minOf(itemEnd, centralZoneEnd)
        if (overlapEnd > overlapStart) overlapEnd - overlapStart else 0f
    }

    return centralItem?.let { item ->
        (item.index - headerItemCount).coerceIn(
            0,
            listState.layoutInfo.totalItemsCount - headerItemCount - 1
        )
    } ?: 0
}