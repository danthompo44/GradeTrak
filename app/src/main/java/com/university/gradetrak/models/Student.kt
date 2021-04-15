package com.university.gradetrak.models

class Student(var uuid: String? = null, var firstName: String? = null,
              var surname: String? = null) {

    override fun toString(): String {
        return "$firstName $surname"
    }
}