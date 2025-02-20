package com.example.modapjt.screen2.user

import UserProfileCard
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.bar.TitleHeaderBar
import com.example.modapjt.components.setting.SettingItem
import com.example.modapjt.domain.viewmodel.AuthViewModel
import com.example.modapjt.domain.viewmodel.UserViewModel
import com.example.modapjt.toktok.gesture.GestureService
import com.example.modapjt.toktok.gesture.GestureStateManager
import com.example.modapjt.toktok.overlay.OverlayService
import com.example.modapjt.toktok.overlay.OverlayStateManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPageScreen(
    navController: NavController,
    authViewModel: AuthViewModel, // AuthViewModel 추가
    currentRoute: String = ""
) {
    val viewModel: UserViewModel = viewModel()
    val context = LocalContext.current

    var showLogoutDialog by remember { mutableStateOf(false) }

    // 🌟 저장 방법 상태 관리
    var saveMode by remember { mutableStateOf(SaveMethod.GESTURE) }
    val isGestureActive by GestureStateManager.isGestureActive.collectAsState()
    val isOverlayActive by OverlayStateManager.isOverlayActive.collectAsState()


    val userStatus by viewModel.userStatus.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchUserStatus()
    }

    val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    val screenCaptureContract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                lateinit var serviceIntent: Intent

                // 권한 승인 후 서비스 시작
                when (saveMode) {
                    SaveMethod.OVERLAY -> {
                        serviceIntent = Intent(context, OverlayService::class.java)
                        .apply {
                            putExtra(OverlayService.EXTRA_RESULT_CODE, result.resultCode)
                            putExtra(OverlayService.EXTRA_DATA, data)
                        }
                        OverlayStateManager.setOverlayActive(true)
                    }
                    SaveMethod.GESTURE -> {
                        serviceIntent = Intent(context, GestureService::class.java)
                        .apply {
                            putExtra(GestureService.EXTRA_RESULT_CODE, result.resultCode)
                            putExtra(GestureService.EXTRA_DATA, data)
                        }
                        OverlayStateManager.setOverlayActive(true)
                    }
                    else -> { }
                }

                context.startForegroundService(serviceIntent)

                // 크롬 브라우저 실행
                Toast.makeText(context, "크롬 브라우저가 실행됩니다.", Toast.LENGTH_SHORT).show()
                val chromeIntent = Intent(Intent.ACTION_MAIN)
                chromeIntent.setPackage("com.android.chrome")
                chromeIntent.addCategory(Intent.CATEGORY_APP_BROWSER)
                chromeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chromeIntent)
                Log.d("OverlayService", "크롬 브라우저 실행")
            }
        }
    }

//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        val allGranted = permissions.entries.all { it.value }
//        if (allGranted) {
//            // 권한이 모두 승인되면 미디어 프로젝션 권한 요청
//            screenCaptureContract.launch(mediaProjectionManager.createScreenCaptureIntent())
//        } else {
//            Toast.makeText(context, "필요한 권한이 승인되지 않았습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }


