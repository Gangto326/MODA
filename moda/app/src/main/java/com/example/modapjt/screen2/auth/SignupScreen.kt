package com.example.modapjt.presentation.auth.signup

import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.modapjt.R
import com.example.modapjt.domain.model.SignUpEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager


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
    val focusManager = LocalFocusManager.current  // 여기에 FocusManager 추가

    // 화면이 처음 표시될 때 상태 초기화
    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

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

    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.tertiary)
        .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null // 클릭 효과 제거
    ){
        // 화면 클릭 시 키보드 숨기기
        focusManager.clearFocus()
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Icon(
                painter = painterResource(
                    id = if (isSystemInDarkTheme()) R.drawable.ic_d_logo else R.drawable.ic_logo
                ),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp),
                tint = Color.Unspecified // 원본 이미지 색상 유지
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 닉네임 입력
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(SignUpEvent.NameChanged(it)) },
                label = { Text("닉네임") },
                singleLine = true,// 한줄 입력으로 제한
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 테두리
                    focusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 테두리
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 라벨
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 라벨
                ),
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
                        singleLine = true,// 한줄 입력으로 제한
                        maxLines = 1,
                        value = state.username,
                        onValueChange = { viewModel.onEvent(SignUpEvent.UsernameChanged(it)) },
                        label = { Text("아이디") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 테두리
                            focusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 테두리
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 라벨
                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 라벨
                        )
                    )
                    state.usernameVerificationMessage?.let { message ->
                        Text(
                            text = message,
                            color = if (state.isUsernameVerified) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onError,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }

                Button(
                    onClick = { viewModel.onEvent(SignUpEvent.VerifyUsername) },
                    modifier = Modifier
                        .height(64.dp)
                        .width(100.dp)
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("중복확인", color = MaterialTheme.colorScheme.tertiary)
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
                        singleLine = true,// 한줄 입력으로 제한
                        maxLines = 1,
                        value = state.email,
                        onValueChange = { viewModel.onEvent(SignUpEvent.EmailChanged(it)) },
                        label = { Text("이메일") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 테두리
                            focusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 테두리
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 라벨
                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 라벨
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
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                    state.emailVerificationMessage?.let { message ->
                        Text(
                            text = message,
                            color = if (message == "인증되었습니다.") MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onError,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }

                Button(
                    onClick = { viewModel.onEvent(SignUpEvent.SendEmailVerification) },
                    enabled = state.isEmailFieldValid() && !state.isEmailVerified,
                    modifier = Modifier
                        .height(64.dp)
                        .width(100.dp)
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isEmailFieldValid() && !state.isEmailVerified) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary, // 활성화: 검정색, 비활성화: 연회색
                        contentColor = if (state.isEmailFieldValid() && !state.isEmailVerified) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary // 활성화: 흰색, 비활성화: 검정색
                    ),
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
                        singleLine = true,// 한줄 입력으로 제한
                        maxLines = 1,
                        value = state.emailVerificationCode,
                        onValueChange = {
                            viewModel.onEvent(
                                SignUpEvent.EmailVerificationCodeChanged(
                                    it
                                )
                            )
                        },
                        label = { Text("인증번호") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 테두리
                            focusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 테두리
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 라벨
                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 라벨
                        )
                    )

                    Button(
                        onClick = { viewModel.onEvent(SignUpEvent.VerifyEmailCode) },
                        modifier = Modifier
                            .height(64.dp)
                            .width(100.dp)
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isEmailFieldValid() && !state.isEmailVerified) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary, // 활성화: 검정색, 비활성화: 연회색
                            contentColor = if (state.isEmailFieldValid() && !state.isEmailVerified) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary // 활성화: 흰색, 비활성화: 검정색
                        ),
                    ) {
                        Text("확인")
                    }
                }
            }

            // 비밀번호 입력
            OutlinedTextField(
                singleLine = true,// 한줄 입력으로 제한
                maxLines = 1,
                value = state.password,
                onValueChange = { viewModel.onEvent(SignUpEvent.PasswordChanged(it)) },
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

            // 비밀번호 확인
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    singleLine = true,// 한줄 입력으로 제한
                    maxLines = 1,
                    value = state.confirmPassword,
                    onValueChange = { viewModel.onEvent(SignUpEvent.ConfirmPasswordChanged(it)) },
                    label = { Text("비밀번호 확인") },
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
                // 비밀번호 유효성 검사 메시지 추가
                state.passwordValidationMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 회원가입 버튼
            Button(
                onClick = {
                    viewModel.onEvent(SignUpEvent.Submit)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                enabled = !state.isLoading && state.isValid(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isEmailFieldValid() && !state.isEmailVerified) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary, // 활성화: 노란색, 비활성화: 연회색
                    contentColor = if (state.isEmailFieldValid() && !state.isEmailVerified) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary // 활성화: 흰색, 비활성화: 검정색
                ),
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("회원가입")
                }
            }

            // 키보드가 보일 때 추가 여백
            if (isKeyboardVisible) {
                Spacer(modifier = Modifier.height(keyboardHeight.dp + 100.dp))
            } else {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // UI 이벤트 수집
        LaunchedEffect(key1 = true) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is SignUpViewModel.UiEvent.SignUpSuccess -> {
                        Toast.makeText(
                            context,
                            "회원가입에 성공하셨습니다! 🎉",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = false }
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