package com.example.geetsunam.features.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.geetsunam.MainActivity
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.login.LoginActivity
import com.example.geetsunam.features.presentation.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        delayAndNavigate()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun delayAndNavigate() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        val mainIntent = Intent(this, MainActivity::class.java)
        GlobalScope.launch {
            // Delay for 2 seconds
            delay(2000)
            startActivity(loginIntent)
            // Finish the SplashActivity so that the user cannot go back to it
            finish()
        }
    }
}