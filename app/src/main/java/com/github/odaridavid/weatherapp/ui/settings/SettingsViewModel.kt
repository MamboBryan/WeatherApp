package com.github.odaridavid.weatherapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.weatherapp.core.api.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsScreenViewState(isLoading = true))
    val state: StateFlow<SettingsScreenViewState> = _state.asStateFlow()

    fun processIntent(settingsScreenIntent: SettingsScreenIntent) {
        when (settingsScreenIntent) {
            SettingsScreenIntent.LoadSettingScreenData -> {
                viewModelScope.launch {
                    setState { copy(versionInfo = settingsRepository.getAppVersion()) }
                }
                viewModelScope.launch {
                    settingsRepository.getLanguage().collect { language ->
                        setState { copy(selectedLanguage = language) }
                    }
                }
                viewModelScope.launch {
                    settingsRepository.getUnits().collect { units ->
                        setState { copy(units = units) }
                    }
                }
                viewModelScope.launch {
                    setState { copy(availableLanguages = settingsRepository.getAvailableLanguages()) }
                }
                viewModelScope.launch {
                    setState { copy(availableLanguages = settingsRepository.getAvailableMetrics()) }
                }
            }

            is SettingsScreenIntent.ChangeLanguage -> {
                viewModelScope.launch {
                    settingsRepository.setLanguage(settingsScreenIntent.selectedLanguage)
                    setState { copy(units = settingsScreenIntent.selectedLanguage) }
                }
            }

            is SettingsScreenIntent.ChangeUnits -> {
                viewModelScope.launch {
                    settingsRepository.setUnits(settingsScreenIntent.selectedUnits)
                    setState { copy(units = settingsScreenIntent.selectedUnits) }
                }
            }
        }
    }

    private fun setState(stateReducer: SettingsScreenViewState.() -> SettingsScreenViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}

data class SettingsScreenViewState(
    val units: String = "",
    val selectedLanguage: String = "",
    val availableLanguages: List<String> = emptyList(),
    val availableMetrics: List<String> = emptyList(),
    val versionInfo: String = "",
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
