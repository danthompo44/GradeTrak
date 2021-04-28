package com.university.gradetrak.ui.addMark

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.BaseActivity
import com.university.gradetrak.databinding.ActivityAddMarkBinding
import com.university.gradetrak.models.Module
import com.university.gradetrak.repositories.ModuleRepository
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.utils.SELECTED_MODULE_ID_KEY
import com.university.gradetrak.utils.SELECTED_MODULE_KEY

class AddMarkActivity : BaseActivity() {
    private lateinit var binding: ActivityAddMarkBinding
    private val auth = Firebase.auth
    private val moduleRepository = ModuleRepository(auth.uid!!)

    private val viewModel: AddMarkViewModel by viewModels {
        AddMarkViewModelFactory(ModuleService(moduleRepository))
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

        //Retrieve module and it's id from intent
        val selectedModule = intent.getParcelableExtra<Module>(SELECTED_MODULE_KEY)
        val selectedModuleId = intent.getStringExtra(SELECTED_MODULE_ID_KEY)
        if (selectedModule != null) {
            viewModel.selectedModule = selectedModule
            viewModel.selectedModule.uuid = selectedModuleId
            displayModule(selectedModule)
        }
    }

    /**
     * Adds a listener to the toolbar nav arrow
     * Will finish the activity
     */
    private fun addNavigationListener(){
        binding.tbEditModulePage.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Observers errors in the view model, will show a snack bar with the error
     */
    private fun observeErrors(){
        viewModel.errorStringIntValue.observe(this, Observer {
            showSnackBar(resources.getString(it), true)
        })
    }

    /**
     * Observers successes in the view model, will show a snack bar with the success
     */
    private fun observeSuccess(){
        viewModel.success.observe(this, { success ->
            if(success){
                finish()
            }
        })
    }

    /**
     * Passes the module name and result to the viewmodel, updates Observable fields
     */
    private fun displayModule(module : Module){
        viewModel.moduleName.set(module.name)
        if(module.result != null){
            viewModel.moduleResult.set(module.result.toString())
        }
    }
}