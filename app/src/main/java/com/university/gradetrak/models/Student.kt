package com.university.gradetrak.models

class Student(var uuid: String? = null, var firstName: String? = null,
              var surname: String? = null, var email: String? = null) {

    override fun toString(): String {
        return "$firstName $surname. Email: $email"
    }
}