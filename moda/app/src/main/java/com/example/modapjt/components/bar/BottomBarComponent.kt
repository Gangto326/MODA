// components/home/HomeTopBar.kt
package com.example.modapjt.components.bar

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
import androidx.navigation.NavController
import java.lang.reflect.Modifier


@Composable
fun BottomBarComponent(navController: NavController, currentRoute: String) {
    NavigationBar(
        containerColor = Color.White, // 흰색 배경 (#FFFFFF)
        contentColor = Color(0xFF665F5B), // 아이콘과 텍스트를 검정색 (#000000)
//        modifier = Modifier.height(56.dp) // 바텀바 높이 설정
    ) {
        NavigationBarItem(
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF000000))
            },
            label = { Text("홈", color = Color(0xFF000000)) },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = if (currentRoute == "home") Color(0xFFFFF9C4) else Color.Transparent // ✅ 선택 시 연한 노란색
            )
        )

        // 즐겨찾기 탭
        NavigationBarItem(
            icon = {
                Icon(Icons.Filled.Star, contentDescription = "즐겨찾기", tint = Color(0xFF000000))
            },
            label = { Text("즐겨찾기", color = Color(0xFF000000)) },
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

        // 링크&파일 업로드 탭
        NavigationBarItem(
            icon = {
                Icon(Icons.Default.Share, contentDescription = "링크 추가", tint = Color(0xFF000000))
            },
            label = { Text("링크 추가", color = Color(0xFF000000)) },
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

        // 검색 탭
        NavigationBarItem(
            icon = {
                Icon(Icons.Default.Search, contentDescription = "검색", tint = Color(0xFF000000))
            },
            label = { Text("검색", color = Color(0xFF000000)) },
            selected = currentRoute == "search",
            onClick = {
                if (currentRoute != "search") {
                    navController.navigate("search") {
                        popUpTo("home")
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = if (currentRoute == "search") Color(0xFFFFF9C4) else Color.Transparent
            )
        )

        // 마이페이지 탭
        NavigationBarItem(
            icon = {
                Icon(Icons.Default.AccountCircle, contentDescription = "My Page", tint = Color(0xFF000000))
            },
            label = { Text("MY", color = Color(0xFF000000)) },
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


    }
}