package com.university.gradetrak.ui.addModule

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R
import com.university.gradetrak.models.Credits
import com.university.gradetrak.models.Level
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService

class AddModuleViewModel (private val moduleService: ModuleService,
                          private val settingsService: SettingsService): ViewModel() {
    var moduleName = ObservableField<String>()
        private set
    var moduleCredits = ObservableField<String>()
    var moduleLevel = ObservableField<String>()
    private lateinit var userSettings: Settings

    //Set up success Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val success = MutableLiveData(false)

    //Set up error Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val errorStringInt = MutableLiveData<Int>()

    /**
     * Retrieves a user settings from [SettingsService.getAll]
     */
    fun getUserSettings(): MutableLiveData<List<Settings>>{
        return settingsService.getAll()
    }

    /**
     * Called by the activity, sets settings in the view model
     */
    fun setSettings(settings: Settings){
        userSettings = settings
    }

    /**
     * Checks for errors and adds the module to the database
     */
    fun handleAddClick(){
        if(validateData()){
            val inputtedCredits = Credits.valueOf(moduleCredits.get()!!).value

            if(Level.valueOf(moduleLevel.get()!!).value == 5){
                val level5TotalAllowedCredits = userSettings.level5Credits
                if(getUsersCredits(5) + inputtedCredits > level5TotalAllowedCredits!!){
                    errorStringInt.value = R.string.error_too_many_level_5_modules
                    return
                }
            }
            if(Level.valueOf(moduleLevel.get()!!).value == 6){
                val level6TotalAllowedCredits = userSettings.level6Credits
                if(getUsersCredits(6) + inputtedCredits > level6TotalAllowedCredits!!){
                    errorStringInt.value = R.string.error_too_many_level_6_modules
                    return
                }
            }

            val module = Module(moduleName.get().toString(), Credits.valueOf(moduleCredits.get()!!)
                .value, Level.valueOf(moduleLevel.get()!!).value)

            moduleService.addModule(module)

            success.value = true
        }
    }

    /**
     * Gets the total amount of credits a user is register for
     * @param The level of the module that will be checked against
     * @return Total number of credits at the level
     */
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

    /**
     * Validate that the user inputs are not empty
     * @return true if data is valid
     */
    private fun validateData(): Boolean{
        val nameString = moduleName.get().toString().trim()
        val credits = moduleCredits.get().toString().trim()
        val level = moduleLevel.get().toString().trim()

        return when {
            nameString.isEmpty() || nameString == "null" -> {
                errorStringInt.value = R.string.no_module_name_error
                false
            }
            credits.isEmpty() || credits == "null" -> {
                errorStringInt.value = R.string.no_credits_error
                false
            }
            level.isEmpty() || level == "null" -> {
                errorStringInt.value = R.string.no_level_error
                false
            }
            else -> true
        }
    }
}