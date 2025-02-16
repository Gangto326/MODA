package com.example.modapjt.presentation.auth.signup

import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.domain.model.SignUpEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onNavigateBack: () -> Unit,
    navController: NavController
) {
    val state = viewModel.signUpState.value
    val scrollState = rememberScrollState()
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var keyboardHeight by remember { mutableStateOf(0) }

    val context = LocalContext.current

    // 키보드 가시성 감지
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val screenHeight = view.context.resources.displayMetrics.heightPixels
            val visibleFrameSize = android.graphics.Rect().apply {
                view.getWindowVisibleDisplayFrame(this)
            }
            val newKeyboardHeight = screenHeight - visibleFrameSize.bottom
            isKeyboardVisible = newKeyboardHeight > screenHeight * 0.15
            if (isKeyboardVisible) {
                keyboardHeight = newKeyboardHeight
            }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 닉네임 입력
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(SignUpEvent.NameChanged(it)) },
            label = { Text("닉네임") },
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

        // 아이디 입력 및 중복 확인
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = state.username,
                    onValueChange = { viewModel.onEvent(SignUpEvent.UsernameChanged(it)) },
                    label = { Text("아이디") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFBBAEA4),
                        focusedBorderColor = Color(0xFFBBAEA4),
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFBBAEA4),
                    )
                )
                state.usernameVerificationMessage?.let { message ->
                    Text(
                        text = message,
                        color = if (state.isUsernameVerified) Color.Green else Color.Red,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }

            Button(
                onClick = { viewModel.onEvent(SignUpEvent.VerifyUsername) },
                modifier = Modifier
                    .height(56.dp)
                    .width(100.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBBAEA4)
                )
            ) {
                Text("중복확인")
            }
        }

        // 이메일 입력 및 인증
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEvent(SignUpEvent.EmailChanged(it)) },
                    label = { Text("이메일") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFBBAEA4),
                        focusedBorderColor = Color(0xFFBBAEA4),
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFBBAEA4),
                    )
                )
                if (state.isTimerRunning) {
                    Text(
                        text = "남은 시간: ${state.remainingTime / 60}:${
                            String.format(
                                "%02d",
                                state.remainingTime % 60
                            )
                        }",
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
                state.emailVerificationMessage?.let { message ->
                    Text(
                        text = message,
                        color = if (message == "인증되었습니다.") Color.Green else Color.Red,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }

            Button(
                onClick = { viewModel.onEvent(SignUpEvent.SendEmailVerification) },
                enabled = state.isEmailFieldValid() && !state.isEmailVerified,
                modifier = Modifier
                    .height(56.dp)
                    .width(100.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBBAEA4)
                )
            ) {
                Text("인증요청")
            }
        }

        // 이메일 인증 코드 입력
        if (state.isEmailVerificationSent && !state.isEmailVerified) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.emailVerificationCode,
                    onValueChange = { viewModel.onEvent(SignUpEvent.EmailVerificationCodeChanged(it)) },
                    label = { Text("인증번호") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFBBAEA4),
                        focusedBorderColor = Color(0xFFBBAEA4),
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color(0xFFBBAEA4),
                    )
                )

                Button(
                    onClick = { viewModel.onEvent(SignUpEvent.VerifyEmailCode) },
                    modifier = Modifier
                        .height(56.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBBAEA4)
                    )
                ) {
                    Text("확인")
                }
            }
        }

        // 비밀번호 입력
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onEvent(SignUpEvent.PasswordChanged(it)) },
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

        // 비밀번호 확인
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged(it)) },
                label = { Text("비밀번호 확인") },
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
            state.passwordMatchMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }

        if (state.error != null) {
            Text(
                text = state.error,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회원가입 버튼
        // 회원가입 버튼
        Button(
            onClick = {
                viewModel.onEvent(SignUpEvent.Submit)
                // 여기서 직접 navigate하지 않음
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            enabled = !state.isLoading && state.isValid(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBBAEA4)
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("회원가입")
            }
        }
// UI 이벤트 수집 부분 수정
        LaunchedEffect(key1 = true) {
            viewModel.uiEvent.collect { event ->
                Log.d("SignUpScreen", "UI Event 발생: $event") // ✅ 확인용 로그 추가
                when (event) {
                    is SignUpViewModel.UiEvent.SignUpSuccess -> {
                        Log.d("SignUpScreen", "로그인 페이지로 이동합니다.") // ✅ 확인용 로그 추가
                        Toast.makeText(
                            context,
                            "회원가입에 성공하셨습니다! 🎉",
                            Toast.LENGTH_SHORT
                        ).show()
                        // onNavigateBack() 대신 로그인 페이지로 직접 네비게이션
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = false } // ✅ "home"까지 유지하면서 "signup"만 제거
                        }
                    }

                    is SignUpViewModel.UiEvent.ShowError -> {
                        Toast.makeText(
                            context,
                            event.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}