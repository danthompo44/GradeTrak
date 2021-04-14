package com.university.gradetrak.ui.addModule

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R
import com.university.gradetrak.models.Credits
import com.university.gradetrak.models.Level
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService

class AddModuleViewModel (private val moduleService: ModuleService,
                          private val settingsService: SettingsService): ViewModel() {
    var moduleName = ObservableField<String>()
        private set
    var moduleCredits = ObservableField<String>()
    var moduleLevel = ObservableField<String>()

    //Set up success Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val success = MutableLiveData(false)

    //Set up error Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val errorStringInt = MutableLiveData<Int>()

    fun handleAddClick(){
        val inputtedCredits = Credits.valueOf(moduleCredits.get()!!).value

        if(Level.valueOf(moduleLevel.get()!!).value == 5){
            val level5TotalAllowedCredits = settingsService.getAll().value!!.level5Credits
            if(getUsersCredits(5) + inputtedCredits > level5TotalAllowedCredits!!){
                errorStringInt.value = R.string.error_too_many_level_5_modules
                return
            }
        }
        if(Level.valueOf(moduleLevel.get()!!).value == 6){
            val level6TotalAllowedCredits = settingsService.getAll().value!!.level6Credits
            if(getUsersCredits(6) + inputtedCredits > level6TotalAllowedCredits!!){
                errorStringInt.value = R.string.error_too_many_level_6_modules
                return
            }
        }

        val module = Module(moduleName.get().toString(), Credits.valueOf(moduleCredits.get()!!).value, Level.valueOf(moduleLevel.get()!!).value)

        moduleService.addModule(module)

        success.value = true
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