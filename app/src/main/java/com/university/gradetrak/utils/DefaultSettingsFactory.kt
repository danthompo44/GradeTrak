package com.university.gradetrak.utils

import com.university.gradetrak.models.Settings

/**
 * A static object that is used as a factory for
 * creating a settings object with 'default' settings.
 */
object DefaultSettingsFactory {
    fun getSettings(userId: String?) : Settings {
        return Settings(thirtySeventyRatio = true, removeLowestModule = true,
                120, 120, userId)
    }
}