package com.university.gradetrak.ui.insights

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.R
import com.university.gradetrak.databinding.FragmentInsightsBinding
import com.university.gradetrak.services.Services
import com.university.gradetrak.utils.TAG

class InsightsFragment : Fragment() {
    private lateinit var binding: FragmentInsightsBinding
    private lateinit var viewModel: InsightsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsightsBinding.inflate(inflater, container, false)

        setupViewModelBinding()
        observeDatabase()
        observeModulePromptTextIntegerValue()
        return binding.root
    }

    private fun setupViewModelBinding() {
        val viewModelFactory = InsightsViewModelFactory(Services.moduleService,
            Services.settingsService)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(InsightsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeDatabase(){
        viewModel.getAllModules().observe(viewLifecycleOwner, {
            viewModel.calculate()
        })
        viewModel.getSettings().observe(viewLifecycleOwner, {
            Log.v(TAG, "Get Settings Calculate")
            viewModel.calculate()
        })
    }

    private fun observeModulePromptTextIntegerValue(){
        viewModel.modulePromptStringIntegerValue.observe(viewLifecycleOwner, {
            Log.v(TAG, "integer value has changed to $it")
            binding.lowestModulePromptText.setText(it)
        })
    }
}