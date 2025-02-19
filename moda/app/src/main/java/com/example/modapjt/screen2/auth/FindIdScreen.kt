package com.example.modapjt.screen2.auth

import android.view.ViewTreeObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.modapjt.R
import com.example.modapjt.domain.viewmodel.FindIdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindIdScreen(
    viewModel: FindIdViewModel,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.state.value
    val scrollState = rememberScrollState()
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var keyboardHeight by remember { mutableStateOf(0) }
    val focusManager = LocalFocusManager.current  // 여기에 FocusManager 추가

    val keyboardController = LocalSoftwareKeyboardController.current

    // 버튼 클릭 여부 상태 변수 추가
    var isRequestClicked by remember { mutableStateOf(false) }
    var isVerifyClicked by remember { mutableStateOf(false) }

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
            .background(MaterialTheme.colorScheme.tertiary)
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
            .padding(20.dp)
            .background(MaterialTheme.colorScheme.tertiary)
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

        // 이메일 입력 및 인증 부분
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
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = { Text("이메일") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 테두리
                        focusedBorderColor = MaterialTheme.colorScheme.onSecondary, // 포커스 테두리
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 해제 라벨
                        focusedLabelColor = MaterialTheme.colorScheme.onSecondary, // 포커스 라벨
                    ),
                )
                if (state.isEmailVerificationSent && state.remainingTime > 0) {
                    Text(
                        text = "남은 시간: ${state.remainingTime / 60}:${String.format("%02d", state.remainingTime % 60)}",
                        color = Color.Red,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
                state.emailVerificationMessage?.let { message ->
                    Text(
                        text = message,
                        color = if (message == "인증되었습니다.") Color.Blue else Color.Red,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }

            Button(
                onClick = { viewModel.sendVerification()
                    isRequestClicked = true // 버튼 클릭 후 상태 변경
                },
                modifier = Modifier
                    .height(64.dp)
                    .padding(top = 8.dp)
                    .width(100.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRequestClicked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary, // 클릭 전: 연회색, 클릭 후: 검정
                    contentColor = if (isRequestClicked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary // 클릭 전: 검정, 클릭 후: 흰색
                ),
                enabled = !state.isLoading
            ) {
                Text("인증요청")
            }
        }

// 인증번호 입력 필드
        if (state.isEmailVerificationSent) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    singleLine = true,// 한줄 입력으로 제한
                    maxLines = 1,
                    value = state.verificationCode,
                    onValueChange = { viewModel.onVerificationCodeChanged(it) },
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
                    onClick = { viewModel.verifyAndFindId()
                        isVerifyClicked = true // 버튼 클릭 후 상태 변경
                        },
                    modifier = Modifier
                        .height(64.dp)
                        .padding(top = 8.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVerifyClicked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary, // 클릭 전: 연회색, 클릭 후: 검정
                        contentColor = if (isVerifyClicked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onPrimary // 클릭 전: 검정, 클릭 후: 흰색
                    ),
                    enabled = !state.isLoading
                ) {
                    Text("확인")
                }
            }
        }

        // 찾은 아이디 표시
        if (state.foundId != null) {
            Text(
                text = "아이디 : " +state.foundId,
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // 에러 메시지 표시
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
            Text("로그인으로 돌아가기", color = MaterialTheme.colorScheme.onSecondary)
        }

        // 키보드가 보일 때 추가 공간 확보
        if (isKeyboardVisible) {
            Spacer(modifier = Modifier.height(keyboardHeight.dp))
        }
    }

    // 로딩 표시
    if (state.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }}
}