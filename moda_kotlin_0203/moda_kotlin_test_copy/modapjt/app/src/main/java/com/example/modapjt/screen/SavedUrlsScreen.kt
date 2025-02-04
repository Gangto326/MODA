package com.example.modapjt.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modapjt.viewmodel.SavedUrlsViewModel

@Composable
fun SavedUrlsScreen(
    viewModel: SavedUrlsViewModel = viewModel()
) {
    val captures by viewModel.allCaptures.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(captures) { capture ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
//                    Text(text = "URL: ${capture.url}")
//                    Text(
//                        text = "저장 시간: ${formatDate(capture.timestamp)}",
//                        style = MaterialTheme.typography.bodySmall
//                    )

                    val isSharedLink = capture.url.startsWith("공유하기 버튼으로 저장된 URL")

                    Text(
                        text = if (isSharedLink) "🔗 공유한 링크" else "🌍 오버레이로 저장한 링크",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = capture.url)

                    Text(
                        text = "저장 시간: ${formatDate(capture.timestamp)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    return android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", timestamp).toString()
}





//package com.example.modapjt.screen
//
//import android.content.Context
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.modapjt.viewmodel.SavedUrlsViewModel
//
//@Composable
//fun SavedUrlsScreen(
//    viewModel: SavedUrlsViewModel = viewModel()
//) {
//    val context = LocalContext.current // 여기서 context 가져오기
//    val captures by viewModel.allUrls.collectAsState()
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        items(captures) { capture ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(text = if (capture.source == "overlay") "🌍 오버레이로 저장한 링크" else "🔗 공유한 링크")
//                    Text(text = capture.url)
//                }
//            }
//        }
//    }
//}
