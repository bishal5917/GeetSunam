package com.example.geetsunam.features.presentation.login.forgot_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivityForgotPasswordBinding
import com.example.geetsunam.databinding.ActivityLoginBinding
import com.example.geetsunam.features.presentation.login.viewmodel.LoginEvent
import com.example.geetsunam.utils.Validation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        watchTextChange(binding.etForgotEmail, 0)
    }

    private fun watchTextChange(editText: EditText, index: Int) {
        editText.doOnTextChanged { text, start, before, count ->
            when (index) {
                0 -> {
                    val validationResult = Validation.validateEmail(text.toString())
                    binding.tvForgotEmailError.text = validationResult.message
//                    loginViewModel.onEvent(
//                        LoginEvent.EmailChanged(
//                            validationResult, text.toString()
//                        )
//                    )
                }
            }
        }
    }
}