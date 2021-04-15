package com.university.gradetrak.models

data class Settings(var thirtySeventyRatio: Boolean? = null, var removeLowestModule: Boolean? = null,
var level5Credits: Int? = null, var level6Credits: Int? = null, var userId: String? = null) {


    override fun toString(): String {
        return "${getRatioString()} ${getModuleString()}"
    }

    private fun getRatioString(): String{
        return if(thirtySeventyRatio == true){
            "Level Ratio - 30% : 70%"
        } else {
            "Level Ratio - 40% : 60%"
        }
    }

    private fun getModuleString(): String{
        return if(removeLowestModule == true){
            "Lowest module is removed"
        } else {
            "Lowest module is not removed"
        }
    }
}