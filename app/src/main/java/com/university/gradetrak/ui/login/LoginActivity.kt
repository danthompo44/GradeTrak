package com.university.gradetrak.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.BaseActivity
import com.university.gradetrak.MainActivity
import com.university.gradetrak.databinding.ActivityLoginBinding
import com.university.gradetrak.ui.forgotPassword.ForgotPasswordActivity
import com.university.gradetrak.ui.signUp.SignUpActivity


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        auth = Firebase.auth

        observeErrors()
    }

    /**
     * Will delegate the validation of user credentials to [LoginViewModel.validateLoginDetails],
     * if true it will call [attemptSignIn].
     */
    fun handleLoginButtonClick(view: View){
        if(viewModel.validateLoginDetails()){
            attemptSignIn()
        }
    }

    /**
     * Uses an email and password to try to sign in a user using firebase [auth]
     *
     * If the task is successful the [MainActivity] will start and finish the [LoginActivity].
     * If an error occurs a snack bar will be display to the user using [BaseActivity.showSnackBar].
     */
    private fun attemptSignIn(){
        auth.signInWithEmailAndPassword(viewModel.getEmail(), viewModel.getPassword())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        showSnackBar(task.exception?.localizedMessage.toString(), true)
                    }
                }
    }

    /**
     * Will start the [SignUpActivity], does not finish [LoginActivity].
     */
    fun handleSignUpButtonClick(view: View){
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    /**
     * Will start the [ForgotPasswordActivity], does not finish [LoginActivity].
     */
    fun handleForgottenPasswordButtonClick(view: View){
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    /**
     * Observes errors in the view model
     *
     * Observes [LoginViewModel.errorStringIntValue] for changes, if a change
     * occurs the lambda will retrieve a string resource using the integer that
     * it is observing.
     */
    private fun observeErrors(){
        viewModel.errorStringIntValue.observe(this, {
            showSnackBar(resources.getString(it), isError = true)
        })
    }
}