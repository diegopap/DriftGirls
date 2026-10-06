package com.driftgirls.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.driftgirls.engine.GameEngine
import com.driftgirls.ui.graphics.ArcadeTrackCanvas
import com.driftgirls.ui.graphics.CarCanvas
import com.driftgirls.ui.graphics.CompanionAvatarCanvas

@Composable
fun RaceScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    onGoToGarage: () -> Unit
) {
    val race = gameEngine.activeRaceState ?: return
    var showTutorial by remember {
        mutableStateOf(race.track.isTutorial || !gameEngine.saveData.completedTutorial)
    }

    // Smooth Game Engine Update Loop
    LaunchedEffect(showTutorial) {
        if (!showTutorial) {
            var lastFrameTimeNanos = 0L
            while (true) {
                withFrameNanos { frameTimeNanos ->
                    if (lastFrameTimeNanos != 0L) {
                        val deltaTimeSeconds = ((frameTimeNanos - lastFrameTimeNanos).toFloat() / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                        gameEngine.updateRace(deltaTimeSeconds)
                    }
                    lastFrameTimeNanos = frameTimeNanos
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // 1. Arcade Track Canvas View
        ArcadeTrackCanvas(
            cornerProgress = race.cornerProgress,
            isInsideCorner = race.isInsideCorner,
            cornerDistance = race.nextCornerDistance,
            isNitroActive = race.isNitroActive
        )

        // 2. Car Canvas View Centered
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 190.dp)
        ) {
            CarCanvas(
                car = race.car,
                driftAngle = race.driftAngle,
                isNitroActive = race.isNitroActive
            )
        }

        // 3. Top HUD Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // Distance Progress Bar
            val progress = (race.distanceTravelled / race.track.distanceMeters.toFloat()).coerceIn(0f, 1f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${race.track.name} (${(progress * 100).toInt()}%)",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = onBackToHome,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("EXIT", color = Color.White, fontSize = 10.sp)
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(top = 4.dp)
                    .clip(CircleShape),
                color = Color(0xFF00E5FF),
                trackColor = Color.White.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = " KM/H",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    if (race.comboMultiplier > 1) {
                        Text(
                            text = "🔥 ${race.comboMultiplier}x COMBO!",
                            color = Color(0xFFFFD600),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Drift Score
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "DRIFT SCORE",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${race.driftScore}",
                        color = Color(0xFFFF4081),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nitro Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("NITRO", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { race.nitroLevel / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(CircleShape),
                    color = Color(0xFF00E5FF),
                    trackColor = Color.Gray.copy(alpha = 0.3f)
                )
            }
        }

        // 4. Live Companion Reaction Overlay Banner (Top-Left)
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 150.dp, start = 16.dp, end = 16.dp),
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
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            race.companionSpeech?.let { speech ->
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF880E4F), Color(0xFFAD1457))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(1.5.dp, Color(0xFFFF80AB), RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = speech,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 5. Drift Rating Flash Badge
        race.lastDriftRating?.let { rating ->
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 60.dp)
                    .background(
                        Color(rating.colorHex).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = rating.label,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // 6. Corner Approach Direction Alert
        if (race.isInsideCorner || race.nextCornerDistance < 90f) {
            Text(
                text = if (race.isInsideCorner) "⚡ DRIFT NOW!! ⚡" else "◀ CORNER APPROACHING ▶",
                color = if (race.isInsideCorner) Color(0xFFFFD600) else Color(0xFF00E5FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 80.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // 7. Interactive Controls Overlay (Bottom)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Drift Left Pad
            Box(
                modifier = Modifier
                    .size(110.dp, 80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (race.isDriftingLeft) Color(0xFFFF007F) else Color(0xFFFF007F).copy(alpha = 0.4f)
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(20.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                gameEngine.triggerDrift(isLeft = true)
                                tryAwaitRelease()
                                gameEngine.releaseDrift()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("◄ DRIFT", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
            }

            // Nitro Boost Button
            Button(
                onClick = { gameEngine.activateNitro() },
                modifier = Modifier.size(90.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (race.nitroLevel >= 25f) Color(0xFF00E5FF) else Color.Gray
                ),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("⚡\nNITRO", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            }

            // Drift Right Pad
            Box(
                modifier = Modifier
                    .size(110.dp, 80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (race.isDriftingRight) Color(0xFFFF007F) else Color(0xFFFF007F).copy(alpha = 0.4f)
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(20.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                gameEngine.triggerDrift(isLeft = false)
                                tryAwaitRelease()
                                gameEngine.releaseDrift()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("DRIFT ►", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
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
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1035))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CompanionAvatarCanvas(
                            companion = race.companion,
                            expression = "EXCITED",
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "RACE TUTORIAL",
                            color = Color(0xFFFFD600),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "1. Tap & hold ◄ DRIFT or DRIFT ► when approaching corners to execute high-score drifts!\n\n" +
                                    "2. Perfect timing awards PERFECT and GREAT drift ratings to charge your Nitro!\n\n" +
                                    "3. Hit ⚡ NITRO to burst into top speeds!\n\n" +
                                    "4. ${race.companion.name} grants passive bonuses and cheers for you!",
                            color = Color.White,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { showTutorial = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF007F))
                        ) {
                            Text("LET'S RACE!!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // 9. Race Finish Victory Dialog
        if (race.isFinished) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1228))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "RACE FINISHED!!",
                            color = Color(0xFFFFD600),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Stars
                        Text(
                            text = "★".repeat(race.starsEarned) + "☆".repeat(3 - race.starsEarned),
                            color = Color(0xFFFFDD00),
                            fontSize = 36.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Rewards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("CASH", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("+$${race.earnedCash}", color = Color(0xFF76FF03), fontSize = 18.sp, fontWeight = FontWeight.Black)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("GEMS", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("+${race.earnedGems} 💎", color = Color(0xFF00E5FF), fontSize = 18.sp, fontWeight = FontWeight.Black)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("AFFECTION", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("+${race.earnedAffection} 💕", color = Color(0xFFFF4081), fontSize = 18.sp, fontWeight = FontWeight.Black)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            OutlinedButton(
                                onClick = onGoToGarage,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("GARAGE", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
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
