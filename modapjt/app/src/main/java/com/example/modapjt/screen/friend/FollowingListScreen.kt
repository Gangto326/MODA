package com.example.modapjt.screen.friends

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// 팔로잉 리스트
@Composable
fun FollowingListScreen(navController: NavController) {
    val followingList = listOf("한윤지", "홍나연", "이미림") // 더미 데이터

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(followingList) { name ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(name, style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { /* TODO: 팔로우 */ }) {
                    Text("팔로우")
                }
            }
        }
    }
}
