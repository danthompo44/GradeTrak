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
    val overallProgress = ObservableField<String>()

    fun getAllModules(): MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    fun calculate(){
        currentLevel5Progress.set("${InsightsCalculator.calculateCurrentLevel5Percentage(
                getAllModules().value!!, settingsService.getAll().value!!)}%")
        overallProgress.set("${InsightsCalculator.calculateOverallLevel5Percentage(
                getAllModules().value!!)}%")
    }
}