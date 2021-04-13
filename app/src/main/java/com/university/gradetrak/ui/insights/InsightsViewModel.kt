package com.university.gradetrak.ui.insights

import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.utils.InsightsCalculator
import com.university.gradetrak.utils.TAG

class InsightsViewModel (private val moduleService: ModuleService,
                         private val settingsService: SettingsService) : ViewModel() {
    var lowestModuleRemovedVisibility = ObservableInt(ViewGroup.INVISIBLE)

    val currentLevel5Progress = ObservableField<String>()
    val overallLevel5Progress = ObservableField<String>()
    val currentLevel6Progress = ObservableField<String>()
    val overallLevel6Progress = ObservableField<String>()
    val weightedLevel5Progress = ObservableField<String>()
    val weightedLevel6Progress = ObservableField<String>()
    val overallProgress = ObservableField<String>()
    val overallProgressWithModuleRemoval = ObservableField<String>()
    val lowestModule = ObservableField<String>()

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
        if(percentages[6] < percentages[7]){
            setLowestModuleVisibility(true)
            overallProgressWithModuleRemoval.set(addPercentToString(percentages[7]))
        } else {
            setLowestModuleVisibility(false)
            overallProgress.set(addPercentToString(percentages[6]))
        }


        lowestModule.set(InsightsCalculator.getLowestModuleString())
    }

    private fun setLowestModuleVisibility(isVisible: Boolean){
        Log.v(TAG, isVisible.toString())
        if(isVisible){
            lowestModuleRemovedVisibility.set(ViewGroup.VISIBLE)
        } else {
            lowestModuleRemovedVisibility.set(ViewGroup.INVISIBLE)
        }
    }

    private fun addPercentToString(value: Double): String{
        return "$value%"
    }
}