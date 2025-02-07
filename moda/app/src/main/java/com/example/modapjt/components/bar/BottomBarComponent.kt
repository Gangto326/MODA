// components/home/HomeTopBar.kt
package com.example.modapjt.components.bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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
            icon = { Icon(Icons.Default.Favorite, "Link Upload") },
            label = { Text("디테일테스트") },
            selected = currentRoute == "card_detail_test", // 변경: screen3 -> link_upload
            onClick = {
                if (currentRoute != "card_detail_test") {
                    navController.navigate("card_detail_test") {
                        popUpTo("home")
                    }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, "Settings") },
            label = { Text("설정") },
            selected = currentRoute == "settings", // 변경: screen4 -> settings
            onClick = {
                if (currentRoute != "settings") {
                    navController.navigate("settings") {
                        popUpTo("home")
                    }
                }
            }
        )
        // screen2/home/newHomeScreen.kt 화면 테스트
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, "Home Test") },
            label = { Text("홈테스트") },
            selected = currentRoute == "home_test", // 변경: screen4 -> settings
            onClick = {
                if (currentRoute != "home_test") {
                    navController.navigate("home_test") {
                        popUpTo("home")
                    }
                }
            }
        )

        // screen2/cardlist/newCardListScreen.kt 화면 테스트
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, "Card List Test") },
            label = { Text("검색바테스트") },
            selected = currentRoute == "card_list_test",
            onClick = {
                if (currentRoute != "card_list_test") {
                    navController.navigate("card_list_test") {
                        popUpTo("home")
                    }
                }
            }
        )

        // screen2/linkupload/newLinkUploadScreen.kt 화면 테스트
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, "File Upload Test") },
            label = { Text("파일업로드테스트") },
            selected = currentRoute == "file_upload_test", // 변경: screen4 -> settings
            onClick = {
                if (currentRoute != "file_upload_test") {
                    navController.navigate("file_upload_test") {
                        popUpTo("home")
                    }
                }
            }
        )

    }
}