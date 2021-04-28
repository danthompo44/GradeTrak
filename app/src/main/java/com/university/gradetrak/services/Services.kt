package com.university.gradetrak.services

import com.university.gradetrak.repositories.Repositories

object Services {
    val settingsService = SettingsService(Repositories.settingsRepository)
    val studentService = StudentService(Repositories.studentRepository)
}