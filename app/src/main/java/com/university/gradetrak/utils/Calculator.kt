package com.university.gradetrak.utils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings

object Calculator {
    private val allLevel5Modules =  ArrayList<Module>()
    private val allLevel6Modules = ArrayList<Module>()
    private const val totalLevel5Credits: Int = 120
    private const val totalLevel6Credits: Int = 120
    private const val totalCredits: Int = 240

    private val level5ModulesWithResults = ArrayList<Module>()
    private val level6ModulesWithResults = ArrayList<Module>()
    var level5CreditsWithResults: Int = 0
    var level6CreditsWithResults: Int = 0

    /**
     * Ensure all arrays and values are reset
     */
    private fun clearUp(){
        allLevel5Modules.clear()
        allLevel6Modules.clear()
        level5ModulesWithResults.clear()
        level6ModulesWithResults.clear()
        level5CreditsWithResults = 0
        level6CreditsWithResults = 0
    }

    fun calculateCurrentLevel5Percentage(modules: List<Module>, settings: Settings): Double{
        clearUp()
        organiseData(modules)
        //Calculate the results
        var totalGradeXCredits: Double = 0.0
//        Log.v(TAG, level5ModulesWithResults.size.toString())
        for(module in level5ModulesWithResults){
//            Log.v(TAG, "${module.name}: Result: ${module.result} Credits: ${module.credits}")
            val gradeXCredits = module.result?.times(module.credits!!)
//            Log.v(TAG, "Grade X Credits: ${gradeXCredits.toString()}")
            totalGradeXCredits += gradeXCredits!!
        }
//        Log.v(TAG, "Total Grade X Credits: $totalGradeXCredits")
//        Log.v(TAG, "Total Level 5 Credits With Results: $level5CreditsWithResults")
//        Log.v(TAG, "Returns: ${totalGradeXCredits/ level5CreditsWithResults}")
        return (totalGradeXCredits / level5CreditsWithResults).round()
    }

    fun calculateOverallPercentage(modules: List<Module>,
                                   settings: Settings): Double{
        organiseData(modules)
        val level5 = addLevel5Weighting(calculateOverallLevel5Result(), settings)
        val level6 = addLevel6Weighting(calculateOverallLevel6Result(), settings)
        return level5 + level6
    }

    private fun organiseData(modules: List<Module>){
        //Assign modules and credits to their appropriate variables
        for(module in modules){
            if(module.level == 5) {
                allLevel5Modules.add(module)
                if(module.result != null){
                    level5ModulesWithResults.add(module)
                    level5CreditsWithResults += module.credits!!
                }
            } else if (module.level == 6) {
                allLevel6Modules.add(module)
                if(module.result != null){
                    level6ModulesWithResults.add(module)
                    level6CreditsWithResults += module.credits!!
                }
            }
        }
    }

    private fun addLevel5Weighting(value: Double, settings: Settings) : Double{
        return if(settings.thirtySeventyRatio == true){
            value * 0.4
        } else value * 0.3
    }

    private fun addLevel6Weighting(value: Double, settings: Settings) : Double{
        return if(settings.thirtySeventyRatio == true){
            value * 0.7
        } else value * 0.6
    }

    /**
     * Calculate the Overall Result for level 5 modules with a result, does not include a weighting
     */
    private fun calculateOverallLevel5Result(): Double{
        var creditsXResult: Double = 0.0
        for(module in level5ModulesWithResults){
            creditsXResult += module.credits?.times(module.result!!)!!
        }
        return creditsXResult / totalLevel5Credits
    }

    /**
     * Calculate the Overall Result for level 6 modules with a result, does not include a weighting
     */
    private fun calculateOverallLevel6Result(): Double{
        var creditsXResult: Double = 0.0
        for(module in level6ModulesWithResults){
            creditsXResult += module.credits?.times(module.result!!)!!
        }
        return creditsXResult / totalLevel6Credits
    }

    /**
     * Extension function of the double class to format to two decimal places
     */
    private fun Double.round(): Double {
        return String.format("%.2f", this).toDouble()
    }

}