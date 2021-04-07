package com.university.gradetrak.ui.enrol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.services.ModuleService

class EnrolViewModelFactory(private val service: ModuleService) : ViewModelProvider.Factory {
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
        if(modelClass.isAssignableFrom(EnrolViewModel::class.java)){
            return EnrolViewModel(service) as T
        }
        throw IllegalArgumentException("View model is not recognised")
    }
}