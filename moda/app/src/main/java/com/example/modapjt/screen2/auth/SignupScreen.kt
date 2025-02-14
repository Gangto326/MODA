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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.modapjt.R
import com.example.modapjt.domain.model.SignUpEvent
import com.example.modapjt.domain.model.SignUpState
import com.example.modapjt.domain.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.signUpState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp),
            tint = Color.Unspecified
        )

        when (state.currentPage) {
            1 -> FirstSignUpPage(
                state = state,
                onEmailChanged = { viewModel.onSignUpEvent(SignUpEvent.EmailChanged(it)) },
                onNameChanged = { viewModel.onSignUpEvent(SignUpEvent.NameChanged(it)) },
                onVerifyEmail = { viewModel.onSignUpEvent(SignUpEvent.VerifyEmail) },
                onNextPage = { viewModel.onSignUpEvent(SignUpEvent.NextPage) },
                onNavigateBack = onNavigateBack
            )
            2 -> SecondSignUpPage(
                state = state,
                onPasswordChanged = { viewModel.onSignUpEvent(SignUpEvent.PasswordChanged(it)) },
                onConfirmPasswordChanged = { viewModel.onSignUpEvent(SignUpEvent.ConfirmPasswordChanged(it)) },
                onPreviousPage = { viewModel.onSignUpEvent(SignUpEvent.PreviousPage) },
                onSubmit = { viewModel.onSignUpEvent(SignUpEvent.Submit) },
                onNavigateBack = onNavigateBack
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstSignUpPage(
    state: SignUpState,
    onEmailChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onVerifyEmail: () -> Unit,
    onNextPage: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChanged,
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChanged,
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
                onClick = onVerifyEmail,
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

        if (state.isEmailVerified) {
            Text(
                text = "사용 가능한 이메일입니다",
                color = Color.Green,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNextPage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            enabled = state.isEmailVerified && state.name.isNotEmpty(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBBAEA4)
            )
        ) {
            Text("다음")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateBack) {
            Text("로그인으로 돌아가기", color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondSignUpPage(
    state: SignUpState,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onPreviousPage: () -> Unit,
    onSubmit: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChanged,
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

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = onConfirmPasswordChanged,
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

        if (state.error != null) {
            Text(
                text = state.error,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onPreviousPage,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBBAEA4)
                )
            ) {
                Text("이전")
            }

            Button(
                onClick = onSubmit,
                modifier = Modifier.weight(1f),
                enabled = !state.isLoading && state.password.isNotEmpty() && state.password == state.confirmPassword,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBBAEA4)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("회원가입")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateBack) {
            Text("로그인으로 돌아가기", color = Color.Gray)
        }
    }
}