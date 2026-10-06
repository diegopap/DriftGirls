package com.driftgirls.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.driftgirls.model.*
import kotlin.math.max
import kotlin.math.min

object GameDataDefaults {
    val initialCars = listOf(
        Car(
            id = "trueno_ae86",
            name = "Apex Trueno AE86",
            subtitle = "The Legendary Drift Hatch",
            baseSpeed = 210,
            baseDriftControl = 85,
            baseNitroCapacity = 70,
            colorHex = 0xFFECEFF1, // Pure White / Black Accent
            price = 0,
            level = 1
        ),
        Car(
            id = "rx7_spirit",
            name = "Spirit RX-7",
            subtitle = "Rotary Engine Rocket",
            baseSpeed = 245,
            baseDriftControl = 90,
            baseNitroCapacity = 80,
            colorHex = 0xFFFF3D00, // Crimson Red
            price = 5000,
            level = 2
        ),
        Car(
            id = "gtr_horizon",
            name = "GT-R Horizon R34",
            subtitle = "All-Wheel Godzilla",
            baseSpeed = 280,
            baseDriftControl = 92,
            baseNitroCapacity = 85,
            colorHex = 0xFF29B6F6, // Midnight Blue
            price = 12000,
            level = 3
        ),
        Car(
            id = "hyper_phantom",
            name = "Hyper Phantom V12",
            subtitle = "Ultimate Cyber Hypercar",
            baseSpeed = 330,
            baseDriftControl = 98,
            baseNitroCapacity = 100,
            colorHex = 0xFFFFD700, // Gold
            price = 25000,
            level = 4
        )
    )

    val initialCompanions = listOf(
        GirlCompanion(
            id = "aoi",
            name = "Aoi",
            title = "The Turbo Specialist",
            bio = "Spunky master mechanic who loves high-rev tuning and late-night highway sprints.",
            affection = 25,
            bonusType = CompanionBonusType.NITRO_RECHARGE,
            baseBonusValue = 20.0f,
            dialogueLines = listOf(
                "Listen to that engine roar!",
                "Hit the Nitro NOW!!",
                "Kyaa~ What a smooth corner!",
                "You're driving like a legend!",
                "Hold tight, turbo's kicking in!"
            ),
            accentColorHex = 0xFFFF4081, // Pink
            favoriteGift = "Boba Milk Tea"
        ),
        GirlCompanion(
            id = "sora",
            name = "Sora",
            title = "The Drift Queen",
            bio = "Calm and composed touge racer who knows the geometry of every hairpin turn.",
            affection = 15,
            bonusType = CompanionBonusType.DRIFT_SCORE_MULTIPLIER,
            baseBonusValue = 25.0f,
            dialogueLines = listOf(
                "Keep your line steady...",
                "Perfect angle! Brilliant!",
                "Feel the tire grip breaking!",
                "Unstoppable combo!",
                "That drift was pure art."
            ),
            accentColorHex = 0xFF00E5FF, // Cyan
            favoriteGift = "Rose Bouquet"
        ),
        GirlCompanion(
            id = "elena",
            name = "Elena",
            title = "High Octane",
            bio = "Sassy street racing icon who lives for raw speed and adrenaline.",
            affection = 10,
            bonusType = CompanionBonusType.TOP_SPEED_BOOST,
            baseBonusValue = 15.0f,
            dialogueLines = listOf(
                "Step on the gas already!",
                "Woohoo!! That's what I'm talking about!",
                "Show them who owns these streets!",
                "Faster! FASTER!",
                "You actually made me gasp!"
            ),
            accentColorHex = 0xFFFF9100, // Orange
            favoriteGift = "Gem Pendant"
        ),
        GirlCompanion(
            id = "mia",
            name = "Mia",
            title = "Precision Pilot",
            bio = "Analytical racing prodigy who calculates apex trajectories down to the millimeter.",
            affection = 5,
            bonusType = CompanionBonusType.DRIFT_TIMING_WINDOW,
            baseBonusValue = 30.0f,
            dialogueLines = listOf(
                "Trajectory optimal.",
                "Precision timing executed.",
                "Drift zone locked in.",
                "Sublime execution driver.",
                "Efficiency rating 100%!"
            ),
            accentColorHex = 0xFF7C4DFF, // Purple
            favoriteGift = "Diamond Ring"
        )
    )

