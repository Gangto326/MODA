package com.example.modapjt.components.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modapjt.R

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp), // 둥근 직사각형
        shadowElevation = 0.dp // 그림자 제거
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 2.dp, color = Color(0xFFFFF176), shape = RoundedCornerShape(8.dp)) // 테두리 추가
                .padding(4.dp), // 테두리와 내부 여백 추가
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
                modifier = Modifier.size(23.dp).padding(start = 8.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        onSearch(it)
                    },
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                )

                if (searchText.isEmpty()) {
                    Text(
                        text = "찾고 싶은 내용을 입력하세요",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
