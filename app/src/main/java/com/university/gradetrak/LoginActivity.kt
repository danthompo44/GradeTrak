package com.university.gradetrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.university.gradetrak.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun handleLoginButtonClick(view: View){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun handleSignUpButtonClick(view: View){
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    fun handleForgottenPasswordButtonClick(view: View){
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }
}