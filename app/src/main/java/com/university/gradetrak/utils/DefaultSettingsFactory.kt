package com.university.gradetrak.utils

import com.university.gradetrak.models.Settings

object DefaultSettingsFactory {
    fun getSettings(userId: String?) : Settings {
        return Settings(thirtySeventyRatio = true, removeLowestModule = true,
                120, 120, userId)
    }
}