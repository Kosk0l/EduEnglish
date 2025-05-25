package com.example.eduenglish.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingPreferences(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NOTIFICATION_KEY = stringPreferencesKey("notifications_enabled")
    }

    //====================================================================================================

    val themeFlow: Flow<AppTheme> = context.dataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY]
        themeName?.let { AppTheme.valueOf(it) } ?: AppTheme.WHITE
    }

    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    //====================================================================================================

    val emailFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[EMAIL_KEY]
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    //====================================================================================================

    val notificationEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_KEY]?.toBooleanStrictOrNull() ?: true // По умолчанию включено
    }

    suspend fun saveNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = enabled.toString()
        }
    }

}