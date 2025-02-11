// components/home/HomeTopBar.kt
package com.example.modapjt.components.bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
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
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Favorite, "Link Upload") },
//            label = { Text("디테일테스트") },
//            selected = currentRoute == "card_detail_test", // 변경: screen3 -> link_upload
//            onClick = {
//                if (currentRoute != "card_detail_test") {
//                    navController.navigate("card_detail_test") {
//                        popUpTo("home")
//                    }
//                }
//            }
//        )
    // screen2/마이페이지 테스트
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, "My Page") },
            label = { Text("마이페이지") },
            selected = currentRoute == "mypage",
            onClick = {
                if (currentRoute != "mypage") {
                    navController.navigate("mypage/user") { // ✅ userId 추가
                        popUpTo("home") { inclusive = false }
                    }
                }
            }
        )

        // 여기 아래는 나중에 삭제할 것!
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



//        // screen2/cardlist/newCardListScreen.kt 화면 테스트
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Settings, "Card List Test") },
//            label = { Text("검색바테스트") },
//            selected = currentRoute == "card_list_test",
//            onClick = {
//                if (currentRoute != "card_list_test") {
//                    navController.navigate("card_list_test") {
//                        popUpTo("home")
//                    }
//                }
//            }
//        )
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Favorite, "Favorite Card List") }, // 아이콘도 적절한 것으로 변경
//            label = { Text("즐겨찾기") }, // "검색바테스트" → "즐겨찾기"
//            selected = currentRoute == "favorite_card_list",
//            onClick = {
//                if (currentRoute != "favorite_card_list") {
//                    navController.navigate("favorite_card_list") {
//                        popUpTo("home")
//                    }
//                }
//            }
//        )




    }
}