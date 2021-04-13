package com.university.gradetrak.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.MainActivity
import com.university.gradetrak.R
import com.university.gradetrak.databinding.FragmentSettingsBinding
import com.university.gradetrak.services.Services

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupViewModelBinding()
        observeSettingsData()

        return binding.root
    }

    private fun setupViewModelBinding(){
        val viewModelFactory = SettingsViewModelFactory(Services.settingsService)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeSettingsData(){
        viewModel.getUserSettings().observe(viewLifecycleOwner, { settings ->
            viewModel.setSettings(settings)
        })
    }
}