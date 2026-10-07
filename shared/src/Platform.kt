package com.driftgirls

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun saveGameData(data: String)
expect fun loadGameData(): String?
expect fun getFormattedCurrentDate(): String
