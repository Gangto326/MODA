package com.example.modapjt.screen2.auth

import android.view.ViewTreeObserver
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.modapjt.R
import com.example.modapjt.domain.model.FindPasswordEvent
import com.example.modapjt.domain.viewmodel.FindPasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindPasswordScreen(
    viewModel: FindPasswordViewModel,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.state.value
    val scrollState = rememberScrollState()
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var keyboardHeight by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current  // 여기에 FocusManager 추가

    val keyboardController = LocalSoftwareKeyboardController.current

    // 비밀번호 재설정 성공 시 처리
    LaunchedEffect(state.isPasswordResetSuccessful) {
        if (state.isPasswordResetSuccessful) {
            // Toast 메시지 표시
            android.widget.Toast.makeText(
                context,
                "비밀번호 변경이 완료되었습니다",
                android.widget.Toast.LENGTH_SHORT
            ).show()
            // 상태 초기화
            viewModel.resetState()
            // 로그인 화면으로 돌아가기
            onNavigateBack()
        }
    }

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

        Spacer(modifier = Modifier.height(32.dp))

        // 아이디 입력
        OutlinedTextField(
            singleLine = true,// 한줄 입력으로 제한
            maxLines = 1,
            value = state.username,
            onValueChange = { viewModel.onFindPasswordEvent(FindPasswordEvent.UsernameChanged(it)) },
            label = { Text("아이디") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFBBAEA4),
                focusedBorderColor = Color(0xFFBBAEA4),
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFFBBAEA4),
            )
        )

        // 이메일 입력
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                singleLine = true,// 한줄 입력으로 제한
                maxLines = 1,
                value = state.email,
                onValueChange = { viewModel.onFindPasswordEvent(FindPasswordEvent.EmailChanged(it)) },
                label = { Text("이메일") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFBBAEA4),
                    focusedBorderColor = Color(0xFFBBAEA4),
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color(0xFFBBAEA4),
                )
            )

            Button(
                onClick = { viewModel.onFindPasswordEvent(FindPasswordEvent.SendVerification) },
                modifier = Modifier
                    .height(64.dp)
                    .width(100.dp)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBBAEA4)
                )
            ) {
                Text("인증요청")
            }
        }

        // 인증번호 입력 필드 (이메일 인증 요청 후 표시)
        if (state.isEmailVerificationSent) {
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
                        value = state.verificationCode,
                        onValueChange = { viewModel.onFindPasswordEvent(FindPasswordEvent.VerificationCodeChanged(it)) },
                        label = { Text("인증번호") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFBBAEA4),
                            focusedBorderColor = Color(0xFFBBAEA4),
                            unfocusedLabelColor = Color.Gray,
                            focusedLabelColor = Color(0xFFBBAEA4),
                        )
                    )
                    if (state.remainingTime > 0) {
                        Text(
                            text = "남은 시간: ${state.remainingTime / 60}:${String.format("%02d", state.remainingTime % 60)}",
                            color = Color.Red,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }

                Button(
                    onClick = { viewModel.onFindPasswordEvent(FindPasswordEvent.VerifyCode) },
                    modifier = Modifier
                        .height(64.dp)
                        .padding(top = 8.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFBBAEA4)
                    )
                ) {
                    Text("확인")
                }
            }
        }

        // 새 비밀번호 입력 (이메일 인증 완료 후 표시)
        if (state.canChangePassword) {
            OutlinedTextField(
                singleLine = true,// 한줄 입력으로 제한
                maxLines = 1,
                value = state.newPassword,
                onValueChange = { viewModel.onFindPasswordEvent(FindPasswordEvent.NewPasswordChanged(it)) },
                label = { Text("새 비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFBBAEA4),
                    focusedBorderColor = Color(0xFFBBAEA4),
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color(0xFFBBAEA4),
                )
            )

            OutlinedTextField(
                singleLine = true,// 한줄 입력으로 제한
                maxLines = 1,
                value = state.confirmNewPassword,
                onValueChange = { viewModel.onFindPasswordEvent(FindPasswordEvent.ConfirmNewPasswordChanged(it)) },
                label = { Text("새 비밀번호 확인") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFBBAEA4),
                    focusedBorderColor = Color(0xFFBBAEA4),
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color(0xFFBBAEA4),
                )
            )

            Button(
                onClick = { viewModel.onFindPasswordEvent(FindPasswordEvent.SubmitNewPassword) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = state.newPassword.isNotEmpty() && state.newPassword == state.confirmNewPassword,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBBAEA4)
                )
            ) {
                Text("비밀번호 변경")
            }
        }

        if (state.error != null) {
            Text(
                text = state.error,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 로그인으로 돌아가기
        TextButton(onClick = onNavigateBack) {
            Text("로그인으로 돌아가기", color = Color.Gray)
        }

        // 키보드가 보일 때 추가 공간 확보
        if (isKeyboardVisible) {
            Spacer(modifier = Modifier.height(keyboardHeight.dp))
        }
    }}
}