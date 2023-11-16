package com.example.geetsunam.features.presentation.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import com.example.geetsunam.R
import com.example.geetsunam.databinding.ActivitySignupBinding
import com.example.geetsunam.features.presentation.home.search.viewmodel.SearchEvent
import com.example.geetsunam.utils.CustomToast
import com.example.geetsunam.utils.LogTag
import com.example.geetsunam.utils.Validation
import com.example.geetsunam.utils.models.QueryRequestModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //on text changed
        watchTextChange(binding.etFullName, 0)
        watchTextChange(binding.etSignupEmail, 1)
        watchTextChange(binding.etSignupPassword, 2)
        watchTextChange(binding.etConfirmPassword, 3)
    }

    private fun watchTextChange(editText: EditText, index: Int) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                when (index) {
                    0 -> {
                        val validationResult = Validation.validateName(s.toString())
                        binding.tvNameError.text = validationResult.message
                    }

                    1 -> {
                        val validationResult = Validation.validateEmail(s.toString())
                        binding.tvEmailError.text = validationResult.message
                    }

                    2 -> {
                        val validationResult = Validation.validatePassword(s.toString())
                        binding.tvPasswordError.text = validationResult.message
                    }

                    3 -> {
                        val validationResult = Validation.confirmPassword(
                            binding
                                .etSignupPassword.text.toString(), s.toString()
                        )
                        binding.tvConfirmPasswordError.text = validationResult.message
                    }
                }
            }
        })
    }
}