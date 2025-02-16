
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.modapjt.domain.viewmodel.AuthViewModel


//package com.example.modapjt.ui
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.example.modapjt.domain.viewmodel.AuthViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//@Composable
//fun SplashScreen(navController: NavHostController, viewModel: AuthViewModel) {
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            delay(1000) // ✅ 스플래시 유지 (1초)
//            viewModel.checkLoginStatus()
//
//            if (viewModel.isLoggedIn.value) {
//                navController.navigate("home") {
//                    popUpTo("splash") { inclusive = true }
//                }
//            } else {
//                navController.navigate("login") {
//                    popUpTo("splash") { inclusive = true }
//                }
//            }
//        }
//    }
//
//    // ✅ 로딩 화면
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        CircularProgressIndicator(modifier = Modifier.size(50.dp))
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("앱을 로딩 중...")
//    }
//}


@Composable
fun SplashScreen(navController: NavHostController, viewModel: AuthViewModel) {
    LaunchedEffect(Unit) {
        viewModel.checkLoginStatus()

        if (viewModel.isLoggedIn.value) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("앱을 로딩 중...")
    }
}
