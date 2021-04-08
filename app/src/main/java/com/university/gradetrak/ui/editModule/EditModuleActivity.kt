package com.university.gradetrak.ui.editModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.university.gradetrak.utils.SELECTED_MODULE_ID_KEY
import com.university.gradetrak.utils.SELECTED_MODULE_KEY
import com.university.gradetrak.utils.TAG

class EditModuleActivity : BaseActivity() {
    private lateinit var binding: ActivityEditModuleBinding

    private val viewModel: EditModuleViewModel by viewModels {
        EditModuleViewModelFactory(Services.moduleService)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        addNavigationListener()
        observeErrors()
        observeSuccess()

        val selectedModule = intent.getParcelableExtra<Module>(SELECTED_MODULE_KEY)
        val selectedModuleId = intent.getStringExtra(SELECTED_MODULE_ID_KEY)
        if (selectedModule != null) {
            viewModel.selectedModule = selectedModule
            viewModel.selectedModule.uuid = selectedModuleId
            displayModule(selectedModule)
        }
    }

    private fun addNavigationListener(){
        binding.tbEditModulePage.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observeErrors(){
        viewModel.error.observe(this, Observer { error ->
            showSnackBar(error, true)
        })
    }

    private fun observeSuccess(){
        viewModel.success.observe(this, { success ->
            if(success){
                finish()
            }
        })
    }

    private fun displayModule(module : Module){
        viewModel.moduleName.set(module.name)
        if(module.result != null){
            viewModel.moduleResult.set(module.result.toString())
        }
    }
}