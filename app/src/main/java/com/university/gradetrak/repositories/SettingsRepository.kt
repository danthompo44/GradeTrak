package com.university.gradetrak.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.university.gradetrak.models.Settings
import com.university.gradetrak.utils.TAG
import java.util.*
import kotlin.collections.ArrayList

class SettingsRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val settings = database.getReference("settings").child("A user ID")
    val settingsLD = MutableLiveData<Settings>()

    init {
        getAll()
    }

    fun addSettings(settings: Settings){
        this.settings.setValue(settings)
    }

    fun editSettings(settings: Settings){
        this.settings.child(settings.uuid.toString()).setValue(settings)
    }

    fun delete(settings: Settings){
        this.settings.child(settings.uuid.toString()).removeValue()
    }


    private fun getAll(){
        // Read from the database
        settings.addValueEventListener(object : ValueEventListener {
            var settingsValue: Settings? = null
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(child in dataSnapshot.children){
                    settingsValue = child.getValue(Settings::class.java)
                    settingsValue!!.uuid = child.key.toString()
                }
                settingsLD.value = settingsValue
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("GradeTrak", "Failed to read value.", error.toException())
            }
        })
    }
}