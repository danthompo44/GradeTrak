package com.university.gradetrak.services

import androidx.lifecycle.MutableLiveData
import com.university.gradetrak.models.Student
import com.university.gradetrak.repositories.StudentRepository

class StudentService (private val repository: StudentRepository) {
    fun getAll(): MutableLiveData<List<Student>> {
        return repository.studentsLD
    }

    fun addStudent(student: Student){
        repository.addStudent(student)
    }

    fun getStudent(userId: String): MutableLiveData<Student>{
        return repository.getStudent(userId)
    }
}