    val initialTracks = listOf(
        RaceTrack(
            id = "track_tutorial",
            name = "Akina Training Pass",
            location = "Mountain Hill",
            difficulty = "Beginner",
            distanceMeters = 800,
            cornersCount = 4,
            rewardCash = 500,
            rewardGems = 10,
            primaryColorHex = 0xFF4CAF50, // Green
            isTutorial = true
        ),
        RaceTrack(
            id = "track_neon_city",
            name = "Neon City Strip",
            location = "Metropolis Highway",
            difficulty = "Easy",
            distanceMeters = 1400,
            cornersCount = 6,
            rewardCash = 1200,
            rewardGems = 15,
            primaryColorHex = 0xFF00E5FF // Cyan
        ),
        RaceTrack(
            id = "track_mountain",
            name = "Dragon Hairpins",
            location = "Mount Fuji Touge",
            difficulty = "Medium",
            distanceMeters = 2000,
            cornersCount = 10,
            rewardCash = 2500,
            rewardGems = 25,
            primaryColorHex = 0xFFFF4081 // Pink
        ),
        RaceTrack(
            id = "track_ocean",
            name = "Sunset Coastal Run",
            location = "Pacific Highway",
            difficulty = "Hard",
            distanceMeters = 2800,
            cornersCount = 12,
            rewardCash = 4500,
            rewardGems = 40,
            primaryColorHex = 0xFFFFD700 // Gold
        )
    )

    val datesList = listOf(
        DateActivity(
            id = "date_cafe",
            title = "Cat Cafe Date",
            location = "Harajuku Street",
            description = "Enjoy sweet boba tea and adorable kittens in a cozy atmosphere.",
            costGems = 10,
            affectionGain = 15
        ),
        DateActivity(
            id = "date_night_drive",
            title = "Midnight Lookout Drive",
            location = "Skyline Summit",
            description = "Drive up to the mountain peak to gaze at city lights together.",
            costGems = 25,
            affectionGain = 35
        ),
        DateActivity(
            id = "date_amusement",
            title = "Ferris Wheel Night",
            location = "Cyber World Park",
            description = "Ride the romantic giant glowing ferris wheel under fireworks.",
            costGems = 50,
            affectionGain = 75
        )
    )

    val giftsList = listOf(
        GiftItem("gift_tea", "Boba Milk Tea", 5, 10, "🧋"),
        GiftItem("gift_roses", "Rose Bouquet", 15, 25, "🌹"),
        GiftItem("gift_pendant", "Gem Pendant", 30, 50, "💎"),
        GiftItem("gift_ring", "Diamond Ring", 75, 120, "💍")
    )
}

data class ActiveRaceState(
    val track: RaceTrack,
    val car: Car,
    val companion: GirlCompanion,
    var distanceTravelled: Float = 0f,
    var currentSpeed: Float = 0f,
    var nitroLevel: Float = 50f,
    var isNitroActive: Boolean = false,
    var isDriftingLeft: Boolean = false,
    var isDriftingRight: Boolean = false,
    var driftAngle: Float = 0f,
    var currentCornerIndex: Int = 0,
    var nextCornerDistance: Float = 150f,
    var isInsideCorner: Boolean = false,
    var cornerProgress: Float = 0f,
    var lastDriftRating: DriftRating? = null,
    var ratingDisplayTimer: Float = 0f,
    var driftScore: Int = 0,
    var comboMultiplier: Int = 1,
    var companionSpeech: String? = null,
    var companionSpeechTimer: Float = 0f,
    var isFinished: Boolean = false,
    var starsEarned: Int = 0,
    var earnedCash: Int = 0,
    var earnedGems: Int = 0,
    var earnedAffection: Int = 0
)

class GameEngine {
    var saveData by mutableStateOf(GameSaveData())
        private set

    var cars by mutableStateOf(GameDataDefaults.initialCars)
        private set

    var companions by mutableStateOf(GameDataDefaults.initialCompanions)
        private set

    var tracks by mutableStateOf(GameDataDefaults.initialTracks)
        private set

    var activeRaceState by mutableStateOf<ActiveRaceState?>(null)
        private set

    // Current Active Models
    val currentCar: Car
        get() = cars.find { it.id == saveData.selectedCarId } ?: cars.first()

    val currentCompanion: GirlCompanion
        get() {
            val base = companions.find { it.id == saveData.selectedCompanionId } ?: companions.first()
            val savedAffection = saveData.companionAffinities[base.id] ?: base.affection
            return base.copy(affection = savedAffection)
        }

    fun startRace(trackId: String) {
        val track = tracks.find { it.id == trackId } ?: tracks.first()
        val car = currentCar
        val companion = currentCompanion

        activeRaceState = ActiveRaceState(
            track = track,
            car = car,
            companion = companion,
            currentSpeed = 80f,
            nextCornerDistance = 200f,
            companionSpeech = "Let's win this race together! GO!!",
            companionSpeechTimer = 3.0f
        )
    }

