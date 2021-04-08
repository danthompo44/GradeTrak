package com.university.gradetrak.ui.editModule

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService

class EditModuleViewModel(private val moduleService: ModuleService): ViewModel() {
    var moduleName = ObservableField<String>()
    val moduleResult = ObservableField<String>()
    lateinit var selectedModule: Module
    val success = MutableLiveData(false)

    //Set up error Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val error: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun handleEditButtonClick(){
        val name = moduleName.get().toString().trim()
        val credits = selectedModule.credits
        val level = selectedModule.level
        val result = moduleResult.get().toString().trim().toInt()
        val module = Module(name, credits, level, result)
        module.uuid = selectedModule.uuid
        moduleService.editModule(module)

        error.value = module.toString()
        success.value = true
        clearDetails()
    }

    private fun clearDetails(){
        moduleResult.set("")
    }
}