package com.example.modapjt.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.modapjt.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight


//@Composable
//fun SelectBoard() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Button(
//            onClick = { /* TODO */ },
//            modifier = Modifier.weight(1f)
//        ) {
//            Text("내 보드")
//        }
//        Spacer(modifier = Modifier.width(8.dp))
//        Button(
//            onClick = { /* TODO */ },
//            modifier = Modifier.weight(1f)
//        ) {
//            Text("즐겨찾기 보드")
//        }
//    }
//}

@Composable
fun SelectBoard(
    onMyBoardsClick: () -> Unit,
    onBookMarkBoardsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onMyBoardsClick() }, // "내 보드" 버튼 클릭 시 호출
            modifier = Modifier.weight(1f)
        ) {
            Text("내 보드")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = { onBookMarkBoardsClick() }, // "즐겨찾기 보드" 버튼 클릭 시 호출
            modifier = Modifier.weight(1f)
        ) {
            Text("즐겨찾기 보드")
        }
    }
}
