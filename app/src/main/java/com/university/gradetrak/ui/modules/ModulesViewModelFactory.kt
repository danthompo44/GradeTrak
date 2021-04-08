package com.university.gradetrak.ui.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.services.ModuleService

class ModulesViewModelFactory(private val service: ModuleService) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given `Class`.
     *
     *
     *
     * @param modelClass a `Class` whose instance is requested
     * @param <T>        The type parameter for the ViewModel.
     * @return a newly created ViewModel
    </T> */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ModulesViewModel::class.java)){
            return ModulesViewModel(service) as T
        }
        else {
            throw IllegalArgumentException("Model Class does not exist")
        }
    }
}