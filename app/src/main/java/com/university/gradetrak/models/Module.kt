package com.university.gradetrak.models

data class Module(var name: String? = null, var credits: Int? = null, var level: Int? = null){

    var uuid : String? = null
    override fun toString(): String = "$name, Credits($credits), Level $level "
}
