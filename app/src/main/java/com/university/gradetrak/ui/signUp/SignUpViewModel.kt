package com.university.gradetrak.ui.signUp

import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R
import com.university.gradetrak.models.Student
import com.university.gradetrak.services.SettingsService
import com.university.gradetrak.services.StudentService
import com.university.gradetrak.utils.DefaultSettingsFactory

class SignUpViewModel (private val studentService: StudentService,
                       private val settingsService: SettingsService) : ViewModel() {

    val firstName = ObservableField<String>()
    val surname = ObservableField<String>()
    val emailAddress = ObservableField<String>()
    val password = ObservableField<String>()
    val confirmPassword = ObservableField<String>()

    val error = MutableLiveData<Int>()

    fun addStudent(currentUserId: String?){
        val nameString = firstName.get().toString().trim()
        val surnameString = surname.get().toString().trim()
        val student = Student(currentUserId, nameString, surnameString)

        studentService.addStudent(student)
//        Use a factory to reduce ViewModel Coupling to the Settings Class
        settingsService.addEditSettings(DefaultSettingsFactory.getSettings(currentUserId))
    }

    /**
     * A function that returns a false if the user has not entered required information.
     * Whilst also updating the error live data, the activity can observe this data and hence update
     * the UI with snackbars using that data
     */
    fun validateUserCredentials(): Boolean {
        val nameString = firstName.get().toString().trim()
        val surnameString = surname.get().toString().trim()
        val emailString = emailAddress.get().toString().trim()
        val passwordString = password.get().toString().trim()
        val confirmPasswordString = confirmPassword.get().toString().trim()

        return when {
            nameString.isEmpty() || nameString == "null" -> {
                error.value = R.string.err_msg_enter_first_name
                false
            }

            surnameString.isEmpty() || surnameString == "null" -> {
                error.value = R.string.err_msg_enter_surname
                false
            }

            emailString.isEmpty() || emailString == "null" ->  {
                error.value = R.string.err_msg_enter_email_address
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(emailString).matches() -> {
                error.value = R.string.err_msg_invalid_email_address
                false
            }

            passwordString.isEmpty() || passwordString == "null" -> {
                error.value = R.string.err_msg_enter_password
                false
            }

            confirmPasswordString.isEmpty() || confirmPasswordString == "null" ->  {
                error.value = R.string.err_msg_confirm_password
                false
            }

            passwordString != confirmPasswordString -> {
                error.value = R.string.err_msg_password_do_not_match
                false
            }
            else -> {
                true
            }
        }
    }
}