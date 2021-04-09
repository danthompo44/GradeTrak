package com.university.gradetrak.services

import com.university.gradetrak.repositories.Repositories

object Services {
    val moduleService = ModuleService(Repositories.moduleRepository)
    val settingsService = SettingsService(Repositories.settingsRepository)
}