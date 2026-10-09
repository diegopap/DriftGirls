package com.driftgirls.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.driftgirls.engine.GameEngine
import com.driftgirls.model.RacePhase
import com.driftgirls.ui.graphics.ArcadeTrackCanvas
import com.driftgirls.ui.graphics.CarCanvas
import com.driftgirls.ui.graphics.CircularTachometer
import com.driftgirls.ui.graphics.CompanionAvatarCanvas

@Composable
fun RaceScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    onGoToGarage: () -> Unit
) {
    val race = gameEngine.activeRaceState ?: return
    var showTutorial by remember {
        mutableStateOf(race.track.isTutorial && !gameEngine.saveData.completedTutorial)
    }

    // Smooth Game Engine Update Loop
    LaunchedEffect(Unit) {
        var lastFrameTimeNanos = 0L
        while (true) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTimeNanos != 0L) {
                    val deltaTimeSeconds = ((frameTimeNanos - lastFrameTimeNanos).toFloat() / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                    if (!showTutorial) {
                        gameEngine.updateRace(deltaTimeSeconds)
                    }
                }
                lastFrameTimeNanos = frameTimeNanos
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        val isLandscape = maxWidth > maxHeight

        // 1. Arcade Track Canvas View
        val isLastDrift = race.isLastCorner
        ArcadeTrackCanvas(
            trackId = race.track.id,
            cornerProgress = race.cornerProgress,
            isInsideCorner = race.isInsideCorner,
            cornerDistance = race.nextCornerDistance,
            cornerDirection = race.currentCornerDirection,
            isNitroActive = race.isNitroActive,
            isLastDrift = isLastDrift,
            distanceTravelled = race.distanceTravelled
        )

        // 2. Car Canvas View
        val steeringShift = when {
            race.isDriftingLeft -> (-35).dp
            race.isDriftingRight -> 35.dp
            else -> 0.dp
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = steeringShift)
                .padding(bottom = if (isLandscape) 100.dp else 180.dp)
        ) {
            CarCanvas(
                car = race.car,
                driftAngle = race.driftAngle,
                isNitroActive = race.isNitroActive,
                modifier = Modifier.size(if (isLandscape) 140.dp else 180.dp, if (isLandscape) 170.dp else 220.dp)
            )
        }

        // 3. Top HUD Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = if (isLandscape) 6.dp else 16.dp)
        ) {
            // Top Progress Bar (Distance for normal tracks, High Score comparison for Endless track)
            val progressText: String
            val progressValue: Float

            if (race.track.isEndless) {
                val highScore = gameEngine.getHighestEndlessScore()
                val targetScore = maxOf(highScore, 5000)
                progressValue = (race.driftScore.toFloat() / targetScore.toFloat()).coerceIn(0f, 1f)
                val isBeating = race.driftScore > highScore
                progressText = if (isBeating) {
                    "${race.track.name} • 👑 HIGH SCORE: ${race.driftScore} / $highScore"
                } else {
                    "${race.track.name} • High Score: $highScore (${(progressValue * 100).toInt()}%)"
                }
            } else {
                progressValue = (race.distanceTravelled / race.track.distanceMeters.toFloat()).coerceIn(0f, 1f)
                progressText = "${race.track.name} (${(progressValue * 100).toInt()}%)"
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = progressText,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = onBackToHome,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp)
                ) {
                    Text("EXIT", color = Color.White, fontSize = 10.sp)
                }
            }

            LinearProgressIndicator(
                progress = { progressValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(top = 2.dp)
                    .clip(CircleShape),
                color = if (race.track.isEndless) Color(0xFFFFD600) else Color(0xFF00E5FF),
                trackColor = Color.White.copy(alpha = 0.2f)
            )

            if (!isLandscape) {
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Speedometer & Nitro Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speed & Combo
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${race.currentSpeed.toInt()}",
                            color = if (race.isNitroActive) Color(0xFF00E5FF) else Color.White,
                            fontSize = if (isLandscape) 24.sp else 32.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = " KM/H",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    if (race.comboMultiplier > 1) {
                        Text(
                            text = "🔥 ${race.comboMultiplier}x COMBO!",
                            color = Color(0xFFFFD600),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Drift Score
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "DRIFT SCORE",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${race.driftScore}",
                        color = Color(0xFFFF4081),
                        fontSize = if (isLandscape) 18.sp else 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Nitro Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("NITRO", color = Color(0xFF00E5FF), fontSize = 10.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.width(6.dp))
                LinearProgressIndicator(
                    progress = { race.nitroLevel / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(CircleShape),
                    color = Color(0xFF00E5FF),
                    trackColor = Color.Gray.copy(alpha = 0.3f)
                )
            }

            // 60s Time Limit Banner Centered Directly Below Nitro Bar
            race.timeRemainingSeconds?.let { timeSec ->
                Spacer(modifier = Modifier.height(6.dp))
                val isUrgent = timeSec <= 10f
                val wholeSec = timeSec.toInt()
                val tenths = ((timeSec - wholeSec) * 10).toInt().coerceIn(0, 9)

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(
                            if (isUrgent) Color(0xFFFF1744) else Color(0xFFFFD600),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(1.5.dp, Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⏱️ TIME REMAINING: $wholeSec.${tenths}s",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // 4. Live Companion Reaction Overlay Banner (Top-Left)
        if (race.companion != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = if (isLandscape) 75.dp else 150.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompanionAvatarCanvas(
                    companion = race.companion,
                    expression = when {
                        race.lastDriftRating == com.driftgirls.model.DriftRating.PERFECT -> "EXCITED"
                        race.lastDriftRating == com.driftgirls.model.DriftRating.GREAT -> "WINK"
                        race.lastDriftRating == com.driftgirls.model.DriftRating.MISS -> "SURPRISED"
                        else -> "HAPPY"
                    },
                    modifier = Modifier.size(if (isLandscape) 48.dp else 64.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                race.companionSpeech?.let { speech ->
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF880E4F), Color(0xFFAD1457))
                                ),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(1.5.dp, Color(0xFFFF80AB), RoundedCornerShape(14.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = speech,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 5. Start Countdown Overlay & Tachometer Gauge (During Countdown)
        if (race.racePhase == RacePhase.COUNTDOWN) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = if (isLandscape) 10.dp else 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val countdownInt = kotlin.math.ceil(race.countdownTimer).toInt().coerceAtLeast(1)

                Text(
                    text = "$countdownInt",
                    color = Color(0xFFFFD600),
                    fontSize = if (isLandscape) 48.sp else 68.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    modifier = Modifier.width(if (isLandscape) 260.dp else 290.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.85f)),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF00E5FF))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ENGINE TACHOMETER",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        CircularTachometer(
                            engineRpm = race.engineRpm,
                            modifier = Modifier.size(if (isLandscape) 140.dp else 170.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "🎯 TARGET: 6,400 - 7,300 RPM (GREEN ZONE)",
                            color = Color(0xFFFFD600),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // 6. Drift Rating Flash Badge
        race.driftDisplayBanner?.let { bannerText ->
            val badgeColor = race.lastDriftRating?.colorHex ?: 0xFFFFD600
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 50.dp)
                    .background(
                        Color(badgeColor).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                    .padding(horizontal = 22.dp, vertical = 10.dp)
            ) {
                Text(
                    text = bannerText,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // 7. Interactive Controls Overlay (Bottom)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = if (isLandscape) 8.dp else 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Button(
                    onClick = { gameEngine.activateNitro() },
                    modifier = Modifier.size(if (isLandscape) 95.dp else 110.dp, if (isLandscape) 46.dp else 56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (race.nitroLevel >= 25f) Color(0xFF00E5FF) else Color.Gray
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text("⚡ NITRO", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(if (isLandscape) 110.dp else 110.dp, if (isLandscape) 64.dp else 80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (race.isDriftingLeft) Color(0xFFFF007F) else Color(0xFFFF007F).copy(alpha = 0.45f)
                        )
                        .border(2.dp, Color.White, RoundedCornerShape(20.dp))
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitFirstDown(requireUnconsumed = false)
                                    gameEngine.triggerDrift(isLeft = true)
                                    waitForUpOrCancellation()
                                    gameEngine.releaseDrift()
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("◄ DRIFT", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                if (race.racePhase == RacePhase.COUNTDOWN) {
                    Box(
                        modifier = Modifier
                            .size(if (isLandscape) 100.dp else 110.dp, if (isLandscape) 46.dp else 56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (race.isAccelerating) Color(0xFF76FF03) else Color(0xFF00E5FF).copy(alpha = 0.85f)
                            )
                            .border(2.5.dp, Color.White, RoundedCornerShape(16.dp))
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        awaitFirstDown(requireUnconsumed = false)
                                        gameEngine.setAccelerating(true)
                                        waitForUpOrCancellation()
                                        gameEngine.setAccelerating(false)
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🏎️", fontSize = 15.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("GAS", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Box(
                    modifier = Modifier
                        .size(if (isLandscape) 110.dp else 110.dp, if (isLandscape) 64.dp else 80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (race.isDriftingRight) Color(0xFFFF007F) else Color(0xFFFF007F).copy(alpha = 0.45f)
                        )
                        .border(2.dp, Color.White, RoundedCornerShape(20.dp))
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitFirstDown(requireUnconsumed = false)
                                    gameEngine.triggerDrift(isLeft = false)
                                    waitForUpOrCancellation()
                                    gameEngine.releaseDrift()
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("DRIFT ►", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        // 8. Tutorial Overlay Dialog
        if (showTutorial) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(if (isLandscape) 0.7f else 1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1035))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CompanionAvatarCanvas(
                            companion = race.companion,
                            expression = "EXCITED",
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "RACE TUTORIAL",
                            color = Color(0xFFFFD600),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "1. During 5s countdown, hold GAS 🏎️ to keep RPM in the GREEN ZONE (6,400 - 7,300 RPM) for a PERFECT LAUNCH!\n\n" +
                                    "2. Tap & hold ◄ DRIFT or DRIFT ► in corners for high-score drifts!\n\n" +
                                    "3. Get 3 STARS (5000+ Score) for a 50% chance to DISCOVER THE TRACK'S GIRL and unlock the next race!\n\n" +
                                    "4. Hit ⚡ NITRO to burst into top speeds!",
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showTutorial = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF007F))
                        ) {
                            Text("LET'S RACE!!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }

        // 9. Race Finish Victory Dialog
        if (race.isFinished) {
            val newlyMet = race.newlyDiscoveredCompanion

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(if (isLandscape) 0.65f else 0.9f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1228))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "RACE FINISHED!!",
                            color = Color(0xFFFFD600),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Stars
                        Text(
                            text = "★".repeat(race.starsEarned) + "☆".repeat(3 - race.starsEarned),
                            color = Color(0xFFFFDD00),
                            fontSize = 32.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // NEW GIRL DISCOVERED POPUP CARD!
                        if (newlyMet != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFAD1457)),
                                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFF80AB))
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "🎉 NEW GIRL DISCOVERED!! 🎉",
                                        color = Color(0xFFFFD600),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    CompanionAvatarCanvas(
                                        companion = newlyMet,
                                        expression = "EXCITED",
                                        modifier = Modifier.size(70.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = newlyMet.name,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = newlyMet.title,
                                        color = Color(0xFFFF80AB),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "She is now unlocked & the next race is UNLOCKED!",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        } else {
                            val girlId = race.track.discoverableGirlId
                            val isGirlDiscovered = girlId != null && gameEngine.saveData.discoveredCompanionIds.contains(girlId)
                            if (girlId != null && !isGirlDiscovered) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF28113D), RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (race.starsEarned == 3) {
                                            "🎲 3 Stars Earned! (50% Discovery Chance Missed - Race again to discover her & unlock the next track!)"
                                        } else {
                                            "🔒 Need 3 Stars (5000+ Drift Score) for a 50% chance to discover the girl & unlock the next race!"
                                        },
                                        color = Color(0xFFFFD600),
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                        // Rewards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("CASH", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("+$${race.earnedCash}", color = Color(0xFF76FF03), fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("GEMS", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("+${race.earnedGems} 💎", color = Color(0xFF00E5FF), fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                            if (race.companion != null) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("AFFECTION", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text("+${race.earnedAffection} 💕", color = Color(0xFFFF4081), fontSize = 16.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        val nextTrackMap = mapOf(
                            "track_tutorial" to "track_neon_city",
                            "track_neon_city" to "track_mountain",
                            "track_mountain" to "track_ocean"
                        )
                        val nextTrackId = nextTrackMap[race.track.id]
                        val trackGirlId = race.track.discoverableGirlId
                        val isGirlDiscoveredForTrack = trackGirlId != null && gameEngine.saveData.discoveredCompanionIds.contains(trackGirlId)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            OutlinedButton(
                                onClick = onGoToGarage,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("GARAGE", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))

                            if (newlyMet != null) {
                                // Girl WAS FOUND! Automatically race next track!
                                Button(
                                    onClick = {
                                        if (nextTrackId != null) {
                                            gameEngine.startRace(nextTrackId)
                                        } else {
                                            onBackToHome()
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76FF03))
                                ) {
                                    Text(
                                        text = if (nextTrackId != null) "NEXT RACE 🏁" else "CONTINUE",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            } else if (trackGirlId != null && !isGirlDiscoveredForTrack) {
                                // Missed finding the girl! RETRY race to attempt discovering her again!
                                Button(
                                    onClick = {
                                        gameEngine.startRace(race.track.id)
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600))
                                ) {
                                    Text(
                                        text = "RETRY RACE 🔄",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            } else {
                                // Already discovered or standard continuation
                                Button(
                                    onClick = onBackToHome,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF007F))
                                ) {
                                    Text("CONTINUE", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
