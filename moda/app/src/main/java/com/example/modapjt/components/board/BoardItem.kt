package com.example.modapjt.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.domain.model.Board


@Composable
fun BoardItem(board: Board, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate("board/${board.boardId}") }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = board.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (board.isView) { // isView : 카드들 보여지는 보드
                Spacer(modifier = Modifier.height(8.dp))
                // 임시로 Box 사용
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 8.dp)
                                .background(Color.LightGray)
                        )
                    }
                }
            }
        }
    }
}