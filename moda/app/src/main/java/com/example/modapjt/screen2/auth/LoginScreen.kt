package com.example.modapjt.screen2.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextButton
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
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit = {}
) {
    val state = viewModel.loginState.value

    LaunchedEffect(key1 = true) {
        viewModel.setOnLoginSuccess(onNavigateToHome)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(60.dp))  // 상단 여백 추가

        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp), // 아이콘 크기
            tint = Color.Unspecified
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onLoginEvent(LoginEvent.EmailChanged(it)) },
            label = { Text("이메일") },
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
                .padding(vertical = 16.dp),
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = { /* TODO: 아이디 찾기 */ }) {
                Text("아이디 찾기", color = Color.Gray)
            }
            Text(
                text = "|",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)  // | 의 세로 크기 증가
            )
            TextButton(onClick = { /* TODO: 비밀번호 찾기 */ }) {
                Text("비밀번호 찾기", color = Color.Gray)
            }
            Text(
                text = "|",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)  // | 의 세로 크기 증가
            )
            TextButton(onClick = onNavigateToSignUp) {
                Text("회원가입", color = Color.Gray)
            }
        }
    }
}