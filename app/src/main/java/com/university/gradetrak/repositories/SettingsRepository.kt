package com.university.gradetrak.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.university.gradetrak.models.Settings

class SettingsRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val settings = database.getReference("settings")
    val settingsLD = MutableLiveData<List<Settings>>()

    init {
        getAll()
    }

    /**
     * Adds or edits settings in the DB
     * @param settings The settings to be added/edited
     */
    fun addEditSettings(settings: Settings){
        this.settings.child(settings.userId!!).setValue(settings)
    }

    /**
     * Adds a listener to the database reference, this then updates the settings live data
     * which can be observed
     */
    private fun getAll(){
        // Read from the database
        settings.addValueEventListener(object : ValueEventListener {
            var settingsArray = ArrayList<Settings>()
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(child in dataSnapshot.children){
                    val settings = child.getValue(Settings::class.java)
                    settings!!.userId = child.key.toString()

                    settingsArray.add(settings)
                }
                settingsLD.value = settingsArray
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("GradeTrak", "Failed to read value.", error.toException())
            }
        })
    }
}