package com.example.modapjt.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modapjt.R

@Composable
fun CardDetailHeaderBar(
    title: String,
    isBookmarked: Boolean,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary)
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        // 왼쪽 뒤로가기 버튼
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    modifier = Modifier.size(15.dp),
                    contentDescription = "뒤로가기",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // 중앙 타이틀
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )

        //북마크 색상 다크모드
        val iconResource = if (isBookmarked) {
            if (isSystemInDarkTheme()) R.drawable.ic_d_bookmark else R.drawable.ic_bookmark_filled
        } else {
            R.drawable.ic_bookmark_outline
        }

        // 오른쪽 액션 버튼들
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
        ) {


            IconButton(onClick = onBookmarkClick) {
                Icon(
                    painter = painterResource(iconResource),
                    contentDescription = if (isBookmarked) "즐겨찾기 해제" else "즐겨찾기",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "더보기",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
//                    DropdownMenuItem(
//                        text = { Text("수정하기") },
//                        onClick = {
//                            onEditClick()
//                            showMenu = false
//                        }
//                    )
                    DropdownMenuItem(
                        text = { Text("삭제하기") },
                        onClick = {
                            onDeleteClick()
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}