package com.example.modapjt.screen2.auth

import android.view.ViewTreeObserver
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.modapjt.R
import com.example.modapjt.domain.model.LoginEvent
import com.example.modapjt.domain.model.LoginState
import com.example.modapjt.domain.viewmodel.AuthViewModel
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToFindId: () -> Unit = {},
    onNavigateToFindPassword: () -> Unit = {}
) {
    val state = viewModel.loginState.value
    var isKeyboardVisible by remember { mutableStateOf(false) }


    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current  // 추가

    // 화면이 처음 표시될 때 상태 초기화
    LaunchedEffect(Unit) {
        viewModel.resetLoginState()
    }

    // 키보드 가시성 감지
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val screenHeight = view.context.resources.displayMetrics.heightPixels
            val visibleFrameSize = android.graphics.Rect().apply {
                view.getWindowVisibleDisplayFrame(this)
            }
            val keyboardHeight = screenHeight - visibleFrameSize.bottom
            isKeyboardVisible = keyboardHeight > screenHeight * 0.15
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

//    LaunchedEffect(key1 = true) {
//        viewModel.setOnLoginSuccess(onNavigateToHome)
//    }
    // -> 변경
    // 로그인 성공 시 홈 화면으로 이동
    LaunchedEffect(key1 = true) {
        println("setOnLoginSuccess 실행됨")
        viewModel.setOnLoginSuccess {
            println("로그인 성공! 홈 화면으로 이동")
            onNavigateToHome()
        }
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                )
            }
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isKeyboardVisible) Arrangement.Top else Arrangement.Center
    ) {
        if (isKeyboardVisible) {
            Spacer(modifier = Modifier.height(60.dp))
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp),
            tint = Color.Unspecified // 원본 이미지 색상 유지
        )

        OutlinedTextField(
            singleLine = true,// 한줄 입력으로 제한
            maxLines = 1,
            value = state.username,
            onValueChange = { viewModel.onLoginEvent(LoginEvent.UsernameChanged(it)) },
            label = { Text("아이디") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 테두리
                focusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 테두리
                unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 라벨
                focusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 라벨
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            singleLine = true,// 한줄 입력으로 제한
            maxLines = 1,
            value = state.password,
            onValueChange = { viewModel.onLoginEvent(LoginEvent.PasswordChanged(it)) },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 테두리
                focusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 테두리
                unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 라벨
                focusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 라벨
            )
        )

        if (state.error != null) {
            Text(
                text = state.error,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Button(
            onClick = { viewModel.onLoginEvent(LoginEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp),
            enabled = !state.isLoading,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary // 버튼 배경색
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary) // 로딩 아이콘
            } else {
                Text("로그인", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = onNavigateToFindId) {
                Text("아이디 찾기", color = MaterialTheme.colorScheme.onSecondary)
            }
            Text(
                text = "|",
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            TextButton(onClick = onNavigateToFindPassword) {
                Text("비밀번호 찾기", color = MaterialTheme.colorScheme.onSecondary)
            }
            Text(
                text = "|",
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            TextButton(onClick = onNavigateToSignUp) {
                Text("회원가입", color = MaterialTheme.colorScheme.onSecondary)
            }
        }

    }}
}