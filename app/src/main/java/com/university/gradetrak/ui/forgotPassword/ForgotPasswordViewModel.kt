package com.university.gradetrak.ui.forgotPassword

import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R

class ForgotPasswordViewModel: ViewModel() {
    val emailAddress = ObservableField<String>()

    val errorIntStringValue = MutableLiveData<Int>()

    /**
     * Validates the forgot password email details, trims off extra spaces in the string
     * @return true if an error is found
     */
    fun validateEmail(): Boolean {
        return if(Patterns.EMAIL_ADDRESS.matcher(emailAddress.get().toString().trim()).matches()){
            true
        } else {
            errorIntStringValue.value = R.string.err_msg_invalid_email_address
            false
        }
    }

    /**
     * Trims the email observable field, removes blank space at the end/beginning
     */
    fun getTrimmedEmail(): String{
        return emailAddress.get().toString().trim()
    }
}