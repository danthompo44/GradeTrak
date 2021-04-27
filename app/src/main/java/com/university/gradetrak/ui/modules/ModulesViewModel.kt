package com.university.gradetrak.ui.modules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.ModuleService

class ModulesViewModel (private val moduleService: ModuleService) : ViewModel() {
    var selectedModule = MutableLiveData<Module?>()
    var moduleForEdit = MutableLiveData<Module>()
    var error = MutableLiveData<Int>()

    /**
     * Will get all modules related to the current user.
     *
     * @return [ModuleService.getAll]
     */
    fun getUsersModules() : MutableLiveData<List<Module>>{
        return moduleService.getAll()
    }

    /**
     * Runs validation of errors using [moduleIsSelectedAndHandleErrors],
     * if true will check that [moduleExists]. Updates the module for edit Live
     * data that can be observed by [ModulesFragment].
     *
     * Finally, sets [selectedModule] to null.
     */
    fun handleEditClick(){
        if(moduleIsSelectedAndHandleErrors()){
            if(moduleExists()){
                moduleForEdit.value = selectedModule.value
                selectedModule.value = null
            }
        }
    }

    /**
     * Runs validation of errors using [moduleIsSelectedAndHandleErrors],
     * if true pass the [Module] to [ModuleService.delete].
     *
     * Finally, set [selectedModule] to null.
     */
    fun handleDeleteClick(){
        if(moduleIsSelectedAndHandleErrors()){
            val module = selectedModule.value
            moduleService.delete(module!!)
            selectedModule.value = null
        }
    }

    /**
     * Checks that a module is selected and if not will handle errors.
     *
     * @return true if a module is selected.
     * @return false if a module is not selected.
     * Sets [error] to a integer representation of the string
     * to be displayed by [ModulesFragment].
     */
    private fun moduleIsSelectedAndHandleErrors(): Boolean {
        return if(selectedModule.value != null){
            true
        }
        else{
            error.value = R.string.select_a_module
            false
        }
    }

    /**
     * Checks to see if a the [selectedModule]
     * exists in [getUsersModules].
     *
     * Sets [error] to an integer value representative
     * of a string value in resources.
     */
    private fun moduleExists() : Boolean{
        for(module in getUsersModules().value!!){
            if(selectedModule.value?.uuid == module.uuid){
                return true
            }
        }
        error.value = R.string.select_a_module
        return false
    }
}