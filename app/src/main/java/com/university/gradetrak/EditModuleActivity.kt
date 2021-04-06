package com.university.gradetrak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.university.gradetrak.databinding.ActivityEditModuleBinding

class EditModuleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditModuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditModuleBinding.inflate(layoutInflater)
        addNavigationListener()
        setContentView(binding.root)
    }

    private fun addNavigationListener(){
        binding.tbEditModulePage.setNavigationOnClickListener {
            finish()
        }
    }
}