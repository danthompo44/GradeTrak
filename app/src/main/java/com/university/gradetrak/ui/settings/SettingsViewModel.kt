package com.university.gradetrak.ui.settings

import android.widget.Button
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.R
import com.university.gradetrak.models.Settings
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService

class SettingsViewModel(private val moduleService: ModuleService, private val settingsService: SettingsService) : ViewModel() {
    var auth = Firebase.auth
    val thirtySeventyWeighting = ObservableBoolean()
    val removeLowestModule = ObservableBoolean()
    val level5Credits = ObservableField<String>()
    private var level5CreditsComparison = ""
    val level6Credits = ObservableField<String>()
    private var level6CreditsComparison = ""

    var applyChangesButtonVisibility = ObservableInt(Button.INVISIBLE)

    val errorStringIntNumber = MutableLiveData<Int>()

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
            level5CreditsComparison = settings.level5Credits.toString()
            level6Credits.set(settings.level6Credits.toString())
            level6CreditsComparison = settings.level6Credits.toString()
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
                databaseSettings.thirtySeventyRatio != thirtySeventyWeighting.get() ||
                level5Credits.get().toString() != level5CreditsComparison ||
                level6Credits.get().toString() != level6CreditsComparison)
        {
                    applyChangesButtonVisibility.set(Button.VISIBLE)
                }
        else{
            applyChangesButtonVisibility.set(Button.INVISIBLE)
        }
    }


    fun handleApplyChangesClick(){
        var inputtedLevel5Credits: Int
        var inputtedLevel6Credits: Int

        try{
            inputtedLevel5Credits = level5Credits.get()?.toInt()!!
        } catch (e: Exception){
            errorStringIntNumber.value = R.string.level_5_is_not_a_number
            return
        }
        try{
            inputtedLevel6Credits = level6Credits.get()?.toInt()!!
        } catch (e: Exception){
            errorStringIntNumber.value = R.string.level_5_is_not_a_number
            return
        }
        if (getUsersCredits(5) > inputtedLevel5Credits){
            errorStringIntNumber.value = R.string.level_5_credits_too_low
            return
        }
        if (getUsersCredits(6) > inputtedLevel6Credits){
            errorStringIntNumber.value = R.string.level_6_credits_too_low
            return
        }

        val settings = Settings(thirtySeventyWeighting.get(), removeLowestModule.get(),
                level5Credits.get()?.toInt(), level6Credits.get()?.toInt())
        settings.userId = auth.uid
        settingsService.addEditSettings(settings)
        applyChangesButtonVisibility.set(Button.INVISIBLE)
    }

    private fun getUsersCredits(level: Int): Int{
        val modules = moduleService.getAll().value
        var credits = 0
        if (modules != null) {
            for (module in modules){
                if(module.level == level){
                    credits += module.credits!!
                }
            }
        }
        return credits
    }
}