package com.driftgirls

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun saveGameData(data: String)
expect fun loadGameData(): String?
expect fun getFormattedCurrentDate(): String

@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)
