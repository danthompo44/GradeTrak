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

    private fun setupViewModelBinding(){
        val viewModelFactory = ModulesViewModelFactory(Services.getModuleService(auth.uid!!))
        viewModel = ViewModelProvider(this, viewModelFactory).get(ModulesViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupRecyclerViews(){
        linearLayoutManager = LinearLayoutManager(activity)
        binding.rvModulesPageModules.layoutManager = linearLayoutManager
    }

    private fun setupNavigationListener(){
        binding.ivAddModuleToolbarAddModule.setOnClickListener {
            startActivity(Intent(activity, AddModuleActivity::class.java))
        }
    }

    private fun setupViewModelObservers(){
        observeUsersModules()
        observeModuleForEdit()
        observeErrors()
    }

    private fun observeUsersModules(){
        viewModel.getUsersModules().observe(viewLifecycleOwner, { modules ->
            val adapter = ModuleRecyclerAdapter(modules, viewModel.selectedModule,
                resources)
            binding.rvModulesPageModules.adapter = adapter
            binding.rvModulesPageModules.setHasFixedSize(true)
        })
    }

    private fun observeModuleForEdit(){
        viewModel.moduleForEdit.observe(viewLifecycleOwner, { module ->
            val intent = Intent(activity, AddMarkActivity::class.java).apply {
                putExtra(SELECTED_MODULE_KEY, module)
                putExtra(SELECTED_MODULE_ID_KEY, module.uuid)
            }
            startActivity(intent)
        })
    }

    private fun observeErrors(){
        viewModel.error.observe(viewLifecycleOwner, { error ->
            (activity as? MainActivity)?.showSnackBar(error, true)
        })
    }
}