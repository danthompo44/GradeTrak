package com.university.gradetrak.repositories

object Repositories {
    var moduleRepository: ModuleRepository? = null
    val settingsRepository = SettingsRepository()
    val studentRepository = StudentRepository()

    fun getModuleRepository(userId: String): ModuleRepository{
        if(moduleRepository == null){
            moduleRepository = ModuleRepository(userId)
            return this.moduleRepository!!
        }
        return this.moduleRepository!!
    }
}