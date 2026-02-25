// components/home/HomeTopBar.kt
package com.example.modapjt.components.bar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modapjt.R
import androidx.compose.ui.res.painterResource


@Composable
fun TopBarComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.moda_logo),
            contentDescription = "Moda Logo",
            modifier = Modifier.size(32.dp)
        )

        Row {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications, // 기본 알람 아이콘
                    contentDescription = "알림 아이콘"
                )
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        }

    }
}