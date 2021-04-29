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
import com.university.gradetrak.repositories.ModuleRepository
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.Services

class AddModuleActivity : BaseActivity() {
    private lateinit var binding: ActivityAddModuleBinding
    private val auth = Firebase.auth
    private val moduleRepository = ModuleRepository(auth.uid!!)
    private val viewModel: AddModuleViewModel by viewModels {
        AddModuleViewModelFactory(ModuleService(moduleRepository), Services.settingsService)
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
        observeSettings()
    }

    /**
     * Adds a listener to the toolbar nav arrow
     * Will finish the activity
     */
    private fun addNavigationListener(){
        binding.tbAddModulePage.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Setups the spinners with enum values defined in models
     */
    private fun setupDropdownMenus(){
        val credits = Credits.values()
        val creditsAdapter = ArrayAdapter(this, R.layout.spinner_item, credits)
        (binding.moduleCreditsLayout.editText as? AutoCompleteTextView)?.setAdapter(creditsAdapter)

        val level = Level.values()
        val levelAdapter = ArrayAdapter(this, R.layout.spinner_item, level)
        (binding.moduleLevelLayout.editText as? AutoCompleteTextView)?.setAdapter(levelAdapter)

    }

    /**
     * Observers errors in the view model, will show a snack bar with the error
     */
    private fun observeErrors(){
        viewModel.errorStringInt.observe(this, Observer {
            showSnackBar(resources.getString(it), true)
        })
    }

    /**
     * Observers successes in the view model, will show a snack bar with the succes
     */
    private fun observeSuccess(){
        viewModel.success.observe(this, { success ->
            if(success){
                finish()
            }
        })
    }

    /**
     * Observes settings in the [AddModuleViewModel]
     *
     * will set settings in the view model if the settings user id
     * matches [auth.uid]
     */
    private fun observeSettings(){
        viewModel.getUserSettings().observe(this, {
            for (settings in it){
                if(settings.userId == auth.uid){
                    viewModel.setSettings(settings)
                }
            }
        })
    }
}