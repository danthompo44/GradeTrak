package com.university.gradetrak.ui.insights

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.utils.InsightsCalculator

class InsightsViewModel (private val moduleService: ModuleService,
                         private val settingsService: SettingsService) : ViewModel() {
    val currentLevel5Progress = ObservableField<String>()
    val overallLevel5Progress = ObservableField<String>()
    val currentLevel6Progress = ObservableField<String>()
    val overallLevel6Progress = ObservableField<String>()
    val weightedLevel5Progress = ObservableField<String>()
    val weightedLevel6Progress = ObservableField<String>()
    val overallProgress = ObservableField<String>()
    val overallProgressWithModuleRemoval = ObservableField<String>()

    fun getAllModules(): MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    fun getSettings(): MutableLiveData<Settings>{
        return settingsService.getAll()
    }

    fun calculate(){
        val percentages = InsightsCalculator.calculatePercentages(
                getAllModules().value!!, getSettings().value!!)
        currentLevel5Progress.set(addPercentToString(percentages[0]))
        overallLevel5Progress.set(addPercentToString(percentages[1]))
        weightedLevel5Progress.set(addPercentToString(percentages[2]))
        currentLevel6Progress.set(addPercentToString(percentages[3]))
        overallLevel6Progress.set(addPercentToString(percentages[4]))
        weightedLevel6Progress.set(addPercentToString(percentages[5]))
        overallProgress.set(addPercentToString(percentages[6]))
    }

    private fun addPercentToString(value: Double): String{
        return "$value%"
    }
}