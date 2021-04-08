package com.university.gradetrak.ui.modules

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.university.gradetrak.MainActivity
import com.university.gradetrak.databinding.FragmentModulesBinding
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.Services
import com.university.gradetrak.ui.adapters.ModuleRecyclerAdapter
import com.university.gradetrak.ui.addModule.AddModuleActivity
import com.university.gradetrak.ui.addMark.AddMarkActivity
import com.university.gradetrak.utils.SELECTED_MODULE_ID_KEY
import com.university.gradetrak.utils.SELECTED_MODULE_KEY

class ModulesFragment : Fragment(), ModuleRecyclerAdapter.OnItemClickListener {
    private lateinit var binding: FragmentModulesBinding
    private lateinit var viewModel: ModulesViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

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
        val viewModelFactory = ModulesViewModelFactory(Services.moduleService)
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
                this, resources)
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

    private fun generateModuleList(): List<Module>{
        val listOfDummyModules: MutableList<Module> = ArrayList()
        for(i in 0..5){
            listOfDummyModules.add(Module("description", i, i))
        }
        listOfDummyModules[2].credits = null

        return listOfDummyModules
    }

//  Override function for recycler view interface
    override fun onItemClick(module: Module) {
        (activity as? MainActivity)?.showSnackBar(module.toString(), false)
    }
}