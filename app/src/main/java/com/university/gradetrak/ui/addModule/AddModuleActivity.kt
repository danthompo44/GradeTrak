package com.university.gradetrak.ui.addModule

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.BaseActivity
import com.university.gradetrak.R
import com.university.gradetrak.databinding.ActivityAddModuleBinding
import com.university.gradetrak.models.Credits
import com.university.gradetrak.models.Level
import com.university.gradetrak.services.Services

class AddModuleActivity : BaseActivity() {
    private lateinit var binding: ActivityAddModuleBinding
    private val auth = Firebase.auth
    private val viewModel: AddModuleViewModel by viewModels {
        AddModuleViewModelFactory(Services.getModuleService(auth.uid!!), Services.settingsService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        addNavigationListener()
        setupDropdownMenus()
        observeErrors()
        observeSuccess()
    }

    private fun addNavigationListener(){
        binding.tbAddModulePage.setNavigationOnClickListener {
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
        viewModel.errorStringInt.observe(this, Observer {
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
}