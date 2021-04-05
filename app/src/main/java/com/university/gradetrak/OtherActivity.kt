package com.university.gradetrak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase

class OtherActivity : AppCompatActivity() {
    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)
        val test = database.getReference("test")
        test.setValue("Testing")
    }
}