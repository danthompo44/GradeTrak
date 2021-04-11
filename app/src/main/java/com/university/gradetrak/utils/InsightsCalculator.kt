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
    private lateinit var lowestScoringModule: Module
    private lateinit var settings: Settings

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
        lowestScoringModule= modules [0]
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

    private fun removeLowestModuleResult(settings: Settings){
        for (module in level5ModulesWithResults){
            doLowestModuleChecks(module, settings)
        }
        for (module in level6ModulesWithResults){
            doLowestModuleChecks(module, settings)
        }

        if(lowestScoringModule.level == 5){
            level5ModulesWithResults.remove(lowestScoringModule)
            level5CreditsWithResults - lowestScoringModule.credits!!
        }
        if(lowestScoringModule.level == 6){
            level6ModulesWithResults.remove(lowestScoringModule)
            level6CreditsWithResults - lowestScoringModule.credits!!
        }
    }

    fun calculatePercentages(modules: List<Module>, settings: Settings): ArrayList<Double>{
        resetData()
        organiseData(modules)
        this.settings = settings
        val currentLevel5 = calculateCurrentLevel5Percentage()
        val overallLevel5 = calculateOverallLevel5Percentage()
        val weightedLevel5 = calculateWeightedLevel5Percentage()
        val currentLevel6 = calculateCurrentLevel6Percentage()
        val overallLevel6 = calculateOverallLevel6Percentage()
        val weightedLevel6 = calculateWeightedLevel6Percentage()
        val overall = calculateOverall()

        val percentages: ArrayList<Double> = ArrayList()
        percentages.add(currentLevel5)
        percentages.add(overallLevel5)
        percentages.add(weightedLevel5)
        percentages.add(currentLevel6)
        percentages.add(overallLevel6)
        percentages.add(weightedLevel6)
        percentages.add(overall)

        return percentages
    }

    private fun doLowestModuleChecks(module: Module, settings: Settings){
        if(settings.removeLowestModule!! && module.result!! <= lowestScoringModule.result!!
                && module.credits!! < lowestScoringModule.credits!!){
            lowestScoringModule = module
        }
    }

    /**
     * Calculates the current percentage achieved in the level 5 modules the user has
     * results for.
     * @param modules A list of all modules attributed to the user
     * @param settings A settings object containing the users settings
     */
    private fun calculateCurrentLevel5Percentage(): Double{
        if(level5ModulesWithResults.size != 0){
            //Calculate the results
            var totalGradeXCredits = 0.0
            for(module in level5ModulesWithResults){
                val gradeXCredits = module.result?.times(module.credits!!)
                totalGradeXCredits += gradeXCredits!!
            }
            return (totalGradeXCredits / level5CreditsWithResults).round()
        }
        return 0.0
    }

    private fun calculateCurrentLevel6Percentage(): Double{
        if(level6ModulesWithResults.size != 0){
            var totalGradeXCredits = 0.0
            for(module in level6ModulesWithResults){
                val gradeXCredits = module.result?.times(module.credits!!)
                totalGradeXCredits += gradeXCredits!!
            }
            return (totalGradeXCredits / level6CreditsWithResults).round()
        }
        return 0.0
    }

    /**
     * Calculates the weighted percentage achieved across level 5 using the results they have received
     * in all their level 5 modules
     * @param modules A list of all modules attributed to the user
     */
    private fun calculateWeightedLevel5Percentage(): Double{
        return addLevel5Weighting(calculateOverallLevel5Percentage()).round()
    }

    /**
     * Calculates the weighted percentage achieved across level 6 using the results they have received
     * in all their level 6 modules
     * @param modules A list of all modules attributed to the user
     */
    private fun calculateWeightedLevel6Percentage(): Double{
        return addLevel6Weighting(calculateOverallLevel6Percentage()).round()
    }

    private fun calculateOverall(): Double{
        return calculateWeightedLevel5Percentage() +
                calculateWeightedLevel6Percentage()
    }

    /**
     * Calculate the Overall Result for level 5 modules with a result, does not include a weighting
     */
    private fun calculateOverallLevel5Percentage(): Double{
        var creditsXResult = 0.0
        if(level5ModulesWithResults.size != 0) {
            for(module in level5ModulesWithResults){
                creditsXResult += module.credits?.times(module.result!!)!!
            }
            return creditsXResult / totalLevel5Credits
        }
        return 0.0
    }

    /**
     * Calculate the Overall Result for level 6 modules with a result, does not include a weighting
     */
    private fun calculateOverallLevel6Percentage(): Double{
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
     * Adds a weighting to a double dependent on the users settings
     * @param value A double to be weighted
     * @param settings A settings object that contains the users grading ratio
     */
    private fun addLevel5Weighting(value: Double) : Double{
        return if(settings.thirtySeventyRatio == true){
            value * 0.3
        } else value * 0.4
    }

    /**
     * Adds a weighting to a double dependent on the users settings
     * @param value A double to be weighted
     * @param settings A settings object that contains the users grading ratio
     */
    private fun addLevel6Weighting(value: Double) : Double{
        return if(settings.thirtySeventyRatio == true){
            value * 0.7
        } else value * 0.6
    }

    /**
     * Extension function of the double class to format to two decimal places
     */
    private fun Double.round(): Double {
        return String.format("%.2f", this).toDouble()
    }

}