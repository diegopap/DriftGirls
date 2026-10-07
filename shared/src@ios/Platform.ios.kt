package com.driftgirls

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun saveGameData(data: String) {}

actual fun loadGameData(): String? = null

actual fun getFormattedCurrentDate(): String = "2026-03-30 12:00"
