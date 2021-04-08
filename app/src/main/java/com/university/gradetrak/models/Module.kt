package com.university.gradetrak.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Module(var name: String? = null,
                  var credits: Int? = null,
                  var level: Int? = null,
                  var result: Int? = null)
    : Parcelable{

    var uuid : String? = null
    override fun toString(): String = "$name, Credits($credits), Level $level "
}
