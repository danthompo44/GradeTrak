package com.university.gradetrak.ui.insights

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.utils.InsightsCalculator

class InsightsViewModel (private val moduleService: ModuleService,
                         private val settingsService: SettingsService) : ViewModel() {
    val currentLevel5Progress = ObservableField<String>()
    val overallLevel5Progress = ObservableField<String>()
    val currentLevel6Progress = ObservableField<String>()
    val overallLevel6Progress = ObservableField<String>()

    fun getAllModules(): MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    fun calculate(){
        currentLevel5Progress.set("${InsightsCalculator.calculateCurrentLevel5Percentage(
                getAllModules().value!!)}%")
        overallLevel5Progress.set("${InsightsCalculator.calculateOverallLevel5Percentage(
                getAllModules().value!!)}%")
        currentLevel6Progress.set("${InsightsCalculator.calculateCurrentLevel6Percentage(
                getAllModules().value!!)}%")
        overallLevel6Progress.set("${InsightsCalculator.calculateOverallLevel6Percentage(
                getAllModules().value!!)}%")
    }
}