package com.university.gradetrak.ui.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService

class ModulesViewModel (private val moduleService: ModuleService) : ViewModel() {

    var selectedModule = MutableLiveData<Module>()

    fun getUsersModules() : MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}