    fun updateRace(deltaTimeSeconds: Float) {
        val race = activeRaceState ?: return
        if (race.isFinished) return

        // Speed logic
        val companionAffection = race.companion.affection.toFloat()
        val maxSpeed = race.car.effectiveTopSpeed(companionAffection).toFloat()

        var targetSpeed = if (race.isNitroActive) maxSpeed * 1.3f else maxSpeed

        if (race.isDriftingLeft || race.isDriftingRight) {
            targetSpeed *= 0.88f // Slight speed drop during drift
        }

        // Accelerate
        if (race.currentSpeed < targetSpeed) {
            race.currentSpeed += 90f * deltaTimeSeconds
        } else {
            race.currentSpeed -= 40f * deltaTimeSeconds
        }

        // Nitro consumption or recharge
        if (race.isNitroActive) {
            race.nitroLevel = max(0f, race.nitroLevel - 35f * deltaTimeSeconds)
            if (race.nitroLevel <= 0f) {
                race.isNitroActive = false
            }
        } else {
            val rechargeRate = 5f * (1f + race.companion.effectiveBonusValue() / 100f)
            race.nitroLevel = min(100f, race.nitroLevel + rechargeRate * deltaTimeSeconds)
        }

        // Distance travelled
        val metersPerSec = (race.currentSpeed * 1000f) / 3600f
        val distanceDelta = metersPerSec * deltaTimeSeconds
        race.distanceTravelled += distanceDelta

        // Corner approach logic
        race.nextCornerDistance -= distanceDelta
        if (race.nextCornerDistance <= 0f && !race.isInsideCorner) {
            race.isInsideCorner = true
            race.cornerProgress = 0f
        }

        if (race.isInsideCorner) {
            race.cornerProgress += deltaTimeSeconds * 0.8f
            if (race.cornerProgress >= 1f) {
                race.isInsideCorner = false
                race.currentCornerIndex++
                race.nextCornerDistance = 180f + (kotlin.random.Random.nextFloat() * 80f)
            }
        }

        // Drift Angle smooth update
        val targetAngle = when {
            race.isDriftingLeft -> -32f
            race.isDriftingRight -> 32f
            else -> 0f
        }
        race.driftAngle += (targetAngle - race.driftAngle) * 8f * deltaTimeSeconds

        // Timer decrements
        if (race.ratingDisplayTimer > 0f) {
            race.ratingDisplayTimer -= deltaTimeSeconds
            if (race.ratingDisplayTimer <= 0f) {
                race.lastDriftRating = null
            }
        }

        if (race.companionSpeechTimer > 0f) {
            race.companionSpeechTimer -= deltaTimeSeconds
            if (race.companionSpeechTimer <= 0f) {
                race.companionSpeech = null
            }
        }

        // Race Finish Check
        if (race.distanceTravelled >= race.track.distanceMeters) {
            finishRace()
        }
    }

    fun triggerDrift(isLeft: Boolean) {
        val race = activeRaceState ?: return
        if (race.isFinished) return

        if (isLeft) race.isDriftingLeft = true else race.isDriftingRight = true

        // Evaluate Drift Timing relative to corner progress
        val rating = if (race.isInsideCorner) {
            when (race.cornerProgress) {
                in 0.25f..0.65f -> DriftRating.PERFECT
                in 0.15f..0.80f -> DriftRating.GREAT
                in 0.05f..0.95f -> DriftRating.GOOD
                else -> DriftRating.LATE
            }
        } else if (race.nextCornerDistance < 50f) {
            DriftRating.GREAT
        } else {
            DriftRating.MISS
        }

        race.lastDriftRating = rating
        race.ratingDisplayTimer = 1.8f

        // Score & Nitro
        val baseScore = 200
        val addedScore = (baseScore * rating.scoreMultiplier * race.comboMultiplier).toInt()
        race.driftScore += addedScore

        if (rating != DriftRating.MISS) {
            race.comboMultiplier = min(10, race.comboMultiplier + 1)
            race.nitroLevel = min(100f, race.nitroLevel + rating.nitroBonus)

            // Companion Reaction
            val speech = race.companion.dialogueLines.random()
            race.companionSpeech = "${rating.label} $speech"
            race.companionSpeechTimer = 2.5f
        } else {
            race.comboMultiplier = 1
            race.companionSpeech = "Watch out for the corner!"
            race.companionSpeechTimer = 2.0f
        }
    }

