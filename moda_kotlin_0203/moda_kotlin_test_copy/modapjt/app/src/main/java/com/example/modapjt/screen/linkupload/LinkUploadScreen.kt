package com.example.modapjt.screen.linkupload

//import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavController

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TopBarComponent




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkUploadScreen(navController: NavController, currentRoute: String) {
    Scaffold(
        topBar = { TopBarComponent() },
        bottomBar = { BottomBarComponent(navController, currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(text = "Link Upload Screen Page")
        }
    }
}