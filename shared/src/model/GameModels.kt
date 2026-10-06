package com.driftgirls.model

enum class DriftRating(
    val label: String,
    val scoreMultiplier: Float,
    val nitroBonus: Float,
    val colorHex: Long
) {
    PERFECT("PERFECT!!", 2.5f, 30f, 0xFFFF007F), // Neon Pink
    GREAT("GREAT!", 1.8f, 20f, 0xFF00E5FF),    // Neon Cyan
    GOOD("GOOD", 1.2f, 10f, 0xFF76FF03),       // Neon Green
    LATE("LATE", 0.8f, 5f, 0xFFFFC107),        // Amber
    MISS("MISS", 0.2f, 0f, 0xFFFF3D00)         // Orange Red
}

enum class RacePhase {
    COUNTDOWN,
    RACING,
    FINISHED
}

enum class CornerDirection {
    LEFT,
    RIGHT
}

enum class CornerLength(val label: String, val progressRate: Float) {
    SHORT("SHARP CHICANE", 1.30f),
    MEDIUM("STANDARD HAIRPIN", 0.75f),
    LONG("SWEEPING ARC", 0.45f),
    ULTRA_LONG("ENDURANCE BEND", 0.30f)
}

enum class CompanionBonusType(val description: String) {
    NITRO_RECHARGE("Faster Nitro Recharge"),
    DRIFT_SCORE_MULTIPLIER("Bonus Drift Score Multiplier"),
    TOP_SPEED_BOOST("Top Speed Boost"),
    DRIFT_TIMING_WINDOW("Wider Perfect Drift Window")
}

data class Car(
    val id: String,
    val name: String,
    val subtitle: String,
    val baseSpeed: Int,          // e.g. 220 km/h
    val baseDriftControl: Int,   // 1..100
    val baseNitroCapacity: Int,  // 1..100
    val colorHex: Long,
    val price: Int,
    val level: Int = 1,
    val engineUpgradeLevel: Int = 0,
    val tiresUpgradeLevel: Int = 0,
    val nitroUpgradeLevel: Int = 0
) {
    fun effectiveTopSpeed(companionAffectionBonus: Float = 0f): Int {
        val upgradeBonus = engineUpgradeLevel * 12
        val affectionBonus = (baseSpeed * (companionAffectionBonus / 100f)).toInt()
        return baseSpeed + upgradeBonus + affectionBonus
    }

    fun effectiveDriftControl(companionAffectionBonus: Float = 0f): Int {
        val upgradeBonus = tiresUpgradeLevel * 8
        val affectionBonus = (companionAffectionBonus * 0.2f).toInt()
        return (baseDriftControl + upgradeBonus + affectionBonus).coerceAtMost(100)
    }

    fun effectiveNitroCapacity(companionAffectionBonus: Float = 0f): Int {
        val upgradeBonus = nitroUpgradeLevel * 10
        val affectionBonus = (companionAffectionBonus * 0.25f).toInt()
        return (baseNitroCapacity + upgradeBonus + affectionBonus).coerceAtMost(100)
    }
}

data class GirlCompanion(
    val id: String,
    val name: String,
    val title: String,
    val bio: String,
    val affection: Int = 10,     // 0..100
    val maxAffinity: Int = 100,
    val bonusType: CompanionBonusType,
    val baseBonusValue: Float,    // percentage e.g. 15.0f
    val dialogueLines: List<String>,
    val accentColorHex: Long,
    val favoriteGift: String
) {
    fun effectiveBonusValue(): Float {
        // Higher affection scales the bonus up to 2x at 100 affection!
        val affectionScaler = 1.0f + (affection.toFloat() / maxAffinity.toFloat())
        return baseBonusValue * affectionScaler
    }
}

data class DateActivity(
    val id: String,
    val title: String,
    val location: String,
    val description: String,
    val costGems: Int,
    val affectionGain: Int
)

data class GiftItem(
    val id: String,
    val name: String,
    val costGems: Int,
    val affectionGain: Int,
    val iconSymbol: String
)

data class RaceTrack(
    val id: String,
    val name: String,
    val location: String,
    val difficulty: String,
    val distanceMeters: Int,
    val cornersCount: Int,
    val rewardCash: Int,
    val rewardGems: Int,
    val primaryColorHex: Long,
    val isTutorial: Boolean = false
)

data class GameSaveData(
    val cash: Int = 2500,
    val gems: Int = 50,
    val selectedCarId: String = "trueno_ae86",
    val selectedCompanionId: String = "aoi",
    val ownedCarIds: Set<String> = setOf("trueno_ae86"),
    val unlockedTrackIds: Set<String> = setOf("track_tutorial", "track_neon_city"),
    val companionAffinities: Map<String, Int> = mapOf("aoi" to 20, "sora" to 10, "elena" to 5, "mia" to 0),
    val completedTutorial: Boolean = false
)