    fun releaseDrift() {
        val race = activeRaceState ?: return
        race.isDriftingLeft = false
        race.isDriftingRight = false
    }

    fun activateNitro() {
        val race = activeRaceState ?: return
        if (race.nitroLevel >= 25f && !race.isNitroActive) {
            race.isNitroActive = true
            race.companionSpeech = "NITRO BOOSTER ENGAGED!!"
            race.companionSpeechTimer = 2.0f
        }
    }

    private fun finishRace() {
        val race = activeRaceState ?: return
        race.isFinished = true
        race.currentSpeed = 0f

        // Calculate rewards
        val stars = when {
            race.driftScore > 5000 -> 3
            race.driftScore > 2500 -> 2
            else -> 1
        }
        race.starsEarned = stars

        val rewardCash = race.track.rewardCash + (race.driftScore / 10)
        val rewardGems = race.track.rewardGems + (stars * 5)
        val affectionGain = 8 + (stars * 4)

        race.earnedCash = rewardCash
        race.earnedGems = rewardGems
        race.earnedAffection = affectionGain

        // Update Save Data
        val newAffinity = min(100, (saveData.companionAffinities[race.companion.id] ?: 10) + affectionGain)
        val updatedAffinities = saveData.companionAffinities.toMutableMap().apply {
            put(race.companion.id, newAffinity)
        }

        val updatedUnlockedTracks = saveData.unlockedTrackIds.toMutableSet().apply {
            add(race.track.id)
            // Unlock next tracks if finished
            tracks.forEach { add(it.id) }
        }

        saveData = saveData.copy(
            cash = saveData.cash + rewardCash,
            gems = saveData.gems + rewardGems,
            companionAffinities = updatedAffinities,
            unlockedTrackIds = updatedUnlockedTracks,
            completedTutorial = true
        )
    }

    fun selectCar(carId: String) {
        if (saveData.ownedCarIds.contains(carId)) {
            saveData = saveData.copy(selectedCarId = carId)
        }
    }

    fun buyCar(car: Car) {
        if (saveData.cash >= car.price && !saveData.ownedCarIds.contains(car.id)) {
            val newOwned = saveData.ownedCarIds + car.id
            saveData = saveData.copy(
                cash = saveData.cash - car.price,
                ownedCarIds = newOwned,
                selectedCarId = car.id
            )
        }
    }

    fun upgradeCarComponent(carId: String, component: String) {
        val car = cars.find { it.id == carId } ?: return
        val upgradeCost = 1000 * (car.engineUpgradeLevel + 1)
        if (saveData.cash >= upgradeCost) {
            val updatedCars = cars.map {
                if (it.id == carId) {
                    when (component) {
                        "ENGINE" -> it.copy(engineUpgradeLevel = it.engineUpgradeLevel + 1)
                        "TIRES" -> it.copy(tiresUpgradeLevel = it.tiresUpgradeLevel + 1)
                        "NITRO" -> it.copy(nitroUpgradeLevel = it.nitroUpgradeLevel + 1)
                        else -> it
                    }
                } else it
            }
            cars = updatedCars
            saveData = saveData.copy(cash = saveData.cash - upgradeCost)
        }
    }

    fun updateCarColor(carId: String, colorHex: Long) {
        cars = cars.map {
            if (it.id == carId) it.copy(colorHex = colorHex) else it
        }
    }

    fun selectCompanion(companionId: String) {
        saveData = saveData.copy(selectedCompanionId = companionId)
    }

    fun goOnDate(companionId: String, dateActivity: DateActivity): Boolean {
        if (saveData.gems >= dateActivity.costGems) {
            val currentAffinity = saveData.companionAffinities[companionId] ?: 10
            val newAffinity = min(100, currentAffinity + dateActivity.affectionGain)
            val updatedAffinities = saveData.companionAffinities.toMutableMap().apply {
                put(companionId, newAffinity)
            }
            saveData = saveData.copy(
                gems = saveData.gems - dateActivity.costGems,
                companionAffinities = updatedAffinities
            )
            return true
        }
        return false
    }

    fun giveGift(companionId: String, giftItem: GiftItem): Boolean {
        if (saveData.gems >= giftItem.costGems) {
            val currentAffinity = saveData.companionAffinities[companionId] ?: 10
            val newAffinity = min(100, currentAffinity + giftItem.affectionGain)
            val updatedAffinities = saveData.companionAffinities.toMutableMap().apply {
                put(companionId, newAffinity)
            }
            saveData = saveData.copy(
                gems = saveData.gems - giftItem.costGems,
                companionAffinities = updatedAffinities
            )
            return true
        }
        return false
    }
}
