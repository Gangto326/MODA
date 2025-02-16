// components/home/HomeTopBar.kt
package com.example.modapjt.components.bar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun BottomBarComponent(navController: NavController, currentRoute: String) {
    Row( // ✅ NavigationBar를 감싸는 Row 추가 (좌우 패딩 조절용)
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp) // ✅ 양쪽 패딩 추가
    ) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color(0xFF665F5B),
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .height(56.dp) // ✅ 높이 줄이기 (기본 56.dp → 48.dp)
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF000000)) },
                label = { Text("홈", fontSize = 10.sp, color = Color(0xFF665F5B)) },
                selected = currentRoute == "home",
                onClick = {
                    if (currentRoute != "home") {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (currentRoute == "home") Color(0xFFFFF9C4) else Color.Transparent
                )
            )

            NavigationBarItem(
                icon = { Icon(Icons.Filled.Star, contentDescription = "즐겨찾기", tint = Color(0xFF000000)) },
                label = { Text("즐겨찾기", fontSize = 10.sp, color = Color(0xFF665F5B)) },
                selected = currentRoute == "bookmarkScreen",
                onClick = {
                    navController.navigate("bookmarkScreen") {
                        popUpTo("bookmarkScreen") { inclusive = true }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (currentRoute == "bookmarkScreen") Color(0xFFFFF9C4) else Color.Transparent
                )
            )

            NavigationBarItem(
                icon = { Icon(Icons.Default.Share, contentDescription = "링크 추가", tint = Color(0xFF000000)) },
                label = { Text("링크 추가", fontSize = 10.sp, color = Color(0xFF665F5B)) },
                selected = currentRoute == "file_upload_test",
                onClick = {
                    if (currentRoute != "file_upload_test") {
                        navController.navigate("file_upload_test") {
                            popUpTo("home")
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (currentRoute == "file_upload_test") Color(0xFFFFF9C4) else Color.Transparent
                )
            )

            NavigationBarItem(
                icon = { Icon(Icons.Default.Search, contentDescription = "검색", tint = Color(0xFF000000)) },
                label = { Text("검색", fontSize = 10.sp, color = Color(0xFF665F5B)) },
                selected = currentRoute == "afterSearch",
                onClick = {
                    if (currentRoute != "afterSearch") {
                        navController.navigate("afterSearch") {
                            popUpTo("home")
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (currentRoute == "afterSearch") Color(0xFFFFF9C4) else Color.Transparent
                )
            )

            NavigationBarItem(
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = "My Page", tint = Color(0xFF000000)) },
                label = { Text("MY", fontSize = 10.sp, color = Color(0xFF665F5B)) },
                selected = currentRoute == "mypage",
                onClick = {
                    if (currentRoute != "mypage") {
                        navController.navigate("mypage/user") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (currentRoute == "mypage") Color(0xFFFFF9C4) else Color.Transparent
                )
            )
        }
    }
}




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

        // 여기 아래는 나중에 삭제할 것!
//        NavigationBarItem(
//            icon = { Icon(Icons.Default.Settings, "Settings") },
//            label = { Text("설정") },
//            selected = currentRoute == "settings", // 변경: screen4 -> settings
//            onClick = {
//                if (currentRoute != "settings") {
//                    navController.navigate("settings") {
//                        popUpTo("home")
//                    }
//                }
//            }
//        )

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
