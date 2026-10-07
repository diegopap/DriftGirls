package com.driftgirls

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.driftgirls.engine.GameEngine
import com.driftgirls.ui.screens.*
import org.jetbrains.compose.reload.DevelopmentEntryPoint

enum class Screen {
    HOME,
    RACE,
    GARAGE,
    COMPANIONS,
    TRACKS,
    LEADERBOARD
}

@Composable
@Preview
@DevelopmentEntryPoint
fun App() {
    val gameEngine = remember { GameEngine() }
    var currentScreen by rememberSaveable { mutableStateOf(Screen.HOME) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0F041D)
        ) {
            Crossfade(targetState = currentScreen) { screen ->
                when (screen) {
                    Screen.HOME -> HomeScreen(
                        gameEngine = gameEngine,
                        onNavigateToRace = { currentScreen = Screen.TRACKS },
                        onNavigateToGarage = { currentScreen = Screen.GARAGE },
                        onNavigateToCompanions = { currentScreen = Screen.COMPANIONS },
                        onNavigateToTracks = { currentScreen = Screen.TRACKS },
                        onNavigateToLeaderboard = { currentScreen = Screen.LEADERBOARD }
                    )
                    Screen.RACE -> RaceScreen(
                        gameEngine = gameEngine,
                        onBackToHome = { currentScreen = Screen.HOME },
                        onGoToGarage = { currentScreen = Screen.GARAGE }
                    )
                    Screen.GARAGE -> GarageScreen(
                        gameEngine = gameEngine,
                        onBackToHome = { currentScreen = Screen.HOME },
                        onStartRace = {
                            gameEngine.startRace(gameEngine.saveData.unlockedTrackIds.firstOrNull() ?: "track_tutorial")
                            currentScreen = Screen.RACE
                        }
                    )
                    Screen.COMPANIONS -> CompanionsScreen(
                        gameEngine = gameEngine,
                        onBackToHome = { currentScreen = Screen.HOME }
                    )
                    Screen.TRACKS -> TrackSelectScreen(
                        gameEngine = gameEngine,
                        onBackToHome = { currentScreen = Screen.HOME },
                        onStartRace = { currentScreen = Screen.RACE }
                    )
                    Screen.LEADERBOARD -> LeaderboardScreen(
                        gameEngine = gameEngine,
                        onBackToHome = { currentScreen = Screen.HOME }
                    )
                }
            }
        }
    }
}
