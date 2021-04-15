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

    private fun setupNavigationListener(){
        binding.tbForgotPasswordPage.setNavigationOnClickListener {
            finish()
        }
    }

    fun handleSubmitButtonClick(view: View){
        if(viewModel.validateEmail()){
            sendFirebaseResetEmailLink()
            finish()
        }
    }

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
                    }
                    else{
                        showSnackBar(task.exception?.localizedMessage.toString(), true)
                    }
                }
    }
}