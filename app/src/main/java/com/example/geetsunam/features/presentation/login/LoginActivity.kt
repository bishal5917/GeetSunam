package com.example.geetsunam.features.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.geetsunam.MainActivity
import com.example.geetsunam.R
import com.example.geetsunam.features.presentation.login.viewmodel.LoginEvent
import com.example.geetsunam.features.presentation.login.viewmodel.LoginState
import com.example.geetsunam.features.presentation.login.viewmodel.LoginViewModel
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.LocalController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        loginBtn.setOnClickListener {
            LocalController().unfocusKeyboard(this)
            loginUser()
        }
        observeLiveData()
    }

    private fun loginUser() {
        val email = findViewById<EditText>(R.id.etLoginEmail).text.toString()
        val pass = findViewById<EditText>(R.id.etLoginPassword).text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            loginViewModel.onEvent(
                LoginEvent.LoginUser(
                    email = email, password = pass
                )
            )
        }
    }

    private fun observeLiveData() {
        loginViewModel.loginState.observe(this) { response ->
            if (response != null) {
                if (response.status == LoginState.LoginStatus.LOADING) {
                    //show loading dialog
                }
                if (response.status == LoginState.LoginStatus.SUCCESS) {
                    CustomToast.showToast(
                        context = this, "${
                            response.message
                        }"
                    )
                    val mainIntent = Intent(this, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }
                if (response.status == LoginState.LoginStatus.FAILED) {
                    CustomToast.showToast(
                        context = this, "${
                            response.message
                        }"
                    )
                }
            }
        }
    }
}