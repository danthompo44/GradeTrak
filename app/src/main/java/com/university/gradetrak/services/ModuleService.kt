package com.university.gradetrak.services

import androidx.lifecycle.MutableLiveData
import com.university.gradetrak.models.Module
import com.university.gradetrak.repositories.ModuleRepository

class ModuleService (private val repository: ModuleRepository){
    fun getAll(): MutableLiveData<List<Module>> {
        return repository.modulesLD
    }

    fun addModule(module: Module){
        repository.addEditStudent(module)
    }

    fun delete(module: Module){
        repository.delete(module)
    }
}