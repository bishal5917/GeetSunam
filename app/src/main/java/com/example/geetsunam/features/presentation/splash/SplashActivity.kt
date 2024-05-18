package com.example.geetsunam.features.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geetsunam.MainActivity
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.login.LoginActivity
import com.example.geetsunam.features.presentation.login.viewmodel.LoginEvent
import com.example.geetsunam.features.presentation.login.viewmodel.LoginState
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashState
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.CustomToast
import com.example.heartconnect.features.presentation.screens.splash.viewmodel.SplashEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel.onEvent(SplashEvent.CheckStatus)
        observe()
    }

    private fun observe() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        val mainIntent = Intent(this, MainActivity::class.java)
        splashViewModel.splashState.observe(this) { response ->
            if (response.status == SplashState.SplashStatus.LoggedIn) {
                startActivity(mainIntent)
                finish()
            }
            if (response.status == SplashState.SplashStatus.LoggedOut) {
                startActivity(loginIntent)
                finish()
            }
            if (response.status == SplashState.SplashStatus.SessionExpired) {
                loginViewModel.onEvent(LoginEvent.LogoutUser)
                CustomToast.showToast(this, response.message ?: "")
                startActivity(loginIntent)
                finish()
            }
        }
    }
}