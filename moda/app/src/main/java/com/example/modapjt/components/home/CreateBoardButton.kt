// components/home/HomeTopBar.kt
package com.example.modapjt.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CreateBoardButton() {
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Create Board"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("보드 생성")
    }
}