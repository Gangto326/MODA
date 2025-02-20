package com.example.modapjt.screen2.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.modapjt.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class OAuthScreen : Fragment() {

    private val REQ_GOOGLE_SIGNIN = 1000 // 구글 로그인 요청 코드
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                OAuthScreenContent(
                    onGoogleSignInClick = { startGoogleSignIn() }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGoogleSignIn()
    }

    private fun initGoogleSignIn() {
        googleSignInClient = getGoogleClient()
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode(getString(R.string.google_login_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(requireActivity(), googleSignInOption)
    }

    private fun startGoogleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQ_GOOGLE_SIGNIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_GOOGLE_SIGNIN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                // 로그인 성공
                handleGoogleSignInSuccess(account)
            } catch (e: ApiException) {
                // 로그인 실패
                handleGoogleSignInError(e)
            }
        }
    }

    private fun handleGoogleSignInSuccess(account: GoogleSignInAccount) {
        val authCode = account.serverAuthCode // 서버에 전달할 인증 코드
        val email = account.email

        // TODO: 서버에 authCode 전송하고 자체 토큰 발급 받기
        Log.d("OAuth", "Google Sign-in success: $email")
    }

    private fun handleGoogleSignInError(e: ApiException) {
        Log.e("OAuth", "Google Sign-in failed: ${e.statusCode}")
        // TODO: 에러 처리 (사용자에게 메시지 표시 등)
    }
}

@Composable
fun OAuthScreenContent(
    onGoogleSignInClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 구글에서 제공하는 로그인 버튼
        GoogleSignInButton(
            onClick = onGoogleSignInClick
        )
    }
}