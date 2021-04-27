package com.university.gradetrak.ui.addModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService

class AddModuleViewModelFactory (private val moduleService: ModuleService,
                                 private val settingsService: SettingsService)
    : ViewModelProvider.Factory {
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
        if(modelClass.isAssignableFrom(AddModuleViewModel::class.java)){
            return AddModuleViewModel(moduleService, settingsService) as T
        }
        throw IllegalArgumentException("View Model does not Exist")
    }
}