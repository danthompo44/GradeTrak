package com.university.gradetrak.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.university.gradetrak.models.Module
import com.university.gradetrak.repositories.ModuleRepository
import com.university.gradetrak.utils.TAG

class ModuleService (private val repository: ModuleRepository){
    fun getAll(): MutableLiveData<List<Module>> {
        return repository.modulesLD
    }

    fun addModule(module: Module){
        repository.addStudent(module)
    }

    fun editModule(module: Module){
        repository.editStudent(module)
    }

    fun delete(module: Module){
        repository.delete(module)
    }
}