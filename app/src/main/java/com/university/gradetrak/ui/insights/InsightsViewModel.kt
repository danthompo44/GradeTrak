package com.university.gradetrak.ui.insights

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.utils.Calculator
import com.university.gradetrak.utils.InsightsCalculator
import com.university.gradetrak.utils.TAG

class InsightsViewModel (private val moduleService: ModuleService,
                         private val settingsService: SettingsService) : ViewModel() {
    val currentLevel5Progress = ObservableField<String>()
    val overallProgress = ObservableField<String>()

    fun getAllModules(): MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    fun calculate(){
        currentLevel5Progress.set("${Calculator.calculateCurrentLevel5Percentage(
                getAllModules().value!!, settingsService.getAll().value!!)}%")
    }
}