//    val keywords by viewModel.interestKeywords.collectAsState(initial = emptyList())

    Scaffold(
        bottomBar = {
            navController?.let {
                BottomBarComponent(it, currentRoute)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TitleHeaderBar(titleName = "마이 페이지")

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 프로필 및 정보 통계
                item {
                    if (userStatus == null) {
                        CircularProgressIndicator()
                    } else {
                        UserProfileCard(
                            nickname = userStatus?.nickname ?: "사용자",
                            itemCount = userStatus?.allCount?.toIntOrNull() ?: 0
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = MaterialTheme.colorScheme.onTertiary, thickness = 6.dp, modifier = Modifier.padding(horizontal = 0.dp))

                        // "정보 통계" 제목 추가
                        Text(
                            text = "정보 통계",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp) // 좌우 패딩 + 상하 여백 추가
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary) // 테두리
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        navController.navigate("cardlistScreen?categoryId=1") // 내 정보 클릭 시 이동
                                    }) {
                                    Text(
                                        text = userStatus?.allCount ?: "0",
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(text = "내 정보", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSecondary)
                                }
                                Text(text = "|", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSecondary)
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        navController.navigate("bookmarkScreen") // 즐겨찾기 페이지로 이동
                                    }) {
                                    Text(
                                        text = userStatus?.bookmarkCount ?: "0",
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(text = "즐겨찾기", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSecondary)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
//                    Divider(color = MaterialTheme.colorScheme.onTertiary, thickness = 6.dp, modifier = Modifier.padding(horizontal = 0.dp))

                }

                // 제스처/오버레이 토글 카드
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Column(
//                            modifier = Modifier.padding(16.dp)
                        ) {
                            // 기존 Switch -> 커스텀 토글 버튼 변경
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "링크 저장 방법",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )

                                CustomToggleSwitch(
                                    saveMode = saveMode,
                                    onToggleChange = {
                                        if (isOverlayActive || isGestureActive) {
                                            Toast.makeText(
                                                context,
                                                "먼저 ${if (isOverlayActive) "오버레이" else "제스처"} 서비스를 종료해주세요.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@CustomToggleSwitch
                                        }

                                        saveMode = it
                                    }
                                )
                            }


                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (saveMode == SaveMethod.GESTURE) {
                                        if (!isGestureActive) {
                                            try {
                                                screenCaptureContract.launch(mediaProjectionManager.createScreenCaptureIntent())
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "크롬 브라우저를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            val serviceIntent = Intent(context, GestureService::class.java)
                                            context.stopService(serviceIntent)
                                            GestureStateManager.setOverlayActive(false)
                                        }
                                    }
                                    else if (saveMode == SaveMethod.OVERLAY){
                                        if (!isOverlayActive) {
                                            try {
                                                screenCaptureContract.launch(mediaProjectionManager.createScreenCaptureIntent())
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "크롬 브라우저를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            val serviceIntent = Intent(context, OverlayService::class.java)
                                            context.stopService(serviceIntent)
                                            OverlayStateManager.setOverlayActive(false)
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = when {
                                        saveMode == SaveMethod.GESTURE -> if (isGestureActive) "제스처 종료" else "제스처 시작"
                                        saveMode == SaveMethod.OVERLAY -> if (isOverlayActive) "오버레이 종료" else "오버레이 시작"
                                        else -> ""
                                    },
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = MaterialTheme.colorScheme.onTertiary, thickness = 6.dp, modifier = Modifier.padding(horizontal = 0.dp))

                }

                // 기타 메뉴 : 공지사항, 휴지통, ..
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        SettingItem(title = "MODA 200% 활용하기") { }
                        SettingItem(title = "공지사항") { }
//                        SettingItem(title = "휴지통") { }
                    }
                }

                // 로그아웃 버튼
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onTertiary)
                        ) {
                            Text("로그아웃", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                    if (showLogoutDialog) {
                        LogoutDialog(
                            viewModel = authViewModel,
                            navController = navController,
                            onDismiss = { showLogoutDialog = false }
                        )
                    }
                }
            }
        }
    }

}


//@Composable
//fun LogoutDialog(viewModel: AuthViewModel, navController: NavController, onDismiss: () -> Unit) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("로그아웃") },
//        text = { Text("로그아웃 하시겠습니까?") },
//        confirmButton = {
//            Button(onClick = {
//                viewModel.logout {
//                    navController.navigate("home") {
//                        popUpTo("home") { inclusive = true }
//                    }
//                }
//                onDismiss()
//            }) {
//                Text("확인")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("취소")
//            }
//        }
//    )
//}
// -> 테스트 진행
@Composable
fun LogoutDialog(viewModel: AuthViewModel, navController: NavController, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("로그아웃") },
        text = { Text("로그아웃 하시겠습니까?") },
        confirmButton = {
            Button(onClick = {
                viewModel.logout(navController) // ✅ 수정된 로그아웃 함수 호출
                onDismiss()
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}


@Composable
fun CustomToggleSwitch(
    saveMode: SaveMethod,
    onToggleChange: (SaveMethod) -> Unit
) {
    Row(
        modifier = Modifier
            .width(130.dp) // 버튼 전체 너비
            .height(35.dp) // 버튼 높이
            .background(MaterialTheme.colorScheme.onTertiary, shape = CircleShape) // 전체 배경색 (회색 계열)
            .padding(4.dp), // 안쪽 패딩 추가
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (saveMode == SaveMethod.OVERLAY) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                    shape = CircleShape
                )
                .clickable(
                    indication = null, // 클릭 효과 제거
                    interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                ) { onToggleChange(SaveMethod.OVERLAY) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "오버레이",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (saveMode == SaveMethod.OVERLAY) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (saveMode == SaveMethod.GESTURE) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                    shape = CircleShape
                )
                .clickable(
                    indication = null, // 클릭 효과 제거
                    interactionSource = remember { MutableInteractionSource() } // 기본 효과 제거
                ) { onToggleChange(SaveMethod.GESTURE) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "제스처",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (saveMode == SaveMethod.GESTURE) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

enum class SaveMethod {
    OVERLAY, GESTURE
}



// 아래는 기존 설정 코드 : 오버레이 때문에 혹시 몰라서 남겨둠 ,,,!!
//    Scaffold(
//        bottomBar = {
//            navController?.let {
//                BottomBarComponent(it, currentRoute)
//            }
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            MyPageHeader()
//
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                item {
//                    if (user == null) {
//                        CircularProgressIndicator()
//                    } else {
//                        UserProfileCard(
//                            profileImage = user?.profileImage,
//                            nickname = user?.nickname ?: "사용자"
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//
//                item {
//                    Divider(
//                        color = Color(0xFFDCDCDC),
//                        thickness = 4.dp,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//
//
//                item {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Text(
//                                text = "링크 저장 기능",
//                                style = MaterialTheme.typography.titleMedium,
//                                fontSize = 16.sp
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "클릭 시 브라우저로 이동합니다.",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = Color.Gray
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//
//                            Button(
//                                onClick = {
//                                    if (!isOverlayActive) {
//                                        try {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                                            permissionLauncher.launch(arrayOf(
//                                                android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION
//                                            ))
//                                            } else {
//                                                // 권한이 필요없는 경우 바로 미디어 프로젝션 권한 요청
//                                                screenCaptureContract.launch(mediaProjectionManager.createScreenCaptureIntent())
//                                            }
//                                        } catch (e: Exception) {
//                                            // 크롬이 설치되어 있지 않은 경우 알림 설정
//                                            Toast.makeText(context, "크롬 브라우저를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
//                                        }
//                                    } else {
//                                        val serviceIntent = Intent(context, OverlayService::class.java)
//                                        context.stopService(serviceIntent)
//                                        OverlayStateManager.setOverlayActive(false)
//                                    }
//                                },
//                                modifier = Modifier.fillMaxWidth(),
//                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC80))
//                            ) {
//                                Text(if (isOverlayActive) "오버레이 종료" else "오버레이 시작", color = Color.Black)
//                            }
//                        }
//                    }
//                }
//
//                item {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        SettingItem(title = "MODA 200% 활용하기") { /* TODO: 알림 설정 추가 */ }
//                        SettingItem(title = "휴지통") { /* TODO: 알림 설정 추가 */ }
//                        SettingItem(title = "로그아웃") { showLogoutDialog = true }
//                    }
//
//                    if (showLogoutDialog) {
//                        LogoutDialog(
//                            viewModel = authViewModel, // AuthViewModel 전달
//                            navController = navController,
//                            onDismiss = { showLogoutDialog = false }
//                        )
//                    }
//                }
//            }
//        }
//    }

