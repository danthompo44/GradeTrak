package com.university.gradetrak.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.MainActivity
import com.university.gradetrak.databinding.FragmentSettingsBinding
import com.university.gradetrak.repositories.ModuleRepository
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.Services

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private val auth = Firebase.auth
    private val moduleRepository = ModuleRepository(auth.uid!!)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupViewModelBinding()
        observeSettingsData()
        observeErrors()

        return binding.root
    }

    /**
     * Uses the [SettingsViewModelFactory] to create a [SettingsViewModel].
     * Passes a [Services.getModuleService] and [Services.settingsService]
     * into the factory.
     *
     * Will then setup databinding between the layout and view model using
     * view binding.
     */
    private fun setupViewModelBinding(){
        val viewModelFactory = SettingsViewModelFactory(
            ModuleService(moduleRepository), Services.settingsService)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SettingsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * Observes [SettingsViewModel.getUserSettings]. If this changes
     * the lambda will call [SettingsViewModel.setSettings] to update
     * the settings object in the view model.
     */
    private fun observeSettingsData(){
        viewModel.getUserSettings().observe(viewLifecycleOwner, { settingsList ->
            for (settings in settingsList){
                if(settings.userId == auth.uid){
                    viewModel.setSettings(settings)
                }
            }
        })
    }

    /**
     * Will setup an observer of the [SettingsViewModel.errorStringIntNumber].
     *
     * If this observer is triggered the lambda will call [MainActivity.showSnackBar]
     * to update the UI with the error message that will be retrieved fro [getResources].
     */
    private fun observeErrors(){
        viewModel.errorStringIntNumber.observe(viewLifecycleOwner, {
            (activity as? MainActivity)?.showSnackBar(resources.getString(it), true)
        })
    }
}