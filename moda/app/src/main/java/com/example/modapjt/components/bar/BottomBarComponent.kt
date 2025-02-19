// components/home/HomeTopBar.kt
package com.example.modapjt.components.bar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.modapjt.R


@Composable
fun BottomBarComponent(navController: NavController, currentRoute: String) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // 화면 높이의 비율로 계산
    val barHeight = (screenHeight * 0.095f).coerceIn(48.dp, 80.dp)

    // 화면 크기에 따른 텍스트 크기 계산
    val iconSize = (screenWidth * 0.05f).coerceIn(20.dp, 28.dp)

    // 선택된 아이템과 선택되지 않은 아이템의 색상
    val selectedColor = MaterialTheme.colorScheme.onPrimary // 검은색
    val unselectedColor = MaterialTheme.colorScheme.onSecondary // 회색

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color(0xFF665F5B),
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
        ) {
            // 홈 아이콘
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_n_home),
                        contentDescription = "Home",
                        modifier = Modifier.size(iconSize),
                        tint = if (currentRoute == "home") selectedColor else unselectedColor
                    )
                },
                label = {
                    Text(
                        "홈",
                        fontSize = 10.sp,
                        color = if (currentRoute == "home") selectedColor else unselectedColor
                    )
                },
                selected = currentRoute == "home",
                onClick = {
                    if (currentRoute != "home") {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Unspecified,
                    unselectedIconColor = Color.Unspecified
                )
            )

            // 즐겨찾기 아이콘
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_n_bookmark_outline),
                        contentDescription = "즐겨찾기",
                        modifier = Modifier.size(iconSize),
                        tint = if (currentRoute == "bookmarkScreen") selectedColor else unselectedColor
                    )
                },
                label = {
                    Text(
                        "즐겨찾기",
                        fontSize = 10.sp,
                        color = if (currentRoute == "bookmarkScreen") selectedColor else unselectedColor
                    )
                },
                selected = currentRoute == "bookmarkScreen",
                onClick = {
                    navController.navigate("bookmarkScreen") {
                        popUpTo("bookmarkScreen") { inclusive = true }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Unspecified,
                    unselectedIconColor = Color.Unspecified
                )
            )

            // 링크 추가 아이콘
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_n_link),
                        contentDescription = "링크 추가",
                        modifier = Modifier.size(iconSize),
                        tint = if (currentRoute == "file_upload_test") selectedColor else unselectedColor
                    )
                },
                label = {
                    Text(
                        "링크 추가",
                        fontSize = 10.sp,
                        color = if (currentRoute == "file_upload_test") selectedColor else unselectedColor
                    )
                },
                selected = currentRoute == "file_upload_test",
                onClick = {
                    if (currentRoute != "file_upload_test") {
                        navController.navigate("file_upload_test") {
                            popUpTo("home")
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Unspecified,
                    unselectedIconColor = Color.Unspecified
                )
            )

            // 검색 아이콘
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_n_search),
                        contentDescription = "검색",
                        modifier = Modifier.size(iconSize),
                        tint = if (currentRoute == "afterSearch") selectedColor else unselectedColor
                    )
                },
                label = {
                    Text(
                        "검색",
                        fontSize = 10.sp,
                        color = if (currentRoute == "afterSearch") selectedColor else unselectedColor
                    )
                },
                selected = currentRoute == "afterSearch",
                onClick = {
                    if (currentRoute != "afterSearch") {
                        navController.navigate("afterSearch") {
                            popUpTo("home")
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Unspecified,
                    unselectedIconColor = Color.Unspecified
                )
            )

            // MY 아이콘
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_n_mypage),
                        contentDescription = "My Page",
                        modifier = Modifier.size(iconSize),
                        tint = if (currentRoute == "mypage") selectedColor else unselectedColor
                    )
                },
                label = {
                    Text(
                        "MY",
                        fontSize = 10.sp,
                        color = if (currentRoute == "mypage") selectedColor else unselectedColor
                    )
                },
                selected = currentRoute == "mypage",
                onClick = {
                    if (currentRoute != "mypage") {
                        navController.navigate("mypage/user") {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Unspecified,
                    unselectedIconColor = Color.Unspecified
                )
            )
        }
    }
}