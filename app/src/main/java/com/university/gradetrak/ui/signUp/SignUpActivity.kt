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
import com.university.gradetrak.services.Services

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(Services.studentService, Services.settingsService)
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

    /**
     * Signs out a user using firebase [auth] sign out.
     */
    private fun reload(){
        auth.signOut()
    }

    /**
     * Sets the content view to [binding].root
     * Sets up data binding between the layout file and view model.
     */
    private fun setupBinding(){
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * Listens for clicks on the navigation arrow in the
     * layout files toolbar.
     *
     * If clicked the [SignUpActivity] will [finish].
     */
    private fun setupNavigationListener(){
        binding.tbEditModulePage.setNavigationOnClickListener {
            finish()
        }
    }

    /**
     * Creates a user if their credentials are valid.
     *
     * Uses [SignUpViewModel.validateUserCredentials], if true will [createUser].
     *
     * Else returns nothing.
     */
    fun handleSignUpButtonPress(view: View){
        if(viewModel.validateUserCredentials()){
            createUser()
        } else {
            return
        }
    }

    /**
     * Uses firebase [auth] to create a user using their email and password.
     *
     * If the task is successful the moth will [SignUpViewModel.addStudent] and
     * [showSnackBar] with a success message.
     *
     * Else a [showSnackBar] with an error message.
     */
    private fun createUser(){
        val email = viewModel.emailAddress.get().toString().trim()
        val password = viewModel.password.get().toString().trim()
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.uid
                        viewModel.addStudent(currentUser)
                        showSnackBar(resources.getString(R.string.sign_up_successful), false)
                        finish()
                    } else {
                        showSnackBar(task.exception?.localizedMessage.toString(), true)
                    }
                }
    }

    /**
     * Observes errors in [SignUpViewModel.error], if there is an
     * error is will [showSnackBar] using [getResources] to retrieve
     * the relevant string to display in the snack bar.
     */
    private fun observeErrors(){
        viewModel.error.observe(this, {
            showSnackBar(resources.getString(it), true)
        })
    }
}