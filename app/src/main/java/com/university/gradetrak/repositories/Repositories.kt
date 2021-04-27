package com.university.gradetrak.repositories

object Repositories {
    var moduleRepository: ModuleRepository? = null
    val settingsRepository = SettingsRepository()
    val studentRepository = StudentRepository()

    /**
     * Will initiate the module repo if null
     * @return Returns the module repo instance
     */
    fun getModuleRepository(userId: String): ModuleRepository{
        if(moduleRepository == null){
            moduleRepository = ModuleRepository(userId)
            return this.moduleRepository!!
        }
        return this.moduleRepository!!
    }
}