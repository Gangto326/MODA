package com.example.modapjt

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.modapjt.data.storage.TokenManager
import com.example.modapjt.domain.viewmodel.AuthViewModel
import com.example.modapjt.domain.viewmodel.AuthViewModelFactory
import com.example.modapjt.navigation.NavGraph
import com.example.modapjt.toktok.BrowserAccessibilityService
import com.example.modapjt.toktok.gesture.GestureService
import com.example.modapjt.toktok.overlay.OverlayService
import com.example.modapjt.ui.theme.ModapjtTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var credentialManager: CredentialManager
    private lateinit var googleIdOption: GetGoogleIdOption


    companion object {
        private const val WEB_CLIENT_ID = "your_web_client_id_here"
        private const val TAG = "MainActivity"
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        credentialManager = CredentialManager.create(this)

        googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setNonce("your_nonce_here") // 실제 nonce 값으로 교체
            .build()


        // 권한 요청
        checkAccessibilityPermission()
        checkOverlayPermission()

        handleNotificationIntent(intent)
        Log.d("MainActivity", "앱 실행됨")
        Log.d("MainActivity", "인텐트 정보: $intent")

        intent?.extras?.let { bundle ->
            bundle.keySet().forEach { key ->
                Log.d("MainActivity", "Extra - $key: ${bundle.get(key)}")
            }
        }

        setContent {
            ModapjtTheme {
                // TokenManager 초기화 (Compose에서 remember로 관리)
                val context = LocalContext.current
                val tokenManager = remember { TokenManager(context) }

                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(tokenManager)
                )

                val navController = rememberAnimatedNavController()
                this@MainActivity.navController = navController  // 이 줄을 추가해야 합니다


                LaunchedEffect(Unit) {
                    println("런치 이펙트??? ")
                    intent?.getStringExtra("cardId")?.let { cardId ->
                        Log.d("MainActivity", "외부에서 전달된 cardId: $cardId")
                        navController.navigate("cardDetail/$cardId")
                    }
                }

                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    onStartOverlay = { checkOverlayPermission() }
                )
            }

        }
    }

    private fun startSignIn() {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@MainActivity
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                handleFailure(e)
            }
        }
    }
    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        when (credential) {
            is PublicKeyCredential -> {
                val responseJson = credential.authenticationResponseJson
                // TODO: 서버로 responseJson 전송
            }

            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                // TODO: 서버로 username과 password 전송
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        // TODO: 서버로 토큰 전송
                        val idToken = googleIdTokenCredential.idToken
                        Log.d(TAG, "Google ID Token: $idToken")

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun handleFailure(e: GetCredentialException) {
        Log.e(TAG, "Error getting credential", e)
        // TODO: 에러 처리 (예: 사용자에게 토스트 메시지 표시)
        Toast.makeText(this, "로그인 실패: ${e.message}", Toast.LENGTH_SHORT).show()
    }

    // 오버레이 권한 요청 함수
    private val OVERLAY_PERMISSION_REQUEST_CODE = 1234
    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
            Toast.makeText(
                this,
                "오버레이를 위해 권한이 필요합니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // 접근성 서비스 권한이 있는지 확인하는 함수
    private fun isAccessibilityServiceEnabled(): Boolean {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        val serviceComponent = ComponentName(packageName, BrowserAccessibilityService::class.java.name)
        return enabledServices?.contains(serviceComponent.flattenToString()) == true
    }

    // 접근성 서비스 권한을 요청하는 함수
    private fun checkAccessibilityPermission() {
        if (!isAccessibilityServiceEnabled()) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
            Toast.makeText(
                this,
                "URL 캡처를 위해 접근성 권한이 필요합니다. '${getString(R.string.app_name)}'을 활성화해주세요.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleNotificationIntent(intent: Intent?) {
        intent?.let {
            val cardId = it.getStringExtra("cardId")
            Log.d("MainActivity", "Received cardId: $cardId")

            cardId?.let { id ->
                // 네비게이션 로직
                navController?.navigate("cardDetail/$id")
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // 로그 추가
        Log.d("MainActivity", "onNewIntent 호출됨")

        // 모든 extras 로깅
        intent.extras?.let { bundle ->
            bundle.keySet().forEach { key ->
                Log.d("MainActivity", "onNewIntent Extra - $key: ${bundle.get(key)}")
            }
        }

        // cardId 로깅 및 네비게이션
        intent.getStringExtra("cardId")?.let { cardId ->
            Log.d("MainActivity", "onNewIntent로 전달된 cardId: $cardId")
            navController?.navigate("cardDetail/$cardId")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, OverlayService::class.java))
        stopService(Intent(this, GestureService::class.java))
    }
}
