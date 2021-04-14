package com.university.gradetrak.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService

class SettingsViewModelFactory(private val moduleService: ModuleService,
                               private val settingsService: SettingsService)
    : ViewModelProvider.Factory{
    /**
     * Creates a new instance of the given `Class`.
     *
     *
     *
     * @param modelClass a `Class` whose instance is requested
     * @param <T>        The type parameter for the ViewModel.
     * @return a newly created ViewModel
    </T> */
    @Suppress("Unchecked_Cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            return SettingsViewModel(moduleService, settingsService) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}