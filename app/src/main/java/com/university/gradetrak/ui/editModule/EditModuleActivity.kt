package com.university.gradetrak.ui.editModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.university.gradetrak.BaseActivity
import com.university.gradetrak.R
import com.university.gradetrak.databinding.ActivityEditModuleBinding
import com.university.gradetrak.models.Credits
import com.university.gradetrak.models.Level
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.Services
import com.university.gradetrak.ui.addModule.AddModuleViewModel
import com.university.gradetrak.ui.addModule.AddModuleViewModelFactory
import com.university.gradetrak.utils.SELECTED_MODULE_KEY

class EditModuleActivity : BaseActivity() {
    private lateinit var binding: ActivityEditModuleBinding

    private val viewModel: EditModuleViewModel by viewModels {
        EditModuleViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        addNavigationListener()
        setupDropdownMenus()
        observeErrors()

        val selectedModule = intent.getParcelableExtra<Module>(SELECTED_MODULE_KEY)
        displayModule(selectedModule?.name)
    }

    private fun addNavigationListener(){
        binding.tbEditModulePage.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupDropdownMenus(){
        val credits = Credits.values()
        val creditsAdapter = ArrayAdapter(this, R.layout.spinner_item, credits)
        (binding.moduleCreditsLayout.editText as? AutoCompleteTextView)?.setAdapter(creditsAdapter)

        val level = Level.values()
        val levelAdapter = ArrayAdapter(this, R.layout.spinner_item, level)
        (binding.moduleLevelLayout.editText as? AutoCompleteTextView)?.setAdapter(levelAdapter)

    }

    private fun observeErrors(){
        viewModel.error.observe(this, Observer { error ->
            showSnackBar(error, true)
        })
    }

    private fun displayModule(moduleName : String?){
        viewModel.moduleName.set(moduleName)
    }

    private fun setupSpinners(){
//        ArrayAdapter.createFromResource(this, R.array.module_level_spinner_strings,
//            R.layout.spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(R.layout.spinner_item)
//            // Apply the adapter to the spinner
//            binding.moduleLevelSpinner.adapter = adapter
//        }
//        ArrayAdapter.createFromResource(this, R.array.module_credits_spinner_strings,
//            R.layout.spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(R.layout.spinner_item)
//            // Apply the adapter to the spinner
//            binding.moduleCreditsSpinner.adapter = adapter
//        }
    }
}