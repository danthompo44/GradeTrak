package com.university.gradetrak.ui.editModule

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Credits
import com.university.gradetrak.models.Level
import com.university.gradetrak.models.Module

class EditModuleViewModel: ViewModel() {
    val moduleName = ObservableField<String>()
    val moduleCredits = ObservableField<String>()
    val moduleLevel = ObservableField<String>()

    //Set up error Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val error: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun handleEditButtonClick(){
        val name = moduleName.get().toString().trim()
        val credits = Credits.valueOf(moduleCredits.get().toString()).value
        val level = Level.valueOf(moduleLevel.get().toString()).value
        val module = Module(name, credits, level)
        error.value = module.toString()
        clearDetails()
    }

    private fun clearDetails(){
        moduleName.set("")
    }
}