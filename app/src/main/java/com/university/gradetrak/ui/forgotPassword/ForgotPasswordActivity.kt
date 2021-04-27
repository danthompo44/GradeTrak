package com.university.gradetrak.ui.forgotPassword

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.BaseActivity
import com.university.gradetrak.R
import com.university.gradetrak.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth : FirebaseAuth
    private val viewModel: ForgotPasswordViewModel by viewModels {
        ForgotPasswordViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        auth = Firebase.auth

        setupNavigationListener()
        observeErrors()
    }

    /**
     * Adds a listener to the toolbars nav button, finsihes the activity when clicked
     */
    private fun setupNavigationListener(){
        binding.tbForgotPasswordPage.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Calls the view model top validate the data, if successful sends a reset link
     * and finished the activity
     */
    fun handleSubmitButtonClick(view: View){
        if(viewModel.validateEmail()){
            sendFirebaseResetEmailLink()
        }
    }

    /**
     * Observes the error int live data for and errors with the data
     */
    private fun observeErrors(){
        viewModel.errorIntStringValue.observe(this, {
            showSnackBar(resources.getString(it), true)
        })
    }

    /**
     * Sends a firebase reset link and updates success and errors which activity should be observing
     */
    private fun sendFirebaseResetEmailLink() {
        auth.sendPasswordResetEmail(viewModel.getTrimmedEmail())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showSnackBar(resources.getString(R.string.reset_password_sent), false)
                        finish()
                    }
                    else{
                        showSnackBar(task.exception?.localizedMessage.toString(), true)
                    }
                }
    }
}