package com.university.gradetrak.services

import com.university.gradetrak.repositories.Repositories

object Services {
    private var moduleService: ModuleService? = null
    val settingsService = SettingsService(Repositories.settingsRepository)
    val studentService = StudentService(Repositories.studentRepository)

    /**
     * Will initiate the module service if null
     * @return Returns the module service instance
     */
    fun getModuleService(userId: String): ModuleService{
        if(moduleService == null){
            moduleService = ModuleService(Repositories.getModuleRepository(userId))
            return this.moduleService!!
        }
        return this.moduleService!!
    }
}