package com.example.modapjt.components.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.data.dto.response.SearchItem
import com.example.modapjt.domain.viewmodel.SearchViewModel
import kotlinx.coroutines.delay

@Composable
fun ThumbnailSlider(
//    viewModel: SearchViewModel = viewModel(),
//    navController: NavController,
    thumbnails: List<SearchItem>?,  // viewModel 대신 데이터만 받도록 수정
    navController: NavController,

    onLoadData: () -> Unit = {}
) {
//    val searchData by viewModel.searchData.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var dragOffset by remember { mutableStateOf(0f) }
//    val dragThreshold = 100f

    println(thumbnails?.size)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.value
    val dragThreshold = screenWidth * 0.4f // 화면 너비의 40%를 임계값으로 설정

    // targetIndex를 추가하여 애니메이션 방향 결정
    var targetIndex by remember { mutableStateOf(0) }

    // 기본 온보딩 이미지 리스트
    val defaultOnboardingImages = listOf(
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding1.jpg",
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding2.jpg",
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding3.jpg",
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding4.jpg",
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding5.jpg"
    )

    val totalItems by remember(thumbnails) {
        mutableStateOf(thumbnails?.size ?: defaultOnboardingImages.size)
    }
    val video123 = "ONBOARD"
    // 홈 화면에서 API 자동 호출
//    LaunchedEffect(Unit) {
//        viewModel.loadSearchData()
//    }

    // 4초마다 자동 슬라이드 추가
    LaunchedEffect(currentIndex) {
        while (true) {
            delay(4000)
            println(totalItems)
            if (totalItems > 1) {
                targetIndex = (currentIndex + 1) % totalItems
                currentIndex = targetIndex
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    // thumbnails 크기가 5보다 작으면 드래그 비활성화
                    if (thumbnails?.size == 5) {
                        detectHorizontalDragGestures(
                            onDragStart = { dragOffset = 0f },
                            onDragEnd = {
                                val listSize = thumbnails?.size ?: defaultOnboardingImages.size
                                when {
                                    dragOffset > dragThreshold -> {
                                        targetIndex = if (currentIndex == 0) listSize - 1 else currentIndex - 1
                                        currentIndex = targetIndex
                                    }
                                    dragOffset < -dragThreshold -> {
                                        targetIndex = (currentIndex + 1) % listSize
                                        currentIndex = targetIndex
                                    }
                                }
                                dragOffset = 0f
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                dragOffset += dragAmount
                            }
                        )
                    }
                }
        ) {
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    val direction = if (targetIndex > initialState) AnimatedContentTransitionScope.SlideDirection.Left
                    else AnimatedContentTransitionScope.SlideDirection.Right
                    slideIntoContainer(
                        towards = direction,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    ) togetherWith slideOutOfContainer(
                        towards = direction,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                },
                label = "thumbnail_slider"
            ) { index ->
                when {
                    thumbnails == null || thumbnails.isEmpty() -> {
                        TopThumbnail(
                            imageUrl = defaultOnboardingImages[index],
                            title = "",
                            content = "",
                            currentIndex = index,
                            totalItems = defaultOnboardingImages.size,
                            onClick = { /* 온보딩 이미지는 클릭 동작 없음 */ },
                            type = video123
                        )
                    }

                    thumbnails.size == 1 -> {
                        // 아이템이 1개일 때는 그냥 첫 번째 아이템 표시
                        val currentItem = thumbnails[0]
                        TopThumbnail(
                            imageUrl = currentItem.thumbnailUrl
                                ?: "https://example.com/default.jpg",
                            title = currentItem.title,
                            content = currentItem.thumbnailContent,
                            currentIndex = 0,
                            totalItems = 1,
                            type = currentItem.type?: "",
                            onClick = {
                                navController.navigate("cardDetail/${currentItem.cardId}")
                            }

                        )
                    }

                    else -> {
                        val currentItem = thumbnails[index]
                        TopThumbnail(
                            imageUrl = currentItem.thumbnailUrl
                                ?: "https://example.com/default.jpg",
                            title = currentItem.title,
                            content = currentItem.thumbnailContent,
                            currentIndex = index,
                            totalItems = thumbnails.size,
                            type = currentItem.type?: "",
                            onClick = {
                                navController.navigate("cardDetail/${currentItem.cardId}")
                            }
                        )
                    }
                }
            }
        }

        // ThumbnailIndicator 표시
//        val totalItems = if (thumbnails?.isEmpty() == true) {
//            defaultOnboardingImages.size
//        } else {
//            thumbnails?.size ?: defaultOnboardingImages.size
//        }
        ThumbnailIndicator(
            currentIndex = currentIndex,
            totalItems = totalItems
        )
    }
}