package com.example.geetsunam.features.presentation.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.geetsunam.R

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val signBtn = findViewById<Button>(R.id.btnSignupSubmit)
        signBtn.setOnClickListener {
            finish()
        }
    }
}