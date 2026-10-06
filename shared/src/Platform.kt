package com.driftgirls

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
