package com.university.gradetrak.ui.settings

import android.util.Log
import android.widget.Button
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Settings
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.utils.TAG

class SettingsViewModel(private val settingsService: SettingsService) : ViewModel() {
    val thirtySeventyWeighting = ObservableBoolean()
    val removeLowestModule = ObservableBoolean()

    var applyChangesButtonVisibility = ObservableInt(Button.INVISIBLE)

    init {
        setSettings(getUserSettings().value)
    }

    fun setSettings(settings: Settings?){
        if(settings != null){
            thirtySeventyWeighting.set(settings.thirtySeventyRatio!!)
            removeLowestModule.set(settings.removeLowestModule!!)
        }
    }

    fun handleWeightingButtonPress(){
        updateApplyChangesButtonVisibility()
    }

    fun getUserSettings(): MutableLiveData<Settings>{
        return settingsService.getAll()
    }

    fun handleLowestModuleClick(){
        updateApplyChangesButtonVisibility()
    }

    private fun updateApplyChangesButtonVisibility(){
        val databaseSettings = getUserSettings().value

        if(databaseSettings?.removeLowestModule != removeLowestModule.get() ||
                databaseSettings.thirtySeventyRatio != thirtySeventyWeighting.get()){
            applyChangesButtonVisibility.set(Button.VISIBLE)
        }
        else{
            applyChangesButtonVisibility.set(Button.INVISIBLE)
        }
    }

    fun handleApplyChangesClick(){
        val settings = Settings(thirtySeventyWeighting.get(), removeLowestModule.get())
        settings.uuid = getUserSettings().value!!.uuid
        settingsService.editSettings(settings)
    }
}