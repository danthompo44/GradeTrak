package com.university.gradetrak.ui.editModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.services.ModuleService

class EditModuleViewModelFactory(private val moduleService: ModuleService) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given `Class`.
     *
     *
     *
     * @param modelClass a `Class` whose instance is requested
     * @param <T>        The type parameter for the ViewModel.
     * @return a newly created ViewModel
    </T> */
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditModuleViewModel::class.java)){
            return EditModuleViewModel(moduleService) as T
        }
        throw IllegalArgumentException("View model does not exist")
    }
}