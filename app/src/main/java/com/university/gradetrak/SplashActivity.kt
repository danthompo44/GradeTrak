package com.university.gradetrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.university.gradetrak.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadLoginActivity()
    }

    /**
     * Loads the login activity after a delay of 2.5 seconds
     */
    @Suppress("DEPRECATION")
    private fun loadLoginActivity(){
        Handler().postDelayed(
            {
                // Launch the Login Activity
                startActivity(Intent(this, LoginActivity::class.java))
                finish() //Close the activity
            },
            2500
        )
    }
}