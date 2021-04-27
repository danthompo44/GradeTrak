package com.university.gradetrak.utils

import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings

object InsightsCalculator {
    private val allLevel5Modules =  ArrayList<Module>()
    private val allLevel6Modules = ArrayList<Module>()
    private var totalLevel5Credits: Int = 0
    private var totalLevel6Credits: Int = 0
    private var totalCredits: Int = 0

    private val level5ModulesWithResults = ArrayList<Module>()
    private val level6ModulesWithResults = ArrayList<Module>()
    private var level5CreditsWithResults: Int = 0
    private var level6CreditsWithResults: Int = 0

    private var receivedLevel5Credits: Int = 0
    private var receivedLevel6Credits: Int = 0
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
        totalLevel5Credits = settings.level5Credits!!
        totalLevel6Credits = settings.level6Credits!!
        totalCredits = totalLevel5Credits + totalLevel6Credits
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
     * Will call [resetData], set [settings] and then [organiseData] using [modules].
     *
     * Once the data has been organised it acts as a facade method to call methods such as
     * [calculateCurrentLevel5Percentage] or [calculateCurrentLevel6Percentage]. These methods
     * handle the heavy lifting, this method just calls them, gathers the information @return
     * an array list of the information.
     */
    fun calculatePercentages(modules: List<Module>, settings: Settings): ArrayList<Double>{
        resetData()
        this.settings = settings
        organiseData(modules)
        receivedLevel5Credits = level5CreditsWithResults
        receivedLevel6Credits = level6CreditsWithResults

        val currentLevel5 = calculateCurrentLevel5Percentage()
        val overallLevel5 = calculateOverallLevel5Percentage()
        val weightedLevel5 = calculateWeightedLevel5Percentage()
        val currentLevel6 = calculateCurrentLevel6Percentage()
        val overallLevel6 = calculateOverallLevel6Percentage()
        val weightedLevel6 = calculateWeightedLevel6Percentage()
        val overall = calculateOverall()
        val overallWithLowestRemoved = getLowestModuleResult()

        val percentages: ArrayList<Double> = ArrayList()
        percentages.add(currentLevel5)
        percentages.add(overallLevel5)
        percentages.add(weightedLevel5)
        percentages.add(currentLevel6)
        percentages.add(overallLevel6)
        percentages.add(weightedLevel6)
        percentages.add(overall)
        percentages.add(overallWithLowestRemoved)

        return percentages
    }

    /**
     * Returns the received credits of [receivedLevel5Credits]
     * and [receivedLevel6Credits] in an [ArrayList].
     */
    fun getReceivedCredits(): ArrayList<Int>{
        val credits: ArrayList<Int> = ArrayList()
        credits.add(receivedLevel5Credits)
        credits.add(receivedLevel6Credits)

        return credits
    }


    /**
     * Calls [removeLowestModuleResult], after thsi method has complete
     * it will @return [calculateOverall].
     */
    private fun getLowestModuleResult(): Double{
        removeLowestModuleResult()
        return calculateOverall()
    }

    /**
     * Handles complex business logic to remove the lowest module if
     * there is a module with a result. Removes that modules credit score and
     * the module itself.
     *
     * Makes use of [doLowestModuleChecks] to deduce what is the lowest module.
     */
    private fun removeLowestModuleResult(){
        lowestScoringModule = if(level5ModulesWithResults.isEmpty()){
            if(level6ModulesWithResults.isEmpty()){
                return
            } else{
                level6ModulesWithResults[0]
            }
        } else {
            level5ModulesWithResults[0]
        }
        for (module in level5ModulesWithResults){
            doLowestModuleChecks(module)
        }
        for (module in level6ModulesWithResults){
            doLowestModuleChecks(module)
        }

        if(lowestScoringModule.level == 5){
            level5ModulesWithResults.remove(lowestScoringModule)
            level5CreditsWithResults -= lowestScoringModule.credits!!
            totalLevel5Credits -= lowestScoringModule.credits!!
        }
        if(lowestScoringModule.level == 6){
            level6ModulesWithResults.remove(lowestScoringModule)
            level6CreditsWithResults -= lowestScoringModule.credits!!
            totalLevel6Credits -= lowestScoringModule.credits!!
        }
    }

    /**
     * Uses [calculateWeightedLevel5Percentage]
     * + [calculateCurrentLevel6Percentage] to deduce
     * a result of the users overall score.
     */
    private fun calculateOverall(): Double{
        return (calculateWeightedLevel5Percentage() +
                calculateWeightedLevel6Percentage()).round()
    }

    /**
     * Checks if [settings] are set to remove lowest module.
     *
     * If true will remove set the lowest scoring module to that
     * which has the beiggest impact on the overall grade
     * using [calculateModuleWeightedMark].
     */
    private fun doLowestModuleChecks(module: Module){
        if(settings.removeLowestModule!!){
            if(module.result!! < lowestScoringModule.result!!){
                lowestScoringModule = module
            } else if (module.result!! == lowestScoringModule.result!!){
                if(calculateModuleWeightedMark(module)
                    > calculateModuleWeightedMark(lowestScoringModule)){
                    lowestScoringModule = module
                }
            }
        }
    }
    /**
     * Calculates the weighted percentage achieved across level 5 using
     * the results they have received in all their level 5 modules
     */
    private fun calculateWeightedLevel5Percentage(): Double{
        return addLevel5Weighting(calculateOverallLevel5Percentage()).round()
    }

    /**
     * Calculates the weighted percentage achieved across level 6
     * using the results they have received
     * in all their level 6 modules
     * @param modules A list of all modules attributed to the user
     */
    private fun calculateWeightedLevel6Percentage(): Double{
        return addLevel6Weighting(calculateOverallLevel6Percentage()).round()
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
            return (creditsXResult / totalLevel5Credits).round()
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
            return (creditsXResult / totalLevel6Credits).round()
        }
        return 0.0
    }

    /**
     * Calculates the current percentage achieved in the level 5 modules the user has
     * results for.
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

    /**
     * Calculates the current percentage achieved in the level 6 modules the user has
     * results for.
     */
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
     * Returns a string represntation of [lowestScoringModule]'s name.
     */
    fun getLowestModuleString(): String{
        return lowestScoringModule.name!!
    }

    /**
     * Uses the credits if the module and the overall credits
     * set in [Settings] to deduce the weighted mark.
     */
    private fun calculateModuleWeightedMark(module: Module): Double{
        var overallYearCredits = 0.0
        overallYearCredits = if(module.level == 5){
            totalLevel5Credits.toDouble()
        } else {
            totalLevel6Credits.toDouble()
        }
        val creditsXResult = module.result?.times(module.credits!!)
        val yearWeightedResult: Double? = creditsXResult?.div(overallYearCredits)
        return if(module.level == 5){
            addLevel5Weighting(yearWeightedResult!!)
        } else {
            addLevel6Weighting(yearWeightedResult!!)
        }
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