package com.example.modapjt.screen2.auth

import android.view.ViewTreeObserver
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.modapjt.R
import com.example.modapjt.domain.model.FindIdEvent
import com.example.modapjt.domain.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindIdScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.findIdState.value
    val scrollState = rememberScrollState()
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var keyboardHeight by remember { mutableStateOf(0) }

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

        Spacer(modifier = Modifier.height(32.dp))

        // 이메일 입력
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onFindIdEvent(FindIdEvent.EmailChanged(it)) },
                label = { Text("이메일") },
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
                onClick = { viewModel.onFindIdEvent(FindIdEvent.SendVerification) },
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

        // 인증번호 입력 필드 (이메일 인증 요청 후 표시)
        if (state.isEmailVerificationSent) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.verificationCode,
                    onValueChange = { viewModel.onFindIdEvent(FindIdEvent.VerificationCodeChanged(it)) },
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
                    onClick = { viewModel.onFindIdEvent(FindIdEvent.VerifyCode) },
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

        // 찾은 아이디 표시
        if (state.foundUsername != null) {
            Text(
                text = "아이디: ${state.foundUsername}",
                modifier = Modifier.padding(vertical = 16.dp),
                color = Color(0xFF4A4A4A)
            )
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
    }
}