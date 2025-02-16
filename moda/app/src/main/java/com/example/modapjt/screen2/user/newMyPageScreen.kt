package com.example.modapjt.screen2.user

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.modapjt.components.bar.BottomBarComponent
import com.example.modapjt.components.setting.SettingItem
import com.example.modapjt.components.user.InterestKeywords
import com.example.modapjt.components.user.MyPageHeader
import com.example.modapjt.components.user.UserProfileCard
import com.example.modapjt.domain.viewmodel.AuthViewModel
import com.example.modapjt.domain.viewmodel.UserViewModel
import com.example.modapjt.overlay.OverlayService
import com.example.modapjt.overlay.OverlayStateManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPageScreen(
    navController: NavController,
    authViewModel: AuthViewModel, // ✅ AuthViewModel 추가
    currentRoute: String = ""
) {
    val viewModel: UserViewModel = viewModel()
    val context = LocalContext.current

    val isOverlayActive by OverlayStateManager.isOverlayActive.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    val screenCaptureContract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                // 권한 승인 후 서비스 시작
                val serviceIntent = Intent(context, OverlayService::class.java)
                    .apply {
                        putExtra(OverlayService.EXTRA_RESULT_CODE, result.resultCode)
                        putExtra(OverlayService.EXTRA_DATA, data)
                    }
                OverlayStateManager.setOverlayActive(true)
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

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            // 권한이 모두 승인되면 미디어 프로젝션 권한 요청
            screenCaptureContract.launch(mediaProjectionManager.createScreenCaptureIntent())
        } else {
            Toast.makeText(context, "필요한 권한이 승인되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchUser()
    }

    val user by viewModel.user.collectAsState()
    val keywords by viewModel.interestKeywords.collectAsState(initial = emptyList())

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
            MyPageHeader()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    if (user == null) {
                        CircularProgressIndicator()
                    } else {
                        UserProfileCard(
                            profileImage = user?.profileImage,
                            nickname = user?.nickname ?: "사용자"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Divider(
                        color = Color(0xFFDCDCDC),
                        thickness = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    InterestKeywords(keywords)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "링크 저장 기능",
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "클릭 시 브라우저로 이동합니다.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (!isOverlayActive) {
                                        try {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            permissionLauncher.launch(arrayOf(
                                                android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION
                                            ))
                                            } else {
                                                // 권한이 필요없는 경우 바로 미디어 프로젝션 권한 요청
                                                screenCaptureContract.launch(mediaProjectionManager.createScreenCaptureIntent())
                                            }
                                        } catch (e: Exception) {
                                            // 크롬이 설치되어 있지 않은 경우 알림 설정
                                            Toast.makeText(context, "크롬 브라우저를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        val serviceIntent = Intent(context, OverlayService::class.java)
                                        context.stopService(serviceIntent)
                                        OverlayStateManager.setOverlayActive(false)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC80))
                            ) {
                                Text(if (isOverlayActive) "오버레이 종료" else "오버레이 시작", color = Color.Black)
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        SettingItem(title = "알림 설정") { /* TODO: 알림 설정 추가 */ }
                        SettingItem(title = "로그아웃") { showLogoutDialog = true }
                    }

                    // ✅ LazyColumn 내부에서 다이얼로그 호출
                    if (showLogoutDialog) {
//                        LogoutDialog(
//                            onConfirm = {
//                                showLogoutDialog = false
//                                navController.navigate("login") {
//                                    popUpTo("home") { inclusive = true }
//                                }
//                            },
//                            onDismiss = { showLogoutDialog = false }
//                        )
                        LogoutDialog(
                            viewModel = authViewModel, // ✅ AuthViewModel 전달
                            navController = navController,
                            onDismiss = { showLogoutDialog = false }
                        )
                    }
                }
            }
        }
    }
}

// ✅ 별도 @Composable 함수로 로그아웃 다이얼로그 분리
//@Composable
//fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("로그아웃") },
//        text = { Text("로그아웃 하시겠습니까?") },
//        confirmButton = {
//            Button(onClick = onConfirm) {
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
@Composable
fun LogoutDialog(viewModel: AuthViewModel, navController: NavController, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("로그아웃") },
        text = { Text("로그아웃 하시겠습니까?") },
        confirmButton = {
            Button(onClick = {
                viewModel.logout {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
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

