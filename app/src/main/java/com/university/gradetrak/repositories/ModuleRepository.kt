package com.university.gradetrak.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.university.gradetrak.models.Module
import com.university.gradetrak.utils.TAG
import java.util.*
import kotlin.collections.ArrayList

class ModuleRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val modules = database.getReference("modules")
    val modulesLD = MutableLiveData<List<Module>>()

    init {
        getAll()
    }

    fun addEditStudent(module: Module){
        Log.v(TAG, module.toString())
        modules.child("A User Id").child(UUID.randomUUID().toString()).setValue(module)
    }

    fun delete(module: Module){
        modules.child(module.uuid.toString()).removeValue()
    }

    private fun getAll(){
        // Read from the database
        modules.addValueEventListener(object : ValueEventListener {
            val modulesArray = ArrayList<Module>()
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                modulesArray.clear()
                for(child in dataSnapshot.children){
                    val module = child.getValue(Module::class.java)
                    module!!.uuid = child.key.toString()
                    modulesArray.add(module)
                }
                modulesLD.value = modulesArray
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("GradeTrak", "Failed to read value.", error.toException())
            }
        })
    }
}