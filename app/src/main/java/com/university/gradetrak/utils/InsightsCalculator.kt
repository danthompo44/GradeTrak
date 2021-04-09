package com.university.gradetrak.utils

import androidx.lifecycle.MutableLiveData
import com.university.gradetrak.models.Module
import com.university.gradetrak.models.Settings

class InsightsCalculator(private val modules: MutableLiveData<List<Module>>, settings: MutableLiveData<Settings>) {
    private val allLevel5Modules =  ArrayList<Module>()
    private val allLevel6Modules = ArrayList<Module>()
    private var totalLevel5Credits: Int = 0
    private var totalLevel6Credits: Int = 0
    private var totalCredits: Int = 0

    private val level5ModulesWithResults = ArrayList<Module>()
    private val level6ModulesWithResults = ArrayList<Module>()
    private var level5CreditsWithResults: Int = 0
    private var level6CreditsWithResults: Int = 0

    init {
        for(module in modules.value!!){
            if(module.level == 5) {
                allLevel5Modules.add(module)
                totalLevel5Credits += module.credits!!
                totalCredits += module.credits!!
                if(module.result != null){
                    level5ModulesWithResults.add(module)
                    level5CreditsWithResults += module.credits!!
                }
            } else if (module.level == 6) {
                allLevel6Modules.add(module)
                totalLevel6Credits += module.credits!!
                totalCredits += module.credits!!
                if(module.result != null){
                    level6ModulesWithResults.add(module)
                    level6CreditsWithResults += module.credits!!
                }
            }
        }
    }

    fun calculateCurrentProgress(){

    }

    /**
     * Returns a double value of the current percentage achieved at level 5 relative
     * to how many results the user has received. (Module Grade x Module Credits)/ Total Module Credits
     */
    fun getLevel5CurrentProgress() : Double {
        var totalGradeXCredits: Double = 0.0
        for(module in level5ModulesWithResults){
            val gradeXCredits = module.result?.times(module.credits!!)
                totalGradeXCredits += gradeXCredits!!
        }
        return totalGradeXCredits / level5CreditsWithResults
    }
}