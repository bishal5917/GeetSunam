package com.example.geetsunam.features.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geetsunam.MainActivity
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.login.LoginActivity
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel.onEvent(SplashEvent.CheckStatus)
        delayAndNavigate()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun delayAndNavigate() {
        GlobalScope.launch {
            // Delay for 1 second
            delay(2000)
            checkTokenAndNavigate()
        }
    }

    private fun checkTokenAndNavigate() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        val mainIntent = Intent(this, MainActivity::class.java)
        if (splashViewModel.userFlow.value?.token == null) {
            startActivity(loginIntent)
            finish()
        } else {
            startActivity(mainIntent)
            finish()
        }
    }
}