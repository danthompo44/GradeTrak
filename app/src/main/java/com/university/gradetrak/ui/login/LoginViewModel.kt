package com.university.gradetrak.ui.login

import android.text.TextUtils
import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.gradetrak.R

class LoginViewModel: ViewModel(){
    val emailAddress = ObservableField<String>()
    val password = ObservableField<String>()

    val errorStringIntValue = MutableLiveData<Int>()

    /**
     * Validates the user login details, trims off extra spaces in the strings. Updates the
     * error Live Data in the view model and will hence trigger a UI update in the Login Activity
     * if an observer is set up on the error data
     * @return true if and error is found
     */
    fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(getEmail()) -> {
                errorStringIntValue.value = R.string.err_msg_enter_email_address
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches() -> {
                errorStringIntValue.value = R.string.err_msg_invalid_email_address
                false
            }

            //Check if password is empty, if it is null the string will say null
            getPassword().isEmpty() || getPassword() =="null" -> {
                errorStringIntValue.value = R.string.err_msg_enter_password
                false
            }

            else -> {
                true
            }
        }
    }

    fun getEmail() : String{
        return emailAddress.get().toString().trim()
    }

    fun getPassword() : String {
        return password.get().toString().trim()
    }

}