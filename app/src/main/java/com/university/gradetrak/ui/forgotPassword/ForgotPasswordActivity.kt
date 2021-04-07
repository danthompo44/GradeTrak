package com.university.gradetrak.ui.forgotPassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.university.gradetrak.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationListener()
    }

    private fun setupNavigationListener(){
        binding.tbForgotPasswordPage.setNavigationOnClickListener {
            finish()
        }
    }

    fun handleSubmitButtonClick(view: View){
        finish()
    }
}