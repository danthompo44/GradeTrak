package com.university.gradetrak.ui.addModule

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Credits
import com.university.gradetrak.models.Level
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService

class AddModuleViewModel (private val moduleService: ModuleService): ViewModel() {
    var moduleName = ObservableField<String>()
        private set
    var moduleCredits = ObservableField<String>()
    var moduleLevel = ObservableField<String>()

    //Set up success Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val success: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    //Set up error Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val error: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun handleAddClick(){
        val module = Module(moduleName.get().toString(), Credits.valueOf(moduleCredits.get()!!).value, Level.valueOf(moduleLevel.get()!!).value)
        error.value = module.toString()
        moduleService.addEditModule(module)
    }
}