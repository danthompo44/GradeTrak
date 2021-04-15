package com.university.gradetrak.ui.signUp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.BaseActivity
import com.university.gradetrak.R
import com.university.gradetrak.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory()
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        auth = Firebase.auth

        setupNavigationListener()
        observeErrors()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun reload(){
        auth.signOut()
    }

    private fun setupBinding(){
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupNavigationListener(){
        binding.tbEditModulePage.setNavigationOnClickListener {
            finish()
        }
    }

    fun handleSignUpButtonPress(view: View){
        if(viewModel.validateUserCredentials()){
            showSnackBar("Successful Log In", false)
            createUser()
        } else {
            return
        }
    }

    private fun createUser(){
        val email = viewModel.emailAddress.get().toString().trim()
        val password = viewModel.password.get().toString().trim()
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        showSnackBar(resources.getString(R.string.sign_up_successful), false)
                        finish()
                    } else {
                        showSnackBar(task.exception?.localizedMessage.toString(), true)
                    }
                }
    }

    private fun observeErrors(){
        viewModel.error.observe(this, {
            showSnackBar(resources.getString(it), true)
        })
    }
}