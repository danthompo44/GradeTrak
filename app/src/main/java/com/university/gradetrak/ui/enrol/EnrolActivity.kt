package com.university.gradetrak.ui.enrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.university.gradetrak.BaseActivity
import com.university.gradetrak.ui.addModule.AddModuleActivity
import com.university.gradetrak.ui.editModule.EditModuleActivity
import com.university.gradetrak.databinding.ActivityEnrolBinding
import com.university.gradetrak.models.Module
import com.university.gradetrak.services.Services
import com.university.gradetrak.ui.adapters.EnrolModuleRecyclerAdapter
import com.university.gradetrak.utils.SELECTED_MODULE_KEY

class EnrolActivity : BaseActivity() {
    private lateinit var binding: ActivityEnrolBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val viewModel: EnrolViewModel by viewModels {
        EnrolViewModelFactory(Services.moduleService)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrolBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        addToolBarNavListeners()
        setupRecyclerView()
        observeModuleTable()
    }

    /**
     * Adds a listener to the navigation part of the tool bar at the top of the UI.
     * Seen as a white arrow in the top left corner and a plus in the top right.
     * If the arrow is clicked the activity will finish, resorting to the still open main activity.
     * The plus opens the add module activity
     */
    private fun addToolBarNavListeners() {
        binding.tbEnrolPage.setNavigationOnClickListener {
            finish()
        }
        binding.ivEnrolToolbarAddModule.setOnClickListener {
            startActivity(Intent(this, AddModuleActivity::class.java))
        }
    }

    private fun setupRecyclerView(){
        linearLayoutManager = LinearLayoutManager(this)
        binding.rvEnrolPageModules.layoutManager = linearLayoutManager
    }

    private fun observeModuleTable(){
        viewModel.getAllUsersModules().observe(this, Observer { modules ->
            val adapter = EnrolModuleRecyclerAdapter(modules, viewModel.selectedModule)
            binding.rvEnrolPageModules.adapter = adapter
            binding.rvEnrolPageModules.setHasFixedSize(true)
        })
    }

    fun handleEditButtonClick(view: View){
        val intent = Intent(this, EditModuleActivity::class.java).apply {
            putExtra(SELECTED_MODULE_KEY, viewModel.selectedModule.value)
        }
        startActivity(intent)
    }

//    private fun generateModuleList(size: Int): List<Module>{
//        val listOfDummyModules: MutableList<Module> = ArrayList()
//        for(i in 0 until size){
//            listOfDummyModules.add(Module("description$i", i, i))
//        }
//
//        return listOfDummyModules
//    }
}