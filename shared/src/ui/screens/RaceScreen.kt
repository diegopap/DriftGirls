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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import com.driftgirls.PlatformBackHandler
import com.driftgirls.engine.GameEngine
import com.driftgirls.i18n.AppSettings
import com.driftgirls.i18n.LanguageSetting
import com.driftgirls.i18n.ThemeSetting
import com.driftgirls.i18n.localizedTitle
import com.driftgirls.model.RacePhase
import com.driftgirls.resources.*
import com.driftgirls.ui.graphics.ArcadeTrackCanvas
import com.driftgirls.ui.graphics.CarCanvas
import com.driftgirls.ui.graphics.CircularTachometer
import com.driftgirls.ui.graphics.CompanionAvatarCanvas
import org.jetbrains.compose.resources.stringResource

@Composable
fun RaceScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    onGoToGarage: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val cardBg = if (isDarkTheme) Color(0xFF1E1035) else Color(0xFFFFFFFF)
    val finishCardBg = if (isDarkTheme) Color(0xFF1A1228) else Color(0xFFFFFFFF)
    val textColor = if (isDarkTheme) Color.White else Color(0xFF1E0A2A)
    val subTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color(0xFF1E0A2A).copy(alpha = 0.6f)
    val titleColor = if (isDarkTheme) Color(0xFFFFD600) else Color(0xFF8B6B00)

    val race = gameEngine.activeRaceState ?: return
    var showTutorial by remember(race) {
        mutableStateOf(race.track.isTutorial && !gameEngine.saveData.completedTutorial)
    }
    var showSettings by remember(race) { mutableStateOf(false) }

    PlatformBackHandler(enabled = !race.isFinished) {
        showSettings = !showSettings
    }

    // Smooth Game Engine Update Loop
    LaunchedEffect(race) {
        var lastFrameTimeNanos = 0L
        while (true) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTimeNanos != 0L) {
                    val deltaTimeSeconds = ((frameTimeNanos - lastFrameTimeNanos).toFloat() / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                    if (!showTutorial && !showSettings && !race.isFinished) {
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
                    onClick = { showSettings = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("⚙️", fontSize = 14.sp)
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
                            text = stringResource(Res.string.speed_unit),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    if (race.comboMultiplier > 1) {
                        Text(
                            text = stringResource(Res.string.combo_format, race.comboMultiplier),
                            color = Color(0xFFFFD600),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Drift Score
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(Res.string.drift_score_label),
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
                Text(stringResource(Res.string.nitro_label), color = Color(0xFF00E5FF), fontSize = 10.sp, fontWeight = FontWeight.Black)
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
                        text = stringResource(Res.string.time_remaining_format, wholeSec, tenths),
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
                            text = stringResource(Res.string.engine_tachometer),
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
                            text = stringResource(Res.string.tachometer_target),
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
                    modifier = Modifier.size(if (isLandscape) 110.dp else 110.dp, if (isLandscape) 64.dp else 80.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (race.nitroLevel >= 25f) Color(0xFF00E5FF) else Color.Gray
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text(stringResource(Res.string.btn_nitro), color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
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
                    Text(stringResource(Res.string.btn_drift_left), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                if (race.racePhase == RacePhase.COUNTDOWN) {
                    Box(
                        modifier = Modifier
                            .size(if (isLandscape) 110.dp else 110.dp, if (isLandscape) 64.dp else 80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (race.isAccelerating) Color(0xFF76FF03) else Color(0xFF00E5FF).copy(alpha = 0.85f)
                            )
                            .border(2.5.dp, Color.White, RoundedCornerShape(20.dp))
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
                            Text(stringResource(Res.string.btn_gas), color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
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
                    Text(stringResource(Res.string.btn_drift_right), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black)
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
                    colors = CardDefaults.cardColors(containerColor = cardBg)
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
                            text = stringResource(Res.string.tutorial_title),
                            color = Color(0xFFFFD600),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = stringResource(Res.string.tutorial_body),
                            color = textColor,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showTutorial = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF007F))
                        ) {
                            Text(stringResource(Res.string.lets_race), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
                    colors = CardDefaults.cardColors(containerColor = finishCardBg)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(Res.string.race_finished),
                            color = titleColor,
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
                                        text = stringResource(Res.string.new_girl_discovered),
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
                                        text = newlyMet.localizedTitle(),
                                        color = Color(0xFFFF80AB),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stringResource(Res.string.new_girl_unlocked),
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
                                        .background(if (isDarkTheme) Color(0xFF28113D) else Color(0xFFF3EFFF), RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (race.starsEarned == 3) {
                                            stringResource(Res.string.missed_discovery_3stars)
                                        } else {
                                            stringResource(Res.string.missed_discovery_needstars)
                                        },
                                        color = titleColor,
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
                                Text(stringResource(Res.string.cash_label), color = subTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("+$${race.earnedCash}", color = Color(0xFF76FF03), fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(stringResource(Res.string.gems_label), color = subTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("+${race.earnedGems} 💎", color = Color(0xFF00E5FF), fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                            if (race.companion != null) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(stringResource(Res.string.affection_label), color = subTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                                Text(stringResource(Res.string.menu_garage), color = textColor)
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
                                        text = if (nextTrackId != null) stringResource(Res.string.next_race) else stringResource(Res.string.continue_btn),
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
                                        text = stringResource(Res.string.retry_race),
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
                                    Text(stringResource(Res.string.continue_btn), color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 10. Settings & Pause Overlay Dialog
        if (showSettings) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(if (isLandscape) 0.75f else 0.95f)
                        .fillMaxHeight(if (isLandscape) 0.92f else 0.85f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF007F).copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(Res.string.settings_title),
                            color = titleColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // RESUME & RESTART buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { showSettings = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF007F))
                            ) {
                                Text(
                                    text = stringResource(Res.string.btn_resume),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Button(
                                onClick = {
                                    showSettings = false
                                    gameEngine.startRace(race.track.id)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600))
                            ) {
                                Text(
                                    text = stringResource(Res.string.btn_restart),
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 1. Language Options
                        RaceSettingsSectionCard(
                            title = stringResource(Res.string.settings_language),
                            isDark = isDarkTheme
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                RaceOptionTile(
                                    label = stringResource(Res.string.settings_language_system),
                                    isSelected = AppSettings.languageSetting == LanguageSetting.SYSTEM,
                                    isDark = isDarkTheme,
                                    onClick = { gameEngine.updateLanguageSetting(LanguageSetting.SYSTEM) }
                                )
                                RaceOptionTile(
                                    label = stringResource(Res.string.settings_language_es),
                                    isSelected = AppSettings.languageSetting == LanguageSetting.ES,
                                    isDark = isDarkTheme,
                                    onClick = { gameEngine.updateLanguageSetting(LanguageSetting.ES) }
                                )
                                RaceOptionTile(
                                    label = stringResource(Res.string.settings_language_en),
                                    isSelected = AppSettings.languageSetting == LanguageSetting.EN,
                                    isDark = isDarkTheme,
                                    onClick = { gameEngine.updateLanguageSetting(LanguageSetting.EN) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 2. Music & Audio Options
                        RaceSettingsSectionCard(
                            title = stringResource(Res.string.settings_audio),
                            isDark = isDarkTheme
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (AppSettings.isMuted) stringResource(Res.string.settings_music_muted) else stringResource(Res.string.settings_music_enabled),
                                        color = textColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Switch(
                                        checked = !AppSettings.isMuted,
                                        onCheckedChange = { enabled ->
                                            gameEngine.updateMuted(!enabled)
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFFFF007F)
                                        )
                                    )
                                }

                                if (!AppSettings.isMuted) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = stringResource(Res.string.settings_music_volume),
                                                color = subTextColor,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${(AppSettings.musicVolume * 100).toInt()}%",
                                                color = Color(0xFFFF007F),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Slider(
                                            value = AppSettings.musicVolume,
                                            onValueChange = { vol ->
                                                gameEngine.updateMusicVolume(vol)
                                            },
                                            valueRange = 0f..1f,
                                            colors = SliderDefaults.colors(
                                                thumbColor = Color(0xFFFF007F),
                                                activeTrackColor = Color(0xFFFF007F)
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 3. Display Theme Options
                        RaceSettingsSectionCard(
                            title = stringResource(Res.string.settings_theme),
                            isDark = isDarkTheme
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                RaceOptionTile(
                                    label = stringResource(Res.string.settings_theme_system),
                                    isSelected = AppSettings.themeSetting == ThemeSetting.SYSTEM,
                                    isDark = isDarkTheme,
                                    onClick = { gameEngine.updateThemeSetting(ThemeSetting.SYSTEM) }
                                )
                                RaceOptionTile(
                                    label = stringResource(Res.string.settings_theme_dark),
                                    isSelected = AppSettings.themeSetting == ThemeSetting.DARK,
                                    isDark = isDarkTheme,
                                    onClick = { gameEngine.updateThemeSetting(ThemeSetting.DARK) }
                                )
                                RaceOptionTile(
                                    label = stringResource(Res.string.settings_theme_light),
                                    isSelected = AppSettings.themeSetting == ThemeSetting.LIGHT,
                                    isDark = isDarkTheme,
                                    onClick = { gameEngine.updateThemeSetting(ThemeSetting.LIGHT) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 4. Navigation Actions (GARAGE & MAIN MENU)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    showSettings = false
                                    onGoToGarage()
                                },
                                modifier = Modifier.weight(1f),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF007F))
                            ) {
                                Text(
                                    text = "🏎️ ${stringResource(Res.string.menu_garage)}",
                                    color = textColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    showSettings = false
                                    onBackToHome()
                                },
                                modifier = Modifier.weight(1f),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF007F))
                            ) {
                                Text(
                                    text = "🏠 ${stringResource(Res.string.game_menu)}",
                                    color = textColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RaceSettingsSectionCard(
    title: String,
    isDark: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardBg = if (isDark) Color(0xFF281340) else Color(0xFFF0E5FF)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF007F).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                color = Color(0xFFFF007F),
                fontSize = 11.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun RaceOptionTile(
    label: String,
    isSelected: Boolean,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) Color(0xFFFF007F) else if (isDark) Color(0xFF3B1C5C) else Color(0xFFFFFFFF)
    val fg = if (isSelected) Color.White else if (isDark) Color.White else Color(0xFF1E0A2A)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = fg,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            if (isSelected) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
