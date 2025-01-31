package com.example.modapjt.screen.friends

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// 친구 추가 검색 화면
@Composable
fun FriendSearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember { mutableStateListOf("김강토", "이민수") } // 더미 데이터

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 🔍 검색 바 UI 개선 (돋보기 버튼 추가)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("친구 검색") },
            placeholder = { Text("이름을 입력하세요") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            trailingIcon = {
                IconButton(onClick = {
                    // TODO: 검색 기능 추가 (API 연동 예정)
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "검색")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 검색 결과 리스트
        LazyColumn {
            items(searchResults) { name ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(name, style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { /* TODO: 친구 추가 기능 */ }) {
                        Text("추가")
                    }
                }
            }
        }
    }
}
