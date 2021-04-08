package com.university.gradetrak.ui.modules

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.utils.TAG

class ModulesViewModel (private val moduleService: ModuleService) : ViewModel() {
    var selectedModule = MutableLiveData<Module>()
    var moduleForEdit = MutableLiveData<Module>()
    var error = MutableLiveData<String>()

    fun getUsersModules() : MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    fun handleEditClick(){
        if(selectedModule.value != null){
            moduleForEdit.value = selectedModule.value
        }
        else{
            error.value = "Please select a module"
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}