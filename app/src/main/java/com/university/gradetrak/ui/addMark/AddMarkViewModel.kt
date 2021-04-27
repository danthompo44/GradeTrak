package com.university.gradetrak.ui.addMark

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService

class AddMarkViewModel(private val moduleService: ModuleService): ViewModel() {
    var moduleName = ObservableField<String>()
    val moduleResult = ObservableField<String>()
    lateinit var selectedModule: Module
    val success = MutableLiveData(false)

    //Set up error Live Data for the Login Activity to observe and update the UI, snack bars etc.
    val errorStringIntValue = MutableLiveData<Int>()

    /**
     * Catches errors and updates the live data for observation
     * If no errors will create a module and pass it to the
     * module service for adding/editing
     */
    fun handleEditButtonClick(){
        var mark = -1

        try{
            mark = moduleResult.get().toString().trim().toInt()
        } catch (e: Exception){
            errorStringIntValue.value = R.string.mark_is_not_a_number
            return
        }

        if(mark < 0 || mark > 100){
            errorStringIntValue.value = R.string.mark_is_invalid
            return
        }

        val name = moduleName.get().toString().trim()
        val credits = selectedModule.credits
        val level = selectedModule.level
        val module = Module(name, credits, level, mark)
        module.uuid = selectedModule.uuid
        moduleService.editModule(module)

        success.value = true
        clearDetails()
    }

    /**
     * Clears the UI Observable fields
     */
    private fun clearDetails(){
        moduleResult.set("")
    }
}