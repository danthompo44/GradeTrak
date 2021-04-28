package com.university.gradetrak.services

import androidx.lifecycle.MutableLiveData
import com.university.gradetrak.models.Settings
import com.university.gradetrak.repositories.SettingsRepository

class SettingsService(private val repository: SettingsRepository) {
    fun getAll():MutableLiveData<List<Settings>>{
        return repository.settingsLD
    }

    fun addEditSettings(settings: Settings){
        repository.addEditSettings(settings)
    }
}