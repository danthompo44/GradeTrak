package com.university.gradetrak.utils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings

object InsightsCalculator {
    private val allLevel5Modules =  ArrayList<Module>()
    private val allLevel6Modules = ArrayList<Module>()
    private const val totalLevel5Credits: Int = 120
    private const val totalLevel6Credits: Int = 120
    private const val totalCredits: Int = 240

    private val level5ModulesWithResults = ArrayList<Module>()
    private val level6ModulesWithResults = ArrayList<Module>()
    private var level5CreditsWithResults: Int = 0
    private var level6CreditsWithResults: Int = 0

    /**
     * Ensure all arrays and values are reset
     */
    private fun resetData(){
        allLevel5Modules.clear()
        allLevel6Modules.clear()
        level5ModulesWithResults.clear()
        level6ModulesWithResults.clear()
        level5CreditsWithResults = 0
        level6CreditsWithResults = 0
    }

    /**
     * Organise the data into appropriate Array lists and Integers
     */
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

    /**
     * Calculates the current percentage achieved in the level 5 modules the user has
     * results for.
     * @param modules A list of all modules attributed to the user
     * @param settings A settings object containing the users settings
     */
    fun calculateCurrentLevel5Percentage(modules: List<Module>): Double{
        resetData()
        organiseData(modules)
        if(level5ModulesWithResults.size != 0){
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
        return 0.0
    }

    fun calculateCurrentLevel6Percentage(modules: List<Module>): Double{
        resetData()
        organiseData(modules)
        if(level6ModulesWithResults.size != 0){
            var totalGradeXCredits: Double = 0.0
            for(module in level6ModulesWithResults){
                val gradeXCredits = module.result?.times(module.credits!!)
                totalGradeXCredits += gradeXCredits!!
            }
            return (totalGradeXCredits / level6CreditsWithResults).round()
        }
        return 0.0
    }

    /**
     * Calculates the overall percentage achieved across level 5 using the results they have received
     * in all their level 5 modules
     * @param modules A list of all modules attributed to the user
     */
    fun calculateOverallLevel5Percentage(modules: List<Module>): Double{
        resetData()
        organiseData(modules)
        return calculateOverallLevel5Result().round()
    }

    /**
     * Calculates the overall percentage achieved across level 6 using the results they have received
     * in all their level 6 modules
     * @param modules A list of all modules attributed to the user
     */
    fun calculateOverallLevel6Percentage(modules: List<Module>): Double{
        resetData()
        organiseData(modules)
        return calculateOverallLevel6Result().round()
    }

    /**
     * Adds a weighting to a double dependent on the users settings
     * @param value A double to be weighted
     * @param settings A settings object that contains the users grading ratio
     */
    private fun addLevel5Weighting(value: Double, settings: Settings) : Double{
        return if(settings.thirtySeventyRatio == true){
            value * 0.3
        } else value * 0.4
    }

    /**
     * Adds a weighting to a double dependent on the users settings
     * @param value A double to be weighted
     * @param settings A settings object that contains the users grading ratio
     */
    private fun addLevel6Weighting(value: Double, settings: Settings) : Double{
        return if(settings.thirtySeventyRatio == true){
            value * 0.7
        } else value * 0.6
    }

    /**
     * Calculate the Overall Result for level 5 modules with a result, does not include a weighting
     */
    private fun calculateOverallLevel5Result(): Double{
        var creditsXResult = 0.0
        if(level5ModulesWithResults.size != 0) {
            for(module in level5ModulesWithResults){
//            Log.v(TAG, "${module.name}: Credits: ${module.credits} Result: ${module.result}")
                creditsXResult += module.credits?.times(module.result!!)!!
//            Log.v(TAG, "Credits X Result: $creditsXResult")
            }
//        Log.v(TAG, "Overall Level 5 Result: ${creditsXResult / totalLevel5Credits}")
            return creditsXResult / totalLevel5Credits
        }
        return 0.0
    }

    /**
     * Calculate the Overall Result for level 6 modules with a result, does not include a weighting
     */
    private fun calculateOverallLevel6Result(): Double{
        if(level6ModulesWithResults.size != 0){
            var creditsXResult = 0.0
            for(module in level6ModulesWithResults){
                creditsXResult += module.credits?.times(module.result!!)!!
            }
            return creditsXResult / totalLevel6Credits
        }
        return 0.0
    }

    /**
     * Extension function of the double class to format to two decimal places
     */
    private fun Double.round(): Double {
        return String.format("%.2f", this).toDouble()
    }

}