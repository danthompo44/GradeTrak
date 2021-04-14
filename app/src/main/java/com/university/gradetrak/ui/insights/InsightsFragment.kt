package com.university.gradetrak.ui.insights

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        observeViewModel()
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

    private fun observeViewModel(){
        observeDatabase()
        observeModulePromptTextIntegerValue()
        observerOverallGradeTextIntegerValue()
    }

    private fun observeDatabase(){
        viewModel.getAllModules().observe(viewLifecycleOwner, {
            viewModel.refreshUI()
        })
        viewModel.getSettings().observe(viewLifecycleOwner, {
            viewModel.refreshUI()
        })
    }

    private fun observeModulePromptTextIntegerValue(){
        viewModel.modulePromptStringIntegerValue.observe(viewLifecycleOwner, {
            binding.lowestModulePromptText.setText(it)
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun observerOverallGradeTextIntegerValue(){
        viewModel.overallGradeResourceId.observe(viewLifecycleOwner, {
            when (it){
                R.string.fail -> {
                    binding.tvGrade.background = resources.getDrawable(R.drawable.grade_colour_scheme_fail)
                }
                R.string.pass -> {
                    binding.tvGrade.background = resources.getDrawable(R.drawable.grade_colour_scheme_pass)
                }
                R.string.third -> {
                    binding.tvGrade.background = resources.getDrawable(R.drawable.grade_colour_scheme_third)
                }
                R.string.two_two -> {
                    binding.tvGrade.background = resources.getDrawable(R.drawable.grade_colour_scheme_two_two)
                }
                R.string.two_one -> {
                    binding.tvGrade.background = resources.getDrawable(R.drawable.grade_colour_scheme_two_one)
                }
                R.string.first -> {
                    binding.tvGrade.background = resources.getDrawable(R.drawable.grade_colour_scheme_first)
                }
            }
            binding.tvGrade.setText(it)
        })
    }
}