package com.example.modapjt.components.user

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun InterestAndInfoSection(keywords: List<String>) { // ✅ 파라미터 추가
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        InterestKeywords(keywords) // ✅ 파라미터 전달
        Spacer(modifier = Modifier.height(16.dp))
    }
}



