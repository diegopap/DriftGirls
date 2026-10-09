package com.driftgirls

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun saveGameData(data: String)
expect fun loadGameData(): String?
expect fun getFormattedCurrentDate(): String

expect fun playBackgroundMusic(musicTrack: String)
expect fun stopBackgroundMusic()
expect fun setMusicMuted(muted: Boolean)
expect fun isMusicMuted(): Boolean

@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)
