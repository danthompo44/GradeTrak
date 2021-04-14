package com.university.gradetrak.ui.addMark

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.university.gradetrak.BaseActivity
import com.university.gradetrak.databinding.ActivityAddMarkBinding
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.Services
import com.university.gradetrak.utils.SELECTED_MODULE_ID_KEY
import com.university.gradetrak.utils.SELECTED_MODULE_KEY

class AddMarkActivity : BaseActivity() {
    private lateinit var binding: ActivityAddMarkBinding

    private val viewModel: AddMarkViewModel by viewModels {
        AddMarkViewModelFactory(Services.moduleService)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMarkBinding.inflate(layoutInflater)
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
        viewModel.errorStringIntValue.observe(this, Observer {
            showSnackBar(resources.getString(it), true)
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