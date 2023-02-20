package com.github.odaridavid.weatherapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.odaridavid.weatherapp.ui.home.HomeScreen
import com.github.odaridavid.weatherapp.ui.home.HomeScreenIntent
import com.github.odaridavid.weatherapp.ui.home.HomeViewModel
import com.github.odaridavid.weatherapp.ui.home.HomeScreenViewState
import com.github.odaridavid.weatherapp.ui.settings.SettingsScreen
import com.github.odaridavid.weatherapp.ui.settings.SettingsScreenIntent
import com.github.odaridavid.weatherapp.ui.settings.SettingsScreenViewState
import com.github.odaridavid.weatherapp.ui.settings.SettingsViewModel

@Composable
fun WeatherAppScreensConfig(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel
) {
    NavHost(navController = navController, startDestination = Destinations.HOME.route) {
        composable(Destinations.HOME.route) {
            val state = homeViewModel
                .state
                .collectAsState(initial = HomeScreenViewState())
                .value

            homeViewModel.processIntent(HomeScreenIntent.LoadWeatherData)

            HomeScreen(
                state = state,
                onSettingClicked = { navController.navigate(Destinations.SETTINGS.route) },
                onTryAgainClicked = { homeViewModel.processIntent(HomeScreenIntent.LoadWeatherData) }
            )
        }
        composable(Destinations.SETTINGS.route) {
            val state = settingsViewModel
                .state
                .collectAsState(initial = SettingsScreenViewState())
                .value

            settingsViewModel.processIntent(SettingsScreenIntent.LoadSettingScreenData)

            SettingsScreen(
                state = state,
                onBackButtonClicked = { navController.navigate(Destinations.HOME.route) },
                onLanguageChanged = { selectedLanguage ->
                    settingsViewModel.processIntent(
                        SettingsScreenIntent.ChangeLanguage(
                            selectedLanguage
                        )
                    )
                },
                onUnitChanged = { selectedUnit ->
                    settingsViewModel.processIntent(SettingsScreenIntent.ChangeUnits(selectedUnit))
                },
                onTimeFormatChanged = { selectedFormat ->
                    settingsViewModel.processIntent(
                        SettingsScreenIntent.ChangeTimeFormat(selectedFormat)
                    )
                }
            )
        }
    }
}

enum class Destinations(val route: String) {
    HOME("home"),
    SETTINGS("settings")
}
