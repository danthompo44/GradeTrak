package com.university.gradetrak.ui.enrol

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService

class EnrolViewModel (private val moduleService: ModuleService): ViewModel() {


    fun getAllUsersModules() : LiveData<List<Module>> {
        return moduleService.getAll()
    }
}