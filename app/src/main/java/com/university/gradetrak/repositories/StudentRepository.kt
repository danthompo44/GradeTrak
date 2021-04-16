package com.university.gradetrak.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.university.gradetrak.models.Student

class StudentRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val students = database.getReference("students")
    val studentsLD = MutableLiveData<List<Student>>()

    init {
        getAll()
    }

    fun addStudent(student: Student){
        students.child(student.uuid!!).setValue(student)
    }

    fun getStudent(userId: String) : MutableLiveData<Student> {
        val student = MutableLiveData<Student>()
        for(s in studentsLD.value!!){
            if(s.uuid == userId){
                student.value = s
            }
        }
        return student
    }

    private fun getAll(){
        // Read from the database
        students.addValueEventListener(object : ValueEventListener {
            val studentsArray = ArrayList<Student>()
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                studentsArray.clear()
                for(child in dataSnapshot.children){
                    val student = child.getValue(Student::class.java)
                    student!!.uuid = child.key.toString()
                    studentsArray.add(student)
                }
                studentsLD.value = studentsArray
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("GradeTrak", "Failed to read value.", error.toException())
            }
        })
    }
}