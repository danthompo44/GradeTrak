package com.university.gradetrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.university.gradetrak.R
import com.university.gradetrak.databinding.ActivityEnrolBinding

class EnrolActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnrolBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEnrolBinding.inflate(layoutInflater)

        addToolBarNavListeners()
        setContentView(binding.root)
    }

    /**
     * Adds a listener to the navigation part of the tool bar at the top of the UI.
     * Seen as a white arrow in the top left corner and a plus in the top right.
     * If the arrow is clicked the activity will finish, resorting to the still open main activity.
     * The plus opens the add module activity
     */
    private fun addToolBarNavListeners() {
        binding.tbEnrolPage.setNavigationOnClickListener {
            finish()
        }
        binding.ivEnrolToolbarAddModule.setOnClickListener {
            startActivity(Intent(this, AddModuleActivity::class.java))
        }
    }
}