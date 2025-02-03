// components/home/HomeTopBar.kt
package com.example.modapjt.components.bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController



@Composable
fun BottomBarComponent(navController: NavController, currentRoute: String) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, "Home") },
            label = { Text("홈") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, "Recommend") },
            label = { Text("추천 알고리즘") },
            selected = currentRoute == "recommend",
            onClick = {
                if (currentRoute != "recommend") {
                    navController.navigate("recommend") {
                        popUpTo("home")
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, "Link Upload") },
            label = { Text("링크 업로드") },
            selected = currentRoute == "link_upload", // ✅ 변경: screen3 -> link_upload
            onClick = {
                if (currentRoute != "link_upload") {
                    navController.navigate("link_upload") {
                        popUpTo("home")
                    }
                }
            }
        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Settings, "Screen 4") },
//            label = { Text("설정") },
//            selected = currentRoute == "screen4",
//            onClick = { /* TODO */ }
//        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, "Settings") },
            label = { Text("설정") },
            selected = currentRoute == "settings", // ✅ 변경: screen4 -> settings
            onClick = {
                if (currentRoute != "settings") {
                    navController.navigate("settings") {
                        popUpTo("home")
                    }
                }
            }
        )
    }
}