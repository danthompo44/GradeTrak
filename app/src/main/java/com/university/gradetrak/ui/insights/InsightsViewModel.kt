package com.university.gradetrak.ui.insights

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.utils.InsightsCalculator
import com.university.gradetrak.utils.TAG

class InsightsViewModel (private val moduleService: ModuleService,
                         private val settingsService: SettingsService) : ViewModel() {
    var currentLevel5Progress = ObservableField<String>()

    private var calculator = InsightsCalculator(moduleService.getAll(), settingsService.getAll())

    fun setCurrentLevel5Progress(){
        currentLevel5Progress.set(calculator.getLevel5CurrentProgress().toString())
    }

    init {
        Log.v(TAG, calculator.getLevel5CurrentProgress().toString())
    }
}