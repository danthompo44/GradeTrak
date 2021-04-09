package com.university.gradetrak.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.university.gradetrak.models.Settings
import com.university.gradetrak.repositories.SettingsRepository
import com.university.gradetrak.utils.TAG

class SettingsService(private val repository: SettingsRepository) {
    fun getAll():MutableLiveData<Settings>{
        return repository.settingsLD
    }

    fun addSettings(settings: Settings){
        repository.addSettings(settings)
    }

    fun editSettings(settings: Settings){
        repository.editSettings(settings)
    }

    fun deleteSettings(settings: Settings){
        repository.delete(settings)
    }
}