package com.university.gradetrak.ui.insights

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.university.gradetrak.repositories.ModuleRepository
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.Services
import com.university.gradetrak.utils.TAG

class InsightsFragment : Fragment() {
    private lateinit var binding: FragmentInsightsBinding
    private lateinit var viewModel: InsightsViewModel
    private val auth = Firebase.auth
    private val moduleRepository = ModuleRepository(auth.uid!!)

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

    /**
     * Creates the view model using a factory, sets up data binding between the view model
     * and layout file
     */
    private fun setupViewModelBinding() {
        val viewModelFactory = InsightsViewModelFactory(ModuleService(moduleRepository),
            Services.settingsService)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(InsightsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * A facade method that is in charge of calling the methods that will create
     * observer of the view model
     */
    private fun observeViewModel(){
        observeSettings()
        observeModulePromptTextIntegerValue()
        observerOverallGradeTextIntegerValue()
        observeLevelsComplete()
        observeShowInsights()

    }

    /**
     * Observes the [InsightsViewModel.getAllModules] and [InsightsViewModel.getSettings] methods.
     *
     * The functions [InsightsViewModel.getAllModules] and [InsightsViewModel.getSettings]
     * returns live data from firebase, this allows this function to become an
     * observer of that data.
     *
     * When a change is detected in [InsightsViewModel.getAllModules] or
     * [InsightsViewModel.getSettings] this method the corresponding method will
     * be called. This will call the [InsightsViewModel.refreshUI] method
     */
    private fun observeSettings(){
        Log.v(TAG, "In Get Settings Observer Method")
        viewModel.getSettings().observe(viewLifecycleOwner, {
            Log.v(TAG, "In Get Settings Observer")
            for(settings in it){
                if(settings.userId == auth.uid){
                    Log.v(TAG, "Settings Matches")
                    Log.v(TAG, settings.toString())
                    viewModel.setSettings(settings)
                    viewModel.refreshUI()
                }
            }
        })
    }

    /**
     * Observes the lowest module prompt text integer value in the view model.
     *
     * [InsightsViewModel.modulePromptStringIntegerValue] returns an integer,
     * this integer relates to a string resource within resources.
     *
     * When [InsightsViewModel.modulePromptStringIntegerValue] updates, the lambda
     * method is triggered, causing the activity to retrieve the required string,
     * pass it to the layout using view binding and display it to the user.
     */
    private fun observeModulePromptTextIntegerValue(){
        viewModel.modulePromptStringIntegerValue.observe(viewLifecycleOwner, {
            binding.lowestModulePromptText.setText(it)
        })
    }

    /**
     * Sets up and observer of [InsightsViewModel.overallGradeResourceId]
     *
     * Dependent on what grade the user has, in the form of an integer value
     * representative of a sting. The observer will change the background colour
     * of the Text view holing the grade in the layout file.
     *
     * This method will also update the text view in the layout file too, using
     * the integer passed to it from the view model to retrieve the string
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun observerOverallGradeTextIntegerValue(){
        viewModel.overallGradeResourceId.observe(viewLifecycleOwner, {
            when (it){
                R.string.fail -> {
                    binding.tvGrade.background =
                        resources.getDrawable(R.drawable.grade_colour_scheme_fail)
                }
                R.string.pass -> {
                    binding.tvGrade.background =
                        resources.getDrawable(R.drawable.grade_colour_scheme_pass)
                }
                R.string.third -> {
                    binding.tvGrade.background =
                        resources.getDrawable(R.drawable.grade_colour_scheme_third)
                }
                R.string.two_two -> {
                    binding.tvGrade.background =
                        resources.getDrawable(R.drawable.grade_colour_scheme_two_two)
                }
                R.string.two_one -> {
                    binding.tvGrade.background =
                        resources.getDrawable(R.drawable.grade_colour_scheme_two_one)
                }
                R.string.first -> {
                    binding.tvGrade.background =
                        resources.getDrawable(R.drawable.grade_colour_scheme_first)
                }
            }
            binding.tvGrade.setText(it)
        })
    }

    /**
     * Sets up an observer of [InsightsViewModel.level5Complete]
     * and [InsightsViewModel.level6Complete]
     *
     * If [InsightsViewModel.level5Complete] or [InsightsViewModel.level6Complete]
     * changes the corresponding lambda will trigger, calling [InsightsFragment.hideLayout]
     * and passing through a linear layout file to be hidden from the UI
     */
    private fun observeLevelsComplete(){
        viewModel.level5Complete.observe(viewLifecycleOwner, {
            hideLayout(binding.currentLevel5Container)
        })
        viewModel.level6Complete.observe(viewLifecycleOwner, {
            hideLayout(binding.currentLevel6Container)
        })
    }

    /**
     * Sets up an observer of the [InsightsViewModel.showInsights] boolean.
     *
     * If [InsightsViewModel.showInsights] is true the method will retrieve the users name using
     * [InsightsViewModel.getStudentName] and passing the id associated with [auth] to it.
     * This name will then be displayed in the UI.
     *
     * If [InsightsViewModel.showInsights] is false the UI will be updated to show "No Insights"
     */
    private fun observeShowInsights(){
        viewModel.showInsights.observe(viewLifecycleOwner, { showInsights ->
            if(showInsights){
                binding.noInsightsText.text = getString(R.string.users_insights)
            } else {
                binding.noInsightsText.text = getString(R.string.no_insights_text)
            }
        })
    }

    /**
     * Sets the margins and height of [layout] to 0.
     *
     * @param layout is a [LinearLayout]
     */
    private fun hideLayout(layout: LinearLayout){
        val param = layout.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,0,0)
        param.height = 0
    }
}