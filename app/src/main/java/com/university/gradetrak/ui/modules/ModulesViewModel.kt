package com.university.gradetrak.ui.modules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService

class ModulesViewModel (private val moduleService: ModuleService) : ViewModel() {
    var selectedModule = MutableLiveData<Module?>()
    var moduleForEdit = MutableLiveData<Module>()
    var error = MutableLiveData<String>()

    fun getUsersModules() : MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    fun handleEditClick(){
        if(moduleIsSelectedAndHandleErrors()){
            if(moduleExists()){
                moduleForEdit.value = selectedModule.value
                selectedModule.value = null
            }
        }
    }

    fun handleDeleteClick(){
        if(moduleIsSelectedAndHandleErrors()){
            val module = selectedModule.value
            moduleService.delete(module!!)
            selectedModule.value = null
        }
    }

    private fun moduleIsSelectedAndHandleErrors(): Boolean {
        return if(selectedModule.value != null){
            true
        }
        else{
            error.value = "Please select a module"
            false
        }
    }

    private fun moduleExists() : Boolean{
        for(module in getUsersModules().value!!){
            if(selectedModule.value?.uuid == module.uuid){
                return true
            }
        }
        error.value = "Please select a module"
        return false
    }
}