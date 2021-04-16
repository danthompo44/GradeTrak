package com.university.gradetrak.ui.insights

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.R
import com.university.gradetrak.databinding.FragmentInsightsBinding
import com.university.gradetrak.services.Services

class InsightsFragment : Fragment() {
    private lateinit var binding: FragmentInsightsBinding
    private lateinit var viewModel: InsightsViewModel
    private val auth = Firebase.auth

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
        val viewModelFactory = InsightsViewModelFactory(Services.getModuleService(auth.uid!!),
            Services.settingsService, Services.studentService)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(InsightsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeViewModel(){
        observeDatabase()
        observeModulePromptTextIntegerValue()
        observerOverallGradeTextIntegerValue()
        observeLevelsComplete()
        observeShowInsights()
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

    private fun observeLevelsComplete(){
        viewModel.level5Complete.observe(viewLifecycleOwner, {
            hideLayout(binding.currentLevel5Container)
        })
        viewModel.level6Complete.observe(viewLifecycleOwner, {
            hideLayout(binding.currentLevel6Container)
        })
    }

    private fun observeShowInsights(){
        viewModel.showInsights.observe(viewLifecycleOwner, { showInsights ->
            if(showInsights){
                binding.noInsightsText.text = createUserString(viewModel.getStudentName(auth.uid!!))
            } else {
                binding.noInsightsText.text = getString(R.string.no_insights_text)
            }
        })
    }

    private fun createUserString(name: String): String{
        return name + getString(R.string.users_insights)
    }

    private fun hideLayout(layout: LinearLayout){
        val param = layout.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,0,0)
        param.height = 0
    }
}