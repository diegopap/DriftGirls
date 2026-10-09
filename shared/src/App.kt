package com.driftgirls

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.driftgirls.engine.GameEngine
import com.driftgirls.i18n.AppSettings
import com.driftgirls.i18n.ThemeSetting
import com.driftgirls.ui.screens.*
import org.jetbrains.compose.reload.DevelopmentEntryPoint

enum class Screen {
    HOME,
    RACE,
    GARAGE,
    COMPANIONS,
    TRACKS,
    LEADERBOARD,
    SETTINGS
}

@Composable
@Preview
@DevelopmentEntryPoint
fun App() {
    val gameEngine = remember { GameEngine() }
    val backStack = remember { mutableStateListOf(Screen.HOME) }
    val currentScreen = backStack.lastOrNull() ?: Screen.HOME

    val systemInDark = isSystemInDarkTheme()
    val isDarkTheme = when (AppSettings.themeSetting) {
        ThemeSetting.SYSTEM -> systemInDark
        ThemeSetting.DARK -> true
        ThemeSetting.LIGHT -> false
    }

    val currentTrackId = gameEngine.activeRaceState?.track?.id
    LaunchedEffect(currentScreen, currentTrackId) {
        if (currentScreen == Screen.RACE) {
            val trackMusic = "race_" + (currentTrackId ?: "track_tutorial")
            playBackgroundMusic(trackMusic)
        } else {
            playBackgroundMusic("menu")
        }
    }

    PlatformBackHandler(enabled = backStack.size > 1) {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        }
    }

    val navigateTo: (Screen) -> Unit = { screen ->
        if (screen == Screen.HOME) {
            backStack.clear()
            backStack.add(Screen.HOME)
        } else {
            if (backStack.lastOrNull() != screen) {
                backStack.add(screen)
            }
        }
    }

    val goBack: () -> Unit = {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        } else {
            backStack.clear()
            backStack.add(Screen.HOME)
        }
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = if (isDarkTheme) Color(0xFF0F041D) else Color(0xFFF5F0FF)
        ) {
            Crossfade(targetState = currentScreen) { screen ->
                when (screen) {
                    Screen.HOME -> HomeScreen(
                        gameEngine = gameEngine,
                        onNavigateToRace = { navigateTo(Screen.TRACKS) },
                        onNavigateToGarage = { navigateTo(Screen.GARAGE) },
                        onNavigateToCompanions = { navigateTo(Screen.COMPANIONS) },
                        onNavigateToTracks = { navigateTo(Screen.TRACKS) },
                        onNavigateToLeaderboard = { navigateTo(Screen.LEADERBOARD) },
                        onNavigateToSettings = { navigateTo(Screen.SETTINGS) },
                        isDarkTheme = isDarkTheme
                    )
                    Screen.RACE -> RaceScreen(
                        gameEngine = gameEngine,
                        onBackToHome = goBack,
                        onGoToGarage = {
                            if (!backStack.contains(Screen.GARAGE)) {
                                backStack.add(Screen.GARAGE)
                            } else {
                                while (backStack.lastOrNull() != Screen.GARAGE && backStack.size > 1) {
                                    backStack.removeAt(backStack.size - 1)
                                }
                            }
                        }
                    )
                    Screen.GARAGE -> GarageScreen(
                        gameEngine = gameEngine,
                        onBackToHome = goBack,
                        onStartRace = {
                            gameEngine.startRace(gameEngine.saveData.unlockedTrackIds.firstOrNull() ?: "track_tutorial")
                            navigateTo(Screen.RACE)
                        }
                    )
                    Screen.COMPANIONS -> CompanionsScreen(
                        gameEngine = gameEngine,
                        onBackToHome = goBack
                    )
                    Screen.TRACKS -> TrackSelectScreen(
                        gameEngine = gameEngine,
                        onBackToHome = goBack,
                        onStartRace = { navigateTo(Screen.RACE) }
                    )
                    Screen.LEADERBOARD -> LeaderboardScreen(
                        gameEngine = gameEngine,
                        onBackToHome = goBack
                    )
                    Screen.SETTINGS -> SettingsScreen(
                        gameEngine = gameEngine,
                        onBackToHome = goBack,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }
    }
}
