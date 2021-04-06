package com.university.gradetrak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.university.gradetrak.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationListener()
    }

    private fun setupNavigationListener(){
        binding.tbEditModulePage.setNavigationOnClickListener {
            finish()
        }
    }
}