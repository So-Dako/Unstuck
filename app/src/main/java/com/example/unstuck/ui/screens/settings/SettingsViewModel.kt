package com.example.unstuck.ui.screens.settings

import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = settingsManager.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _isLanguageMenuExpanded = MutableStateFlow(false)
    val isLanguageMenuExpanded: StateFlow<Boolean> = _isLanguageMenuExpanded.asStateFlow()

    private val _pendingLanguageChange = MutableStateFlow<String?>(null)
    val pendingLanguageChange: StateFlow<String?> = _pendingLanguageChange.asStateFlow()

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.saveDarkMode(enabled)
        }
    }

    fun languageMenuExpanded(){
        _isLanguageMenuExpanded.value = true
    }

    fun languageMenuDismiss(){
        _isLanguageMenuExpanded.value = false
    }

    fun setLocale(languageCode: String) {
        viewModelScope.launch {
            settingsManager.saveLanguage(languageCode)
            _pendingLanguageChange.value = languageCode
        }
    }

    fun onLanguageChangeHandled() {
        _pendingLanguageChange.value = null
    }
}