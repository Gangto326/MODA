package com.example.modapjt.components.home

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.domain.viewmodel.SearchViewModel
import kotlinx.coroutines.delay

@Composable
fun ThumbnailSlider(
    viewModel: SearchViewModel = viewModel(),
    navController: NavController,
) {
    val searchData by viewModel.searchData.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var dragOffset by remember { mutableStateOf(0f) }
    val dragThreshold = 100f

    // 기본 온보딩 이미지 리스트
    val defaultOnboardingImages = listOf(
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding1.jpg",
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding2.jpg",
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding3.jpg",
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding4.jpg",
        "https://a805bucket.s3.ap-northeast-2.amazonaws.com/onboarding/onboarding5.jpg"
    )

    // 홈 화면에서 API 자동 호출
    LaunchedEffect(Unit) {
        viewModel.loadSearchData()
    }

    // 4초마다 자동 슬라이드 추가
    LaunchedEffect(currentIndex) {
        while (true) {
            delay(4000)
            val totalItems = if (searchData?.thumbnails?.isEmpty() == true) {
                defaultOnboardingImages.size
            } else {
                searchData?.thumbnails?.size ?: defaultOnboardingImages.size
            }
            currentIndex = (currentIndex + 1) % totalItems
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { dragOffset = 0f },
                        onDragEnd = {
                            val totalItems = if (searchData?.thumbnails?.isEmpty() == true) {
                                defaultOnboardingImages.size
                            } else {
                                searchData?.thumbnails?.size ?: defaultOnboardingImages.size
                            }
                            when {
                                dragOffset > dragThreshold -> {
                                    currentIndex = if (currentIndex == 0) totalItems - 1 else currentIndex - 1
                                }
                                dragOffset < -dragThreshold -> {
                                    currentIndex = (currentIndex + 1) % totalItems
                                }
                            }
                            dragOffset = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            dragOffset += dragAmount
                        }
                    )
                }
        ) {
            if (searchData?.thumbnails?.isEmpty() == true) {
                // 기본 온보딩 이미지 표시
                TopThumbnail(
                    imageUrl = defaultOnboardingImages[currentIndex],
                    title = "온보딩 이미지 ${currentIndex + 1}",
                    content = "환영합니다!",
                    currentIndex = currentIndex,
                    totalItems = defaultOnboardingImages.size,
                    onClick = { /* 온보딩 이미지는 클릭 동작 없음 */ }
                )
            } else {
                searchData?.thumbnails?.let { thumbnails ->
                    if (thumbnails.isNotEmpty()) {
                        val currentItem = thumbnails[currentIndex]
                        TopThumbnail(
                            imageUrl = currentItem.thumbnailUrl ?: "https://example.com/default.jpg",
                            title = currentItem.title,
                            content = currentItem.thumbnailContent,
                            currentIndex = currentIndex,
                            totalItems = thumbnails.size,
                            onClick = {
                                navController.navigate("cardDetail/${currentItem.cardId}")
                            }
                        )
                    }
                }
            }
        }

        // ThumbnailIndicator 표시
        val totalItems = if (searchData?.thumbnails?.isEmpty() == true) {
            defaultOnboardingImages.size
        } else {
            searchData?.thumbnails?.size ?: defaultOnboardingImages.size
        }
        ThumbnailIndicator(
            currentIndex = currentIndex,
            totalItems = totalItems
        )
    }
}