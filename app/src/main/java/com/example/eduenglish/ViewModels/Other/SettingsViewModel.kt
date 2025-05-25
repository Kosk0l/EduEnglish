package com.example.eduenglish.ViewModels.Other

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduenglish.ui.theme.AppTheme
import com.example.eduenglish.ui.theme.SettingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = SettingPreferences(application)

    private val _theme = MutableStateFlow(AppTheme.WHITE)
    val theme: StateFlow<AppTheme> = _theme

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    //====================================================================================================

    init {
        viewModelScope.launch {
            preferences.themeFlow.collect {
                _theme.value = it
            }
        }

        viewModelScope.launch {
            preferences.emailFlow.collect {
                _email.value = it ?: ""
            }
        }

        viewModelScope.launch {
            preferences.notificationEnabledFlow.collect {
                _notificationsEnabled.value = it
            }
        }
    }

    fun setTheme(newTheme: AppTheme) {
        _theme.value = newTheme
        viewModelScope.launch {
            preferences.saveTheme(newTheme)
        }
    }

    fun saveEmail(email: String) {
        viewModelScope.launch {
            preferences.saveEmail(email)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        viewModelScope.launch {
            preferences.saveNotificationEnabled(enabled)
        }
    }

}