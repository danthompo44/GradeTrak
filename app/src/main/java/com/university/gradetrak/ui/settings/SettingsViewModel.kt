package com.university.gradetrak.ui.settings

import android.util.Log
import android.widget.Button
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
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
    val level5Credits = ObservableField<String>()
    val level6Credits = ObservableField<String>()

    var applyChangesButtonVisibility = ObservableInt(Button.INVISIBLE)

    private val callback = object : Observable.OnPropertyChangedCallback(){
        /**
         * Called by an Observable whenever an observable property changes.
         * @param sender The Observable that is changing.
         * @param propertyId The BR identifier of the property that has changed. The getter
         * for this property should be annotated with [Bindable].
         */
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            updateApplyChangesButtonVisibility()
        }
    }

    init {
        setSettings(getUserSettings().value)
        level5Credits.addOnPropertyChangedCallback(callback)
        level6Credits.addOnPropertyChangedCallback(callback)
    }

    fun setSettings(settings: Settings?){
        if(settings != null){
            thirtySeventyWeighting.set(settings.thirtySeventyRatio!!)
            removeLowestModule.set(settings.removeLowestModule!!)
            level5Credits.set(settings.level5Credits.toString())
            level6Credits.set(settings.level6Credits.toString())
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

        val level5 = if(level5Credits.get() == ""){
            0
        } else {
                level5Credits.get()?.toInt()
        }

        val level6 = if(level6Credits.get() == ""){
            0
        } else {
            level6Credits.get()?.toInt()
        }


        if(databaseSettings?.removeLowestModule != removeLowestModule.get() ||
                databaseSettings.thirtySeventyRatio != thirtySeventyWeighting.get() ||
                databaseSettings.level5Credits != level5 ||
                databaseSettings.level6Credits != level6)
        {
                    applyChangesButtonVisibility.set(Button.VISIBLE)
                }
        else{
            applyChangesButtonVisibility.set(Button.INVISIBLE)
        }
    }


    fun handleApplyChangesClick(){
        val settings = Settings(thirtySeventyWeighting.get(), removeLowestModule.get(),
                level5Credits.get()?.toInt(), level6Credits.get()?.toInt())
        settings.uuid = getUserSettings().value!!.uuid
        settingsService.editSettings(settings)
        applyChangesButtonVisibility.set(Button.INVISIBLE)
    }
}