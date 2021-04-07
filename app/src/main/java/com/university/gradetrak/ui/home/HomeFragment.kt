package com.university.gradetrak.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.university.gradetrak.R
import com.university.gradetrak.databinding.FragmentHomeBinding
import com.university.gradetrak.models.Module
import com.university.gradetrak.ui.adapters.ModuleRecyclerAdapter

class HomeFragment : Fragment(), ModuleRecyclerAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var linearLayoutManagerLevel5: LinearLayoutManager
    private lateinit var linearLayoutManagerLevel6: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupViewModelBinding()
        setupRecyclerViews()

        return binding.root
    }

    private fun setupViewModelBinding(){
        val viewModelFactory = HomeViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupRecyclerViews(){
        linearLayoutManagerLevel5 = LinearLayoutManager(activity)
        linearLayoutManagerLevel6 = LinearLayoutManager(activity)
        binding.rvLevel5ModulesList.layoutManager = linearLayoutManagerLevel5
        binding.rvLevel6ModulesList.layoutManager = linearLayoutManagerLevel6

        val adapterLevel5 = ModuleRecyclerAdapter(generateModuleList(), this)
        val adapterLevel6 = ModuleRecyclerAdapter(generateModuleList(), this)
        binding.rvLevel5ModulesList.adapter = adapterLevel5
        binding.rvLevel5ModulesList.setHasFixedSize(true)
        binding.rvLevel6ModulesList.adapter = adapterLevel6
        binding.rvLevel6ModulesList.setHasFixedSize(true)
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
    override fun onItemClick(position: Int) {
        Toast.makeText(activity, "Clicked at position $position", Toast.LENGTH_LONG).show()
    }
}