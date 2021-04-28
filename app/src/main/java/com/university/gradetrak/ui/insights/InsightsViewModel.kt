package com.university.gradetrak.ui.insights

import android.util.Log
import android.widget.LinearLayout
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings
import com.university.gradetrak.models.Student
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.utils.InsightsCalculator
import com.university.gradetrak.utils.TAG

class InsightsViewModel (private val moduleService: ModuleService,
                         private val settingsService: SettingsService) : ViewModel() {

    val modulePromptStringIntegerValue = MutableLiveData<Int>()
    val totalLevel5Credits = ObservableField<String>()
    val totalLevel5CreditsInt = ObservableField<Int>()
    val receivedLevel5Credits = ObservableField<Int>()
    val totalLevel6Credits = ObservableField<String>()
    val totalLevel6CreditsInt = ObservableField<Int>()
    val receivedLevel6Credits = ObservableField<Int>()

    val currentLevel5Progress = ObservableField<String>()
    val overallLevel5Progress = ObservableField<String>()
    val currentLevel6Progress = ObservableField<String>()
    val overallLevel6Progress = ObservableField<String>()
    val weightedLevel5Progress = ObservableField<String>()
    val weightedLevel6Progress = ObservableField<String>()
    val overallProgress = ObservableField<String>()
    val lowestModule = ObservableField<String>()

    var overallProgressDouble = 0.0
    var overallGradeResourceId = MutableLiveData<Int>()

    val level5Complete = MutableLiveData<Boolean>()
    val level6Complete = MutableLiveData<Boolean>()

    val displayInsights = ObservableInt(LinearLayout.VISIBLE)
    val showInsights = MutableLiveData<Boolean>()
    private lateinit var userSettings: Settings
    private lateinit var currentStudent: Student

    /**
     * Retrieves all modules from [ModuleService.getAll].
     *
     * @return A list of modules that can be observed.
     */
    fun getAllModules(): MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    /**
     * Retrieves a users settings from [SettingsService.getAll].
     *
     * @return A settings object that can be observed.
     */
    fun getSettings(): MutableLiveData<List<Settings>>{
        return settingsService.getAll()
    }

    fun setSettings(settings : Settings){
        userSettings = settings
        Log.v(TAG, "Settings Have Been Initialised")
    }

    /**
     * Updates Observable fields using data retrieved from [SettingsService.getAll]
     *
     * Stores Strings of information received to be displayed in the UI. Stores integers
     * of these settings for business logic to be applied to.
     * If [hasMarks] returns true calls [calculate] which will retrieve insights from
     * the [InsightsCalculator]. Also retrieves received credits from
     * [InsightsCalculator.getReceivedCredits] and sets these in Observable Fields to be
     * displayed in the UI.
     *
     * Updates [level5Complete] and [level6Complete] booleans which can be observed in the activity.
     */
    fun refreshUI(){
        totalLevel5Credits.set(userSettings.level5Credits.toString())
        totalLevel5CreditsInt.set(userSettings.level5Credits)
        totalLevel6Credits.set(userSettings.level6Credits.toString())
        totalLevel6CreditsInt.set(userSettings.level6Credits)

        if(hasMarks()){
            calculate()
            val receivedCredits = InsightsCalculator.getReceivedCredits()

            receivedLevel5Credits.set(receivedCredits[0])
            receivedLevel6Credits.set(receivedCredits[1])

            if(receivedCredits[0] >= userSettings.level5Credits!!){
                level5Complete.value = true
            }
            if(receivedCredits[1] >= userSettings.level6Credits!!){
                level6Complete.value = true
            }
        }
    }

    /**
     * Deduces if a user has registered marks in any modules.
     * If any [Module] in [getAllModules] has a [Module.result]
     * [displayInsights] will be set to visible, whilst [showInsights] will
     * be set to true.
     *
     * Else, [displayInsights] is invisible and [showInsights] is false.
     */
    private fun hasMarks(): Boolean{
        for(module in getAllModules().value!!){
            if(module.result != null){
                displayInsights.set(LinearLayout.VISIBLE)
                showInsights.value =  true
                return true
            }
        }
        displayInsights.set(LinearLayout.INVISIBLE)
        showInsights.value =  false
        return false
    }

    /**
     * Updates the UI appropriately given the data it receives from
     * [InsightsCalculator.calculatePercentages] and [InsightsCalculator.getLowestModuleString]
     *
     * Will call [runGradeAssignment] so that it can set the users grade and communicate with the
     * fragment.
     */
    private fun calculate(){
        val percentages = InsightsCalculator.calculatePercentages(
                getAllModules().value!!, userSettings)
        currentLevel5Progress.set(addPercentToString(percentages[0]))
        overallLevel5Progress.set(addPercentToString(percentages[1]))
        weightedLevel5Progress.set(addPercentToString(percentages[2]))
        currentLevel6Progress.set(addPercentToString(percentages[3]))
        overallLevel6Progress.set(addPercentToString(percentages[4]))
        weightedLevel6Progress.set(addPercentToString(percentages[5]))
        if(percentages[6] < percentages[7]){
            modulePromptStringIntegerValue.value = R.string.overall_progress_removal
            overallProgress.set(addPercentToString(percentages[7]))
            overallProgressDouble = percentages[7]
        } else {
            modulePromptStringIntegerValue.value = R.string.overall_progress
            overallProgress.set(addPercentToString(percentages[6]))
            overallProgressDouble = percentages[6]
        }
        lowestModule.set(InsightsCalculator.getLowestModuleString())
        runGradeAssignment()
    }

    /**
     * Adds a percentage sign onto the end of [value].
     */
    private fun addPercentToString(value: Double): String{
        return "$value%"
    }

    /**
     * Updates the [overallGradeResourceId] dependent on what [overallProgressDouble] is.
     *
     * [overallGradeResourceId] can be observed in the fragment to update
     * the UI and feedback to the user.
     */
    private fun runGradeAssignment(){
        if(overallProgressDouble < 35){
            overallGradeResourceId.value = R.string.fail
        }
        else if (overallProgressDouble >=35 && overallProgressDouble <40){
            overallGradeResourceId.value = R.string.pass
        }
        else if(overallProgressDouble >=40 && overallProgressDouble <49){
            overallGradeResourceId.value = R.string.third
        }
        else if(overallProgressDouble >=50 && overallProgressDouble <59){
            overallGradeResourceId.value = R.string.two_two
        }
        else if(overallProgressDouble >=60 && overallProgressDouble <69){
            overallGradeResourceId.value = R.string.two_one
        }
        else if(overallProgressDouble >=70) {
            overallGradeResourceId.value = R.string.first
        }
    }
}