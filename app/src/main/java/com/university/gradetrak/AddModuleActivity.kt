package com.university.gradetrak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.university.gradetrak.databinding.ActivityAddModuleBinding

class AddModuleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddModuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddModuleBinding.inflate(layoutInflater)

        addNavigationListener()
        setupModuleLevelSpinner()
        setContentView(binding.root)
    }

    private fun addNavigationListener(){
        binding.tbAddModulePage.setNavigationOnClickListener {
            Log.v("GradeTrak", "Listener")
            finish()
        }
    }

    private fun setupModuleLevelSpinner(){
        ArrayAdapter.createFromResource(this, R.array.module_level_spinner_strings,
            R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_item)
            // Apply the adapter to the spinner
            binding.moduleLevelSpinner.adapter = adapter
        }
    }
}