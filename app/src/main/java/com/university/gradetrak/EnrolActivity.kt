package com.university.gradetrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.university.gradetrak.databinding.ActivityEnrolBinding
import com.university.gradetrak.models.Module
import com.university.gradetrak.ui.adapters.EnrolModuleRecyclerAdapter

class EnrolActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnrolBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEnrolBinding.inflate(layoutInflater)

        addToolBarNavListeners()
        setupRecyclerView()
        setContentView(binding.root)
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
        val adapter = EnrolModuleRecyclerAdapter(generateModuleList(10))
        binding.rvEnrolPageModules.adapter = adapter
        binding.rvEnrolPageModules.setHasFixedSize(true)
    }

    private fun generateModuleList(size: Int): List<Module>{
        val listOfDummyModules: MutableList<Module> = ArrayList()
        for(i in 0 until size){
            listOfDummyModules.add(Module("description$i", i))
        }

        return listOfDummyModules
    }
}