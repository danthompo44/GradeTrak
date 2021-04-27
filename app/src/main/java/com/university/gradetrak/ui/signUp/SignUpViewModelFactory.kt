package com.university.gradetrak.ui.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.services.StudentService

class SignUpViewModelFactory(private val studentService: StudentService,
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
        if(modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            return SignUpViewModel(studentService, settingsService) as T
        }
        throw IllegalArgumentException("Not a recognised view model")
    }
}