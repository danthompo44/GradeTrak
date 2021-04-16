package com.university.gradetrak.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.services.StudentService

class InsightsViewModelFactory(private val moduleService: ModuleService,
                               private val settingsService: SettingsService,
                               private val studentService: StudentService) : ViewModelProvider.Factory {
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
        if(modelClass.isAssignableFrom(InsightsViewModel::class.java)){
            return InsightsViewModel(moduleService, settingsService, studentService) as T
        }
        throw IllegalArgumentException("View model class does not exist")
    }
}