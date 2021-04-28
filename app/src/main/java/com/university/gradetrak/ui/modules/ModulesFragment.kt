package com.university.gradetrak.ui.modules

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.university.gradetrak.MainActivity
import com.university.gradetrak.databinding.FragmentModulesBinding
import com.university.gradetrak.repositories.ModuleRepository
import com.university.gradetrak.services.ModuleService
import com.university.gradetrak.services.Services
import com.university.gradetrak.ui.adapters.ModuleRecyclerAdapter
import com.university.gradetrak.ui.addMark.AddMarkActivity
import com.university.gradetrak.ui.addModule.AddModuleActivity
import com.university.gradetrak.utils.SELECTED_MODULE_ID_KEY
import com.university.gradetrak.utils.SELECTED_MODULE_KEY

class ModulesFragment : Fragment() {
    private lateinit var binding: FragmentModulesBinding
    private lateinit var viewModel: ModulesViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val auth = Firebase.auth
    private val moduleRepository = ModuleRepository(auth.uid!!)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModulesBinding.inflate(inflater, container, false)

        setupViewModelBinding()
        setupRecyclerViews()
        setupNavigationListener()
        setupViewModelObservers()
        return binding.root
    }

    /**
     * Uses [ModulesViewModelFactory] to create [ModulesViewModel],
     * passes in [Services.getModuleService] to the factory. Will create
     * databinding between the layout file using view binding [binding].
     */
    private fun setupViewModelBinding(){
        val viewModelFactory = ModulesViewModelFactory(ModuleService(moduleRepository))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ModulesViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * Sets up the layout manager of the recycler view using view binding.
     */
    private fun setupRecyclerViews(){
        linearLayoutManager = LinearLayoutManager(activity)
        binding.rvModulesPageModules.layoutManager = linearLayoutManager
    }

    /**
     * Adds a listener the add module plus sign in the layout file.
     * Will start the [AddModuleActivity] when pressed, does not finish
     * [MainActivity].
     */
    private fun setupNavigationListener(){
        binding.ivAddModuleToolbarAddModule.setOnClickListener {
            startActivity(Intent(activity, AddModuleActivity::class.java))
        }
    }

    /**
     * A facade method that calls [observeUsersModules],
     * [observeErrors] and [observeModuleForEdit].
     */
    private fun setupViewModelObservers(){
        observeUsersModules()
        observeModuleForEdit()
        observeErrors()
    }

    /**
     * Observes [ModulesViewModel.getUsersModules]. If there is a change a new adapter
     * is created using [ModuleRecyclerAdapter]. the result of [ModulesViewModel.getUsersModules],
     * [ModulesViewModel.selectedModule] and [getResources]
     * will be passed to [ModuleRecyclerAdapter].
     *
     * This object of [ModuleRecyclerAdapter] will set as the adapter of the
     * recycler view in the layout file using view binding.
     */
    private fun observeUsersModules(){
        viewModel.getUsersModules().observe(viewLifecycleOwner, { modules ->
            val adapter = ModuleRecyclerAdapter(modules, viewModel.selectedModule,
                resources)
            binding.rvModulesPageModules.adapter = adapter
            binding.rvModulesPageModules.setHasFixedSize(true)
        })
    }

    /**
     * Sets up an observer on [ModulesViewModel.moduleForEdit]. If this changes
     * and intent will be created for the [AddMarkActivity], whilst passing the selected module
     * and that modules ID through to this activity.
     *
     * The [MainActivity] will not be finished.
     */
    private fun observeModuleForEdit(){
        viewModel.moduleForEdit.observe(viewLifecycleOwner, { module ->
            val intent = Intent(activity, AddMarkActivity::class.java).apply {
                putExtra(SELECTED_MODULE_KEY, module)
                putExtra(SELECTED_MODULE_ID_KEY, module.uuid)
            }
            startActivity(intent)
        })
    }

    /**
     * Sets up an observer of [ModulesViewModel.error], if an error occurs
     * this integer will passed to the lambda function. This will call
     * [MainActivity.showSnackBar] to show a snack bar in the UI by retrieving
     * the relevant string from [getResources].
     */
    private fun observeErrors(){
        viewModel.error.observe(viewLifecycleOwner, { error ->
            (activity as? MainActivity)?.showSnackBar(getString(error), true)
        })
    }
}