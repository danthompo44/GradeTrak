package com.university.gradetrak.ui.editModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.university.gradetrak.R
import com.university.gradetrak.databinding.ActivityEditModuleBinding

class EditModuleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditModuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditModuleBinding.inflate(layoutInflater)
        addNavigationListener()
        setupSpinners()
        setContentView(binding.root)
    }

    private fun addNavigationListener(){
        binding.tbEditModulePage.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupSpinners(){
        ArrayAdapter.createFromResource(this, R.array.module_level_spinner_strings,
            R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_item)
            // Apply the adapter to the spinner
            binding.moduleLevelSpinner.adapter = adapter
        }
        ArrayAdapter.createFromResource(this, R.array.module_credits_spinner_strings,
            R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_item)
            // Apply the adapter to the spinner
            binding.moduleCreditsSpinner.adapter = adapter
        }
    }
}