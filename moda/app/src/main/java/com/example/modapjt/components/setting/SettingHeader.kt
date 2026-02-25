package com.example.modapjt.components.setting
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.modapjt.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingHeader(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp) }, // 제목
        navigationIcon = { // 🔹 왼쪽 네비게이션 아이콘 (뒤로 가기)
            IconButton(onClick = onBackClick) {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "뒤로가기", Modifier.size(15.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White // 배경색 설정
        )
    )
}
