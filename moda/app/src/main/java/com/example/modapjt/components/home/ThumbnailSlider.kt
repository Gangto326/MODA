package com.example.modapjt.components.home

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    userId: String
) {
    val searchData by viewModel.searchData.collectAsState()

    var currentIndex by remember { mutableStateOf(0) }
    var dragOffset by remember { mutableStateOf(0f) }
    val dragThreshold = 100f

    // ✅ 홈 화면에서 API 자동 호출
    LaunchedEffect(userId) {
        viewModel.loadSearchData(userId)
    }

    // ✅ 4초마다 자동 슬라이드 추가
    LaunchedEffect(currentIndex) {
        while (true) {
            delay(4000) // ✅ 4초마다 자동 슬라이드
            searchData?.thumbnails?.let { thumbnails ->
                if (thumbnails.isNotEmpty()) {
                    currentIndex = (currentIndex + 1) % thumbnails.size
                }
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
                    detectHorizontalDragGestures(
                        onDragStart = { dragOffset = 0f },
                        onDragEnd = {
                            when {
                                dragOffset > dragThreshold -> {
                                    currentIndex = if (currentIndex == 0) searchData?.thumbnails?.lastIndex ?: 0 else currentIndex - 1
                                }
                                dragOffset < -dragThreshold -> {
                                    currentIndex = (currentIndex + 1) % (searchData?.thumbnails?.size ?: 1)
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
            searchData?.thumbnails?.let { thumbnails ->
                if (thumbnails.isNotEmpty()) {
                    val currentItem = thumbnails[currentIndex]
                    TopThumbnail(
                        imageUrl = currentItem.thumbnailUrl ?: "https://example.com/default.jpg",
                        title = currentItem.title,
                        content = currentItem.thumbnailContent,
                        currentIndex = currentIndex, // ✅ 현재 인덱스 전달,
                        totalItems = thumbnails.size, // ✅ 전체 개수 전달
                        onClick = {
                            navController.navigate("cardDetail/${currentItem.cardId}")
                        }
                    )
                }
            }
        }

        // ✅ ThumbnailIndicator 추가 (썸네일 개수 표시)
        searchData?.thumbnails?.let { thumbnails ->
            if (thumbnails.isNotEmpty()) {
                ThumbnailIndicator(
                    currentIndex = currentIndex,
                    totalItems = thumbnails.size
                )
            }
        }
    }
}
