//package com.example.modapjt.screen2.auth
//
//import android.view.ViewTreeObserver
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalView
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import com.example.modapjt.R
//import com.example.modapjt.domain.model.LoginEvent
//import com.example.modapjt.domain.viewmodel.AuthViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LoginScreen(
//    viewModel: AuthViewModel,
//    onNavigateToSignUp: () -> Unit,
//    onNavigateToHome: () -> Unit = {},
//    onNavigateToFindId: () -> Unit = {},
//    onNavigateToFindPassword: () -> Unit = {}
//) {
//    val state = viewModel.loginState.value
//    var isKeyboardVisible by remember { mutableStateOf(false) }
//
//    // 키보드 가시성 감지
//    val view = LocalView.current
//    DisposableEffect(view) {
//        val listener = ViewTreeObserver.OnGlobalLayoutListener {
//            val screenHeight = view.context.resources.displayMetrics.heightPixels
//            val visibleFrameSize = android.graphics.Rect().apply {
//                view.getWindowVisibleDisplayFrame(this)
//            }
//            val keyboardHeight = screenHeight - visibleFrameSize.bottom
//            isKeyboardVisible = keyboardHeight > screenHeight * 0.15
//        }
//
//        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
//        onDispose {
//            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
//        }
//    }
//
//    LaunchedEffect(key1 = true) {
//        viewModel.setOnLoginSuccess(onNavigateToHome)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = if (isKeyboardVisible) Arrangement.Top else Arrangement.Center
//    ) {
//        if (isKeyboardVisible) {
//            Spacer(modifier = Modifier.height(60.dp))
//        }
//
//        Icon(
//            painter = painterResource(id = R.drawable.ic_logo),
//            contentDescription = "Logo",
//            modifier = Modifier.size(80.dp),
//            tint = Color.Unspecified
//        )
//
//        OutlinedTextField(
//            value = state.username,
//            onValueChange = { viewModel.onLoginEvent(LoginEvent.UsernameChanged(it)) },
//            label = { Text("아이디") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp),
//            shape = RoundedCornerShape(8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedBorderColor = Color(0xFFBBAEA4),
//                focusedBorderColor = Color(0xFFBBAEA4),
//                unfocusedLabelColor = Color.Gray,
//                focusedLabelColor = Color(0xFFBBAEA4),
//            )
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = state.password,
//            onValueChange = { viewModel.onLoginEvent(LoginEvent.PasswordChanged(it)) },
//            label = { Text("비밀번호") },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp),
//            shape = RoundedCornerShape(8.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                unfocusedBorderColor = Color(0xFFBBAEA4),
//                focusedBorderColor = Color(0xFFBBAEA4),
//                unfocusedLabelColor = Color.Gray,
//                focusedLabelColor = Color(0xFFBBAEA4),
//            )
//        )
//
//        if (state.error != null) {
//            Text(
//                text = state.error,
//                color = Color.Red,
//                modifier = Modifier.padding(vertical = 16.dp)
//            )
//        }
//
//        Button(
//            onClick = { viewModel.onLoginEvent(LoginEvent.Submit) },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp),
//            enabled = !state.isLoading,
//            shape = RoundedCornerShape(8.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFBBAEA4)
//            )
//        ) {
//            if (state.isLoading) {
//                CircularProgressIndicator(color = Color.White)
//            } else {
//                Text("로그인")
//            }
//        }
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            TextButton(onClick = onNavigateToFindId ) {
//                Text("아이디 찾기", color = Color.Gray)
//            }
//            Text(
//                text = "|",
//                color = Color.Gray,
//                modifier = Modifier.padding(vertical = 12.dp)
//            )
//            TextButton(onClick = onNavigateToFindPassword) {
//                Text("비밀번호 찾기", color = Color.Gray)
//            }
//            Text(
//                text = "|",
//                color = Color.Gray,
//                modifier = Modifier.padding(vertical = 12.dp)
//            )
//            TextButton(onClick = onNavigateToSignUp) {
//                Text("회원가입", color = Color.Gray)
//            }
//        }
//    }
//}



// 테스트용 : 로그인만 남긴 코드
package com.example.modapjt.screen2.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.modapjt.R
import com.example.modapjt.domain.model.LoginEvent
import com.example.modapjt.domain.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit = {} // 로그인 성공 후 이동할 홈 화면
) {
    val state = viewModel.loginState.value

    // 로그인 성공 시 홈 화면으로 이동
    LaunchedEffect(key1 = true) {
        println("✅ setOnLoginSuccess 실행됨")
        viewModel.setOnLoginSuccess {
            println("✅ 로그인 성공! 홈 화면으로 이동")
            onNavigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ 아이디 입력 필드
        OutlinedTextField(
            value = state.username,
            onValueChange = { viewModel.onLoginEvent(LoginEvent.UsernameChanged(it)) },
            label = { Text("아이디") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFBBAEA4),
                focusedBorderColor = Color(0xFFBBAEA4),
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFFBBAEA4),
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ 비밀번호 입력 필드
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onLoginEvent(LoginEvent.PasswordChanged(it)) },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFBBAEA4),
                focusedBorderColor = Color(0xFFBBAEA4),
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFFBBAEA4),
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ 로그인 오류 메시지 표시
        if (state.error != null) {
            println("로그인 오류 ----------------")
            Text(
                text = state.error,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // ✅ 로그인 버튼
        Button(
            onClick = { viewModel.onLoginEvent(LoginEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            enabled = !state.isLoading,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBBAEA4)
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("로그인")
            }
        }
    }
}

