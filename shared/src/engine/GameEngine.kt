package com.driftgirls.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.driftgirls.i18n.*
import com.driftgirls.loadGameData
import com.driftgirls.model.*
import com.driftgirls.saveGameData
import kotlin.math.max
import kotlin.math.min

private data class CarCustomData(
    val engineLvl: Int,
    val tiresLvl: Int,
    val nitroLvl: Int,
    val colorHex: Long
)

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
            distanceMeters = 1400,
            cornersCount = 6,
            rewardCash = 650,
            rewardGems = 12,
            primaryColorHex = 0xFF4CAF50, // Green
            isTutorial = true,
            discoverableGirlId = "aoi"
        ),
        RaceTrack(
            id = "track_neon_city",
            name = "Neon City Strip",
            location = "Metropolis Highway",
            difficulty = "Easy",
            distanceMeters = 2000,
            cornersCount = 8,
            rewardCash = 1500,
            rewardGems = 20,
            primaryColorHex = 0xFF00E5FF, // Cyan
            discoverableGirlId = "sora"
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
            primaryColorHex = 0xFFFF4081, // Pink
            discoverableGirlId = "elena"
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
            primaryColorHex = 0xFFFFD700, // Gold
            discoverableGirlId = "mia"
        ),
        RaceTrack(
            id = "track_endless",
            name = "Infinity Drift Pass",
            location = "Celestial Summit",
            difficulty = "Endless (60s)",
            distanceMeters = 999999,
            cornersCount = 99,
            rewardCash = 0,
            rewardGems = 0,
            primaryColorHex = 0xFFFFD700, // Gold
            isEndless = true,
            timeLimitSeconds = 60f
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

class ActiveRaceState(
    val track: RaceTrack,
    val car: Car,
    val companion: GirlCompanion?,
    initialRacePhase: RacePhase = RacePhase.COUNTDOWN,
    initialCountdownTimer: Float = 3.0f,
    initialEngineRpm: Float = 1000f,
    initialCurrentSpeed: Float = 0f,
    initialNextCornerDistance: Float = 220f,
    initialCompanionSpeech: String? = null,
    initialCompanionSpeechTimer: Float = 0f
) {
    var racePhase by mutableStateOf(initialRacePhase)
    var countdownTimer by mutableStateOf(initialCountdownTimer)
    var engineRpm by mutableStateOf(initialEngineRpm)
    var isAccelerating by mutableStateOf(false)
    var launchRating by mutableStateOf<DriftRating?>(null)
    var distanceTravelled by mutableStateOf(0f)
    var currentSpeed by mutableStateOf(initialCurrentSpeed)
    var nitroLevel by mutableStateOf(50f)
    var isNitroActive by mutableStateOf(false)
    var isDriftingLeft by mutableStateOf(false)
    var isDriftingRight by mutableStateOf(false)
    var driftAngle by mutableStateOf(0f)
    var currentCornerIndex by mutableStateOf(0)
    var currentCornerDirection by mutableStateOf(CornerDirection.entries.random())
    var currentCornerLength by mutableStateOf(CornerLength.entries.random())
    var nextCornerDistance by mutableStateOf(initialNextCornerDistance)
    var isInsideCorner by mutableStateOf(false)
    var cornerProgress by mutableStateOf(0f)
    var entryDriftRating by mutableStateOf<DriftRating?>(null)
    var exitDriftRating by mutableStateOf<DriftRating?>(null)
    var hasAttemptedEntry by mutableStateOf(false)
    var hasAttemptedExit by mutableStateOf(false)
    var lastDriftRating by mutableStateOf<DriftRating?>(null)
    var driftDisplayBanner by mutableStateOf<String?>(null)
    var ratingDisplayTimer by mutableStateOf(0f)
    var driftScore by mutableStateOf(0)
    var comboMultiplier by mutableStateOf(1)
    var companionSpeech by mutableStateOf<String?>(initialCompanionSpeech)
    var companionSpeechTimer by mutableStateOf(initialCompanionSpeechTimer)
    var isFinished by mutableStateOf(false)
    var starsEarned by mutableStateOf(0)
    var earnedCash by mutableStateOf(0)
    var earnedGems by mutableStateOf(0)
    var earnedAffection by mutableStateOf(0)
    var timeRemainingSeconds by mutableStateOf<Float?>(track.timeLimitSeconds)
    var newlyDiscoveredCompanion by mutableStateOf<GirlCompanion?>(null)
    var finishStraightTargetDistance by mutableStateOf<Float?>(null)

    val isLastCorner: Boolean
        get() = !track.isEndless && currentCornerIndex == track.cornersCount - 1
}

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

    init {
        loadSaveFromDisk()
    }

    private fun persistSaveToDisk() {
        try {
            val carCustoms = cars.joinToString(";") {
                "${it.id}:${it.engineUpgradeLevel},${it.tiresUpgradeLevel},${it.nitroUpgradeLevel},${it.colorHex}"
            }
            val affinities = saveData.companionAffinities.entries.joinToString(";") {
                "${it.key}:${it.value}"
            }
            val leaderboardPayload = saveData.leaderboardEntries.joinToString(";") {
                "${it.id}#${it.trackName}#${it.driftScore}#${it.dateString}#${it.carName}#${it.companionName ?: ""}"
            }
            val payload = listOf(
                saveData.cash.toString(),
                saveData.gems.toString(),
                saveData.selectedCarId,
                saveData.selectedCompanionId ?: "",
                saveData.ownedCarIds.joinToString(","),
                saveData.unlockedTrackIds.joinToString(","),
                affinities,
                if (saveData.completedTutorial) "1" else "0",
                carCustoms,
                saveData.discoveredCompanionIds.joinToString(","),
                if (saveData.isGameCompleted) "1" else "0",
                leaderboardPayload,
                saveData.languageSetting,
                saveData.themeSetting,
                saveData.musicVolume.toString(),
                if (saveData.isMuted) "1" else "0"
            ).joinToString("||")

            saveGameData(payload)
        } catch (e: Exception) {
            // Fail-safe
        }
    }

    private fun loadSaveFromDisk() {
        val raw = loadGameData() ?: return
        try {
            val parts = raw.split("||")
            if (parts.size >= 8) {
                val cash = parts[0].toIntOrNull() ?: 2500
                val gems = parts[1].toIntOrNull() ?: 50
                val selectedCarId = parts[2]
                val rawSelectedComp = parts[3]
                val selectedCompanionId = if (rawSelectedComp.isNotBlank() && rawSelectedComp != "null") rawSelectedComp else null
                val ownedCarIds = parts[4].split(",").filter { it.isNotBlank() }.toSet()

                val companionAffinities = parts[6].split(";").filter { it.contains(":") }.associate {
                    val pair = it.split(":")
                    pair[0] to (pair[1].toIntOrNull() ?: 10)
                }
                val completedTutorial = parts[7].toIntOrNull() == 1

                val carCustomsMap = if (parts.size >= 9) {
                    parts[8].split(";").filter { it.contains(":") }.associate {
                        val p = it.split(":")
                        val carId = p[0]
                        val values = p[1].split(",")
                        val engineLvl = values.getOrNull(0)?.toIntOrNull() ?: 0
                        val tiresLvl = values.getOrNull(1)?.toIntOrNull() ?: 0
                        val nitroLvl = values.getOrNull(2)?.toIntOrNull() ?: 0
                        val colorHex = values.getOrNull(3)?.toLongOrNull() ?: 0xFFECEFF1
                        carId to CarCustomData(engineLvl, tiresLvl, nitroLvl, colorHex)
                    }
                } else emptyMap()

                val discoveredCompanionIds = if (parts.size >= 10) {
                    parts[9].split(",").filter { it.isNotBlank() }.toSet()
                } else emptySet()

                val isGameCompleted = if (parts.size >= 11) {
                    parts[10].toIntOrNull() == 1
                } else false

                val leaderboardEntries = if (parts.size >= 12) {
                    parts[11].split(";").filter { it.contains("#") }.mapNotNull {
                        val p = it.split("#")
                        if (p.size >= 5) {
                            LeaderboardEntry(
                                id = p[0],
                                trackName = p[1],
                                driftScore = p[2].toIntOrNull() ?: 0,
                                dateString = p[3],
                                carName = p[4],
                                companionName = p.getOrNull(5)?.ifBlank { null }
                            )
                        } else null
                    }.sortedWith(
                        compareByDescending<LeaderboardEntry> { it.driftScore }
                            .thenByDescending { it.dateString }
                    )
                } else emptyList()

                val langSettingStr = if (parts.size >= 13) parts[12].ifBlank { "SYSTEM" } else "SYSTEM"
                val themeSettingStr = if (parts.size >= 14) parts[13].ifBlank { "SYSTEM" } else "SYSTEM"
                val musicVol = if (parts.size >= 15) parts[14].toFloatOrNull() ?: 1.0f else 1.0f
                val isMuted = if (parts.size >= 16) parts[15] == "1" else false

                val langSetting = try { LanguageSetting.valueOf(langSettingStr.uppercase()) } catch (e: Exception) { LanguageSetting.SYSTEM }
                val themeSetting = try { ThemeSetting.valueOf(themeSettingStr.uppercase()) } catch (e: Exception) { ThemeSetting.SYSTEM }

                AppSettings.languageSetting = langSetting
                com.driftgirls.applyLanguageSetting(langSetting)
                AppSettings.themeSetting = themeSetting
                AppSettings.musicVolume = musicVol
                AppSettings.isMuted = isMuted
                com.driftgirls.setMusicMuted(isMuted)
                com.driftgirls.setMusicVolume(musicVol)

                // Strictly sanitize unlocked tracks based on discovered companions
                val sanitizedUnlockedTracks = mutableSetOf("track_tutorial")
                if (discoveredCompanionIds.contains("aoi")) sanitizedUnlockedTracks.add("track_neon_city")
                if (discoveredCompanionIds.contains("sora")) sanitizedUnlockedTracks.add("track_mountain")
                if (discoveredCompanionIds.contains("elena")) sanitizedUnlockedTracks.add("track_ocean")
                if (isGameCompleted) sanitizedUnlockedTracks.add("track_endless")

                // Strictly sanitize selected companion
                val sanitizedSelectedCompanion = if (selectedCompanionId != null && discoveredCompanionIds.contains(selectedCompanionId)) {
                    selectedCompanionId
                } else {
                    discoveredCompanionIds.firstOrNull()
                }

                saveData = GameSaveData(
                    cash = cash,
                    gems = gems,
                    selectedCarId = if (selectedCarId.isNotBlank()) selectedCarId else "trueno_ae86",
                    selectedCompanionId = sanitizedSelectedCompanion,
                    ownedCarIds = if (ownedCarIds.isEmpty()) setOf("trueno_ae86") else ownedCarIds,
                    unlockedTrackIds = sanitizedUnlockedTracks,
                    discoveredCompanionIds = discoveredCompanionIds,
                    companionAffinities = companionAffinities,
                    completedTutorial = completedTutorial,
                    isGameCompleted = isGameCompleted,
                    leaderboardEntries = leaderboardEntries,
                    languageSetting = langSetting.name,
                    themeSetting = themeSetting.name,
                    musicVolume = musicVol,
                    isMuted = isMuted
                )

                cars = cars.map { car ->
                    val custom = carCustomsMap[car.id]
                    if (custom != null) {
                        car.copy(
                            engineUpgradeLevel = custom.engineLvl,
                            tiresUpgradeLevel = custom.tiresLvl,
                            nitroUpgradeLevel = custom.nitroLvl,
                            colorHex = custom.colorHex
                        )
                    } else car
                }
            }
        } catch (e: Exception) {
            // Graceful fallback to initial state on corrupted or invalid save format
        }
    }

    // Current Active Models
    val currentCar: Car
        get() = cars.find { it.id == saveData.selectedCarId } ?: cars.first()

    val currentCompanion: GirlCompanion?
        get() {
            val selectedId = saveData.selectedCompanionId ?: return null
            if (!saveData.discoveredCompanionIds.contains(selectedId)) return null
            val base = companions.find { it.id == selectedId } ?: return null
            val savedAffection = saveData.companionAffinities[base.id] ?: base.affection
            return base.copy(affection = savedAffection)
        }

    fun startRace(trackId: String) {
        val track = tracks.find { it.id == trackId } ?: tracks.first()
        val car = currentCar
        val companion = currentCompanion

        val initialSpeech = if (companion != null) {
            "Hold ACCELERATE to rev RPM into the green sweet spot for a PERFECT LAUNCH!"
        } else {
            "Rev RPM into the green sweet spot for a PERFECT LAUNCH!"
        }

        activeRaceState = ActiveRaceState(
            track = track,
            car = car,
            companion = companion,
            initialRacePhase = RacePhase.COUNTDOWN,
            initialCountdownTimer = 3.0f,
            initialEngineRpm = 1000f,
            initialCurrentSpeed = 0f,
            initialNextCornerDistance = 220f,
            initialCompanionSpeech = initialSpeech,
            initialCompanionSpeechTimer = 5.0f
        )
    }

    fun setAccelerating(accelerating: Boolean) {
        activeRaceState?.isAccelerating = accelerating
    }

    fun updateRace(deltaTimeSeconds: Float) {
        val race = activeRaceState ?: return
        if (race.isFinished) return

        if (race.racePhase == RacePhase.COUNTDOWN) {
            // Update 5-second launch countdown & engine RPM
            race.countdownTimer -= deltaTimeSeconds

            // Rev Engine RPM when holding accelerator
            if (race.isAccelerating) {
                race.engineRpm = min(9000f, race.engineRpm + 6500f * deltaTimeSeconds)
            } else {
                race.engineRpm = max(1000f, race.engineRpm - 4500f * deltaTimeSeconds)
            }

            // Speech timer
            if (race.companionSpeechTimer > 0f) {
                race.companionSpeechTimer -= deltaTimeSeconds
                if (race.companionSpeechTimer <= 0f) {
                    race.companionSpeech = null
                }
            }

            // When countdown reaches 0: Launch!
            if (race.countdownTimer <= 0f) {
                evaluateStartLaunch()
            }
            return
        }

        // --- RACING PHASE ---
        val companionAffection = race.companion?.affection?.toFloat() ?: 0f
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
            val bonusValue = race.companion?.effectiveBonusValue() ?: 0f
            val rechargeRate = 5f * (1f + bonusValue / 100f)
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
            race.entryDriftRating = null
            race.exitDriftRating = null
        }

        if (race.isInsideCorner) {
            val speedFactor = (race.currentSpeed / 200f).coerceIn(0.6f, 1.8f)
            race.cornerProgress += deltaTimeSeconds * race.currentCornerLength.progressRate * speedFactor

            // Auto-evaluate LATE EXIT if player holds button past corner exit (progress >= 1.00f)
            if (race.cornerProgress >= 1.00f && race.hasAttemptedEntry && !race.hasAttemptedExit) {
                evaluateExit(race, forcedMiss = true)
            }

            if (race.cornerProgress >= 1f) {
                // If corner completes and player never pressed any button, evaluate a CORNER MISSED rating!
                if (!race.hasAttemptedEntry) {
                    race.entryDriftRating = DriftRating.MISS
                    race.exitDriftRating = DriftRating.MISS
                    race.lastDriftRating = DriftRating.MISS
                    race.comboMultiplier = 1
                    race.driftDisplayBanner = "CORNER MISSED! (MISS)"
                    race.ratingDisplayTimer = 2.0f
                    if (race.companion != null) {
                        race.companionSpeech = "Aww! You missed the turn completely!"
                        race.companionSpeechTimer = 2.5f
                    }
                } else if (!race.hasAttemptedExit) {
                    evaluateExit(race, forcedMiss = true)
                }

                race.isInsideCorner = false
                race.currentCornerIndex++

                if (!race.track.isEndless && race.currentCornerIndex >= race.track.cornersCount) {
                    // Do NOT generate any more corners after the last corner!
                    race.nextCornerDistance = 999999f
                    race.finishStraightTargetDistance = maxOf(
                        race.track.distanceMeters.toFloat(),
                        race.distanceTravelled + 150f
                    )
                } else {
                    race.nextCornerDistance = 160f + (kotlin.random.Random.nextFloat() * 100f)
                    race.currentCornerDirection = CornerDirection.entries.random()
                    race.currentCornerLength = CornerLength.entries.random()
                }

                // Reset drift attempt flags for the next corner
                race.cornerProgress = 0f
                race.hasAttemptedEntry = false
                race.hasAttemptedExit = false
                race.entryDriftRating = null
                race.exitDriftRating = null
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
                race.driftDisplayBanner = null
            }
        }

        if (race.companionSpeechTimer > 0f) {
            race.companionSpeechTimer -= deltaTimeSeconds
            if (race.companionSpeechTimer <= 0f) {
                race.companionSpeech = null
            }
        }

        // Time Limit Countdown for Endless / Timed tracks
        if (race.timeRemainingSeconds != null) {
            val remaining = (race.timeRemainingSeconds!! - deltaTimeSeconds).coerceAtLeast(0f)
            race.timeRemainingSeconds = remaining
            if (remaining <= 0f) {
                finishRace()
                return
            }
        }

        // Race Finish Check (for distance-based non-endless tracks)
        if (!race.track.isEndless) {
            val isAllCornersDone = race.currentCornerIndex >= race.track.cornersCount
            val targetDist = race.finishStraightTargetDistance ?: race.track.distanceMeters.toFloat()
            val isTargetReached = race.distanceTravelled >= targetDist

            if (isAllCornersDone && !race.isInsideCorner && isTargetReached) {
                finishRace()
            }
        }
    }

    private fun evaluateStartLaunch() {
        val race = activeRaceState ?: return
        race.racePhase = RacePhase.RACING
        
        val companionAffection = race.companion?.affection?.toFloat() ?: 0f
        val maxSpeed = race.car.effectiveTopSpeed(companionAffection).toFloat()

        // Target sweet spot: 6400 - 7300 RPM
        val rpm = race.engineRpm
        val rating = when (rpm) {
            in 6400f..7300f -> DriftRating.PERFECT
            in 5800f..7800f -> DriftRating.GREAT
            in 4800f..8300f -> DriftRating.GOOD
            in 3500f..8700f -> DriftRating.LATE
            else -> DriftRating.MISS
        }

        race.launchRating = rating
        race.lastDriftRating = rating
        race.driftDisplayBanner = "${rating.label} LAUNCH!"
        race.ratingDisplayTimer = 2.5f

        when (rating) {
            DriftRating.PERFECT -> {
                race.currentSpeed = maxSpeed * 0.7f
                race.driftScore += 1500
                race.nitroLevel = min(100f, race.nitroLevel + 50f)
                if (race.companion != null) race.companionSpeech = "PERFECT LAUNCH!! ROCKET START!!"
            }
            DriftRating.GREAT -> {
                race.currentSpeed = maxSpeed * 0.55f
                race.driftScore += 1000
                race.nitroLevel = min(100f, race.nitroLevel + 30f)
                if (race.companion != null) race.companionSpeech = "GREAT START!! Let's go!!"
            }
            DriftRating.GOOD -> {
                race.currentSpeed = maxSpeed * 0.4f
                race.driftScore += 500
                race.nitroLevel = min(100f, race.nitroLevel + 15f)
                if (race.companion != null) race.companionSpeech = "Good start! Accelerating!"
            }
            DriftRating.LATE -> {
                race.currentSpeed = maxSpeed * 0.25f
                if (race.companion != null) race.companionSpeech = "Late start! Pick up speed!"
            }
            DriftRating.MISS -> {
                race.currentSpeed = 30f
                if (race.companion != null) race.companionSpeech = "Wheelspin / Engine stall!! Recover!!"
            }
        }
        race.companionSpeechTimer = 3.0f
    }

    fun triggerDrift(isLeft: Boolean) {
        val race = activeRaceState ?: return
        if (race.isFinished) return

        if (isLeft) race.isDriftingLeft = true else race.isDriftingRight = true

        // Strict 1-attempt rule per corner: ignore secondary presses during the same corner!
        if (race.hasAttemptedEntry) return
        race.hasAttemptedEntry = true

        val isCorrectDirection = if (race.currentCornerDirection == CornerDirection.LEFT) isLeft else !isLeft

        // Corner Entry Timing Evaluation
        val rating = if (!isCorrectDirection) {
            DriftRating.MISS
        } else if (race.isInsideCorner) {
            when (race.cornerProgress) {
                in 0.10f..0.35f -> DriftRating.PERFECT
                in 0.05f..0.50f -> DriftRating.GREAT
                in 0.00f..0.65f -> DriftRating.GOOD
                else -> DriftRating.MISS
            }
        } else if (race.nextCornerDistance < 80f) {
            DriftRating.GREAT
        } else {
            DriftRating.MISS
        }

        race.entryDriftRating = rating
        race.lastDriftRating = rating
        val isEs = com.driftgirls.i18n.AppLanguage.currentLanguage == com.driftgirls.i18n.Language.ES
        val localizedRatingLabel = rating.localizedLabel()

        race.driftDisplayBanner = if (rating == DriftRating.MISS) {
            if (!isCorrectDirection) {
                if (isEs) "¡DIRECCIÓN INCORRECTA! (FALLO)" else "WRONG DIRECTION! (MISS)"
            } else {
                if (isEs) "¡ENTRADA FALLIDA!" else "ENTRY MISS!"
            }
        } else {
            if (isEs) "¡ENTRADA $localizedRatingLabel!" else "$localizedRatingLabel ENTRY!"
        }
        race.ratingDisplayTimer = 2.0f

        if (rating != DriftRating.MISS) {
            val baseScore = 200
            val addedScore = (baseScore * rating.scoreMultiplier * race.comboMultiplier).toInt()
            race.driftScore += addedScore

            race.comboMultiplier = min(10, race.comboMultiplier + 1)
            race.nitroLevel = min(100f, race.nitroLevel + rating.nitroBonus)

            if (race.companion != null) {
                val speech = race.companion.localizedDialogueLines().random()
                race.companionSpeech = if (isEs) "¡ENTRADA $localizedRatingLabel! $speech" else "$localizedRatingLabel ENTRY! $speech"
            }
        } else {
            race.comboMultiplier = 1
            if (race.companion != null) {
                race.companionSpeech = if (!isCorrectDirection) {
                    if (isEs) "¡Dirección incorrecta! ¡Gira hacia el otro lado!" else "Wrong turn direction! Steer the other way!"
                } else {
                    if (isEs) "¡Punto de entrada no alcanzado!" else "Missed entry point!"
                }
            }
        }
        race.companionSpeechTimer = 2.5f
    }

    fun releaseDrift() {
        val race = activeRaceState ?: return

        race.isDriftingLeft = false
        race.isDriftingRight = false

        // Evaluate exit IF entry was attempted and exit hasn't been evaluated yet
        if (race.hasAttemptedEntry && !race.hasAttemptedExit) {
            evaluateExit(race)
        }
    }

    private fun evaluateExit(race: ActiveRaceState, forcedMiss: Boolean = false) {
        if (race.hasAttemptedExit) return
        race.hasAttemptedExit = true

        val progress = race.cornerProgress
        val exitRating = if (forcedMiss) {
            DriftRating.MISS
        } else {
            when {
                progress < 0.55f -> DriftRating.MISS              // Released TOO EARLY before exit!
                progress in 0.55f..0.72f -> DriftRating.GOOD
                progress in 0.72f..0.85f -> DriftRating.GREAT
                progress in 0.85f..1.12f -> DriftRating.PERFECT   // Releasing right at corner exit window
                progress in 1.12f..1.22f -> DriftRating.GREAT     // Slightly late exit
                else -> DriftRating.MISS                          // Held TOO LONG after exit (> 1.22f)!
            }
        }

        race.exitDriftRating = exitRating
        race.lastDriftRating = exitRating
        val isEs = com.driftgirls.i18n.AppLanguage.currentLanguage == com.driftgirls.i18n.Language.ES

        if (exitRating == DriftRating.MISS) {
            race.comboMultiplier = 1
            val missMsg = if (isEs) {
                if (progress < 0.55f) "¡SALIDA ANTICIPADA! (FALLO)" else "¡SALIDA TARDÍA! (FALLO)"
            } else {
                if (progress < 0.55f) "EARLY EXIT! (MISS)" else "LATE EXIT! (MISS)"
            }
            race.driftDisplayBanner = missMsg
            if (race.companion != null) {
                race.companionSpeech = if (isEs) {
                    if (progress < 0.55f) "¡Soltaste demasiado pronto!" else "¡Mantuviste demasiado tiempo tras la curva!"
                } else {
                    if (progress < 0.55f) "Released too early!" else "Held too long past corner!"
                }
            }
        } else if (race.entryDriftRating == DriftRating.PERFECT && exitRating == DriftRating.PERFECT) {
            race.exitDriftRating = exitRating
            race.lastDriftRating = exitRating
            race.driftScore += 2500
            race.nitroLevel = min(100f, race.nitroLevel + 50f)
            race.comboMultiplier = min(10, race.comboMultiplier + 2)
            val doublePerfBanner = if (isEs) "¡¡DERRAPE DOBLE PERFECTO!! 🚀" else "DOUBLE PERFECT DRIFT!! 🚀"
            race.driftDisplayBanner = doublePerfBanner
            if (race.companion != null) {
                race.companionSpeech = if (isEs) "¡¡DERRAPE DOBLE PERFECTO!! ¡¡MAGNÍFICO!! 🚀" else "DOUBLE PERFECT DRIFT!! MAGNIFICENT!! 🚀"
            }
        } else {
            race.exitDriftRating = exitRating
            race.lastDriftRating = exitRating
            race.driftScore += (600 * exitRating.scoreMultiplier).toInt()
            val localizedLabel = exitRating.localizedLabel()
            race.driftDisplayBanner = if (isEs) "¡SALIDA $localizedLabel!" else "$localizedLabel EXIT!"
            if (race.companion != null) {
                race.companionSpeech = if (isEs) "¡¡SALIDA $localizedLabel!! ¡Gran control!" else "$localizedLabel EXIT!! Great control!"
            }
        }

        race.ratingDisplayTimer = 2.2f
        race.companionSpeechTimer = 2.5f
    }

    fun activateNitro() {
        val race = activeRaceState ?: return
        if (race.nitroLevel >= 25f && !race.isNitroActive) {
            race.isNitroActive = true
            if (race.companion != null) {
                val isEs = com.driftgirls.i18n.AppLanguage.currentLanguage == com.driftgirls.i18n.Language.ES
                race.companionSpeech = if (isEs) "¡¡PROPULSOR NITRO ACTIVADO!!" else "NITRO BOOSTER ENGAGED!!"
                race.companionSpeechTimer = 2.0f
            }
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

        val rewardCash = if (race.track.isEndless) 0 else race.track.rewardCash + (race.driftScore / 10)
        val rewardGems = if (race.track.isEndless) 0 else race.track.rewardGems + (stars * 5)
        val affectionGain = if (race.companion != null && !race.track.isEndless) 8 + (stars * 4) else 0

        race.earnedCash = rewardCash
        race.earnedGems = rewardGems
        race.earnedAffection = affectionGain

        // Update Save Data
        val updatedAffinities = saveData.companionAffinities.toMutableMap()
        race.companion?.let { comp ->
            val newAffinity = min(100, (updatedAffinities[comp.id] ?: comp.affection) + affectionGain)
            updatedAffinities[comp.id] = newAffinity
        }

        // Track unlock logic: next race unlocks ONLY IF the girl for the current race is discovered!
        val updatedUnlockedTracks = saveData.unlockedTrackIds.toMutableSet().apply {
            add(race.track.id)
        }

        // 50% chance to discover new girl ONLY IF 3 stars earned!
        var newlyDiscovered: GirlCompanion? = null
        val discoverableGirlId = race.track.discoverableGirlId
        val updatedDiscovered = saveData.discoveredCompanionIds.toMutableSet()
        var updatedSelectedCompanionId = saveData.selectedCompanionId

        if (stars == 3 && discoverableGirlId != null && !updatedDiscovered.contains(discoverableGirlId)) {
            val chanceSuccess = kotlin.random.Random.nextFloat() < 0.5f
            if (chanceSuccess) {
                updatedDiscovered.add(discoverableGirlId)
                val girlObj = companions.find { it.id == discoverableGirlId }
                if (girlObj != null) {
                    newlyDiscovered = girlObj
                    if (!updatedAffinities.containsKey(discoverableGirlId)) {
                        updatedAffinities[discoverableGirlId] = 10
                    }
                    if (updatedSelectedCompanionId.isNullOrEmpty()) {
                        updatedSelectedCompanionId = discoverableGirlId
                    }
                }
            }
        }

        // Unlock next race based on discovered girls:
        if (updatedDiscovered.contains("aoi")) updatedUnlockedTracks.add("track_neon_city")
        if (updatedDiscovered.contains("sora")) updatedUnlockedTracks.add("track_mountain")
        if (updatedDiscovered.contains("elena")) updatedUnlockedTracks.add("track_ocean")

        // Add to Leaderboard sorted by Drift Score & Date
        val newLbEntry = LeaderboardEntry(
            id = "lb_${kotlin.random.Random.nextInt(100000, 999999)}",
            trackName = race.track.name,
            driftScore = race.driftScore,
            dateString = com.driftgirls.getFormattedCurrentDate(),
            carName = race.car.name,
            companionName = race.companion?.name
        )

        val updatedLeaderboard = (saveData.leaderboardEntries + newLbEntry)
            .sortedWith(
                compareByDescending<LeaderboardEntry> { it.driftScore }
                    .thenByDescending { it.dateString }
            )
            .take(30)

        race.newlyDiscoveredCompanion = newlyDiscovered

        saveData = saveData.copy(
            cash = saveData.cash + rewardCash,
            gems = saveData.gems + rewardGems,
            companionAffinities = updatedAffinities,
            discoveredCompanionIds = updatedDiscovered,
            selectedCompanionId = updatedSelectedCompanionId,
            unlockedTrackIds = updatedUnlockedTracks,
            completedTutorial = true,
            leaderboardEntries = updatedLeaderboard
        )

        checkAndTriggerGameCompletion()
        persistSaveToDisk()
    }

    fun checkAndTriggerGameCompletion(): Boolean {
        val allGirlIds = GameDataDefaults.initialCompanions.map { it.id }.toSet()
        val discovered = saveData.discoveredCompanionIds
        if (discovered.containsAll(allGirlIds)) {
            val allMaxed = allGirlIds.all { girlId ->
                (saveData.companionAffinities[girlId] ?: 0) >= 100
            }
            if (allMaxed) {
                val updatedUnlocked = saveData.unlockedTrackIds + "track_endless"
                if (!saveData.isGameCompleted || !saveData.unlockedTrackIds.contains("track_endless")) {
                    saveData = saveData.copy(
                        isGameCompleted = true,
                        unlockedTrackIds = updatedUnlocked
                    )
                    persistSaveToDisk()
                }
                return true
            }
        }
        return false
    }

    fun resetGameProgress() {
        saveData = GameSaveData()
        cars = GameDataDefaults.initialCars
        companions = GameDataDefaults.initialCompanions
        AppSettings.languageSetting = LanguageSetting.SYSTEM
        com.driftgirls.applyLanguageSetting(LanguageSetting.SYSTEM)
        AppSettings.themeSetting = ThemeSetting.SYSTEM
        AppSettings.musicVolume = 1.0f
        AppSettings.isMuted = false
        com.driftgirls.setMusicMuted(false)
        com.driftgirls.setMusicVolume(1.0f)
        persistSaveToDisk()
    }

    fun updateLanguageSetting(setting: LanguageSetting) {
        com.driftgirls.applyLanguageSetting(setting)
        AppSettings.languageSetting = setting
        saveData = saveData.copy(languageSetting = setting.name)
        persistSaveToDisk()
    }

    fun updateThemeSetting(setting: ThemeSetting) {
        AppSettings.themeSetting = setting
        saveData = saveData.copy(themeSetting = setting.name)
        persistSaveToDisk()
    }

    fun updateMusicVolume(volume: Float) {
        AppSettings.musicVolume = volume
        saveData = saveData.copy(musicVolume = volume)
        com.driftgirls.setMusicVolume(volume)
        persistSaveToDisk()
    }

    fun updateMuted(muted: Boolean) {
        AppSettings.isMuted = muted
        saveData = saveData.copy(isMuted = muted)
        com.driftgirls.setMusicMuted(muted)
        persistSaveToDisk()
    }

    fun getHighestEndlessScore(): Int {
        return saveData.leaderboardEntries
            .filter { it.trackName == "Infinity Drift Pass" }
            .maxOfOrNull { it.driftScore } ?: 5000
    }

    fun selectCar(carId: String) {
        if (saveData.ownedCarIds.contains(carId)) {
            saveData = saveData.copy(selectedCarId = carId)
            persistSaveToDisk()
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
            persistSaveToDisk()
        }
    }

    fun getUpgradeCost(car: Car, component: String): Int {
        val level = when (component) {
            "ENGINE" -> car.engineUpgradeLevel
            "TIRES" -> car.tiresUpgradeLevel
            "NITRO" -> car.nitroUpgradeLevel
            else -> 0
        }
        if (level >= 5) return 0
        return 1000 * (level + 1)
    }

    fun upgradeCarComponent(carId: String, component: String) {
        val car = cars.find { it.id == carId } ?: return
        val level = when (component) {
            "ENGINE" -> car.engineUpgradeLevel
            "TIRES" -> car.tiresUpgradeLevel
            "NITRO" -> car.nitroUpgradeLevel
            else -> 0
        }
        if (level >= 5) return
        val upgradeCost = getUpgradeCost(car, component)
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
            persistSaveToDisk()
        }
    }

    fun updateCarColor(carId: String, colorHex: Long) {
        cars = cars.map {
            if (it.id == carId) it.copy(colorHex = colorHex) else it
        }
        persistSaveToDisk()
    }

    fun selectCompanion(companionId: String) {
        if (saveData.discoveredCompanionIds.contains(companionId)) {
            saveData = saveData.copy(selectedCompanionId = companionId)
            persistSaveToDisk()
        }
    }

    fun goOnDate(companionId: String, dateActivity: DateActivity): Boolean {
        if (!saveData.discoveredCompanionIds.contains(companionId)) return false
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
            checkAndTriggerGameCompletion()
            persistSaveToDisk()
            return true
        }
        return false
    }

    fun giveGift(companionId: String, giftItem: GiftItem): Boolean {
        if (!saveData.discoveredCompanionIds.contains(companionId)) return false
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
            checkAndTriggerGameCompletion()
            persistSaveToDisk()
            return true
        }
        return false
    }
}
