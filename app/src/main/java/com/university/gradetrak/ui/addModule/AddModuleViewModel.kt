package com.university.gradetrak.ui.addModule

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.models.Credits
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.utils.TAG

class AddModuleViewModel (private val moduleService: ModuleService): ViewModel() {
    var moduleName = ObservableField<String>()
        private set
    var moduleCredits = ObservableField<String>()
    var moduleLevel = ObservableField<String>()

    //Set up error Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val error: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun handleAddClick(){
        Log.v(TAG, "Handle Add Click")
        val module = Module(moduleName.get().toString(), Credits.valueOf(moduleCredits.get()!!).value, moduleLevel.get().toString())
        error.value = module.toString()
    }
}