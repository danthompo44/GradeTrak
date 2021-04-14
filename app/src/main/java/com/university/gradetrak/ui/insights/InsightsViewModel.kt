package com.university.gradetrak.ui.insights

import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings
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

    fun getAllModules(): MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    fun getSettings(): MutableLiveData<Settings>{
        return settingsService.getAll()
    }

    fun refreshUI(){
        calculate()
        val settings = settingsService.getAll().value
        totalLevel5Credits.set(settings!!.level5Credits.toString())
        totalLevel5CreditsInt.set(settings.level5Credits)
        totalLevel6Credits.set(settings.level6Credits.toString())
        totalLevel6CreditsInt.set(settings.level6Credits)
        val receivedCredits = InsightsCalculator.getReceivedCredits()

        receivedLevel5Credits.set(receivedCredits[0])
        receivedLevel6Credits.set(receivedCredits[1])

        if(receivedCredits[0] >= settings.level5Credits!!){
            level5Complete.value = true
        }
        if(receivedCredits[1] >= settings.level6Credits!!){
            level6Complete.value = true
        }
    }

    private fun calculate(){
        val percentages = InsightsCalculator.calculatePercentages(
                getAllModules().value!!, getSettings().value!!)
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

    private fun addPercentToString(value: Double): String{
        return "$value%"
    }

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