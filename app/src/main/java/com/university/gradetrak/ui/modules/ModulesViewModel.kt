package com.university.gradetrak.ui.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService

class ModulesViewModel (private val moduleService: ModuleService) : ViewModel() {
    var selectedModule = MutableLiveData<Module>()
    var moduleForEdit = MutableLiveData<Module>()
    var error = MutableLiveData<String>()

    fun getUsersModules() : MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    fun handleEditClick(){
        if(checkIfModuleIsSelectedAndHandleErrors()){
            moduleForEdit.value = selectedModule.value
        }
    }

    fun handleDeleteClick(){
        if(checkIfModuleIsSelectedAndHandleErrors()){
            moduleService.delete(selectedModule.value!!)
        }
    }

    private fun checkIfModuleIsSelectedAndHandleErrors(): Boolean {
        return if(selectedModule.value != null){
            true
        }
        else{
            error.value = "Please select a module"
            false
        }
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}