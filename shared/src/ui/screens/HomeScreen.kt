package com.driftgirls.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.driftgirls.engine.GameEngine
import com.driftgirls.ui.graphics.CarCanvas
import com.driftgirls.ui.graphics.CompanionAvatarCanvas

@Composable
fun HomeScreen(
    gameEngine: GameEngine,
    onNavigateToRace: () -> Unit,
    onNavigateToGarage: () -> Unit,
    onNavigateToCompanions: () -> Unit,
    onNavigateToTracks: () -> Unit,
    onNavigateToLeaderboard: () -> Unit
) {
    val companion = gameEngine.currentCompanion
    val car = gameEngine.currentCar
    val isGameCompleted = gameEngine.saveData.isGameCompleted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF140826), Color(0xFF271040), Color(0xFF0F041D))
                )
            )
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header Title & Status Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DRIFT GIRLS",
                    color = Color(0xFFFF007F),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "HIGH OCTANE DRIFT & DATING",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                var isMutedState by remember { mutableStateOf(com.driftgirls.isMusicMuted()) }
                Surface(
                    onClick = {
                        val nextMuted = !isMutedState
                        com.driftgirls.setMusicMuted(nextMuted)
                        isMutedState = nextMuted
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF25133B)
                ) {
                    Text(
                        text = if (isMutedState) "🔇" else "🎵",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF25133B)
                ) {
                    Text(
                        text = "$${gameEngine.saveData.cash}",
                        color = Color(0xFF76FF03),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF25133B)
                ) {
                    Text(
                        text = "${gameEngine.saveData.gems} 💎",
                        color = Color(0xFF00E5FF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Game Completed Banner
        if (isGameCompleted) {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD600))
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "👑 GAME COMPLETED - ULTIMATE DRIFT KING! 👑",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Infinity Drift Pass (Endless 60s) Track is now UNLOCKED!",
                        color = Color.Black.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Center Hero Card Showcase (Companion & Car)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF210E36)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF007F).copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Partner Speech Bubble
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFF007F).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFFF007F), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = if (companion != null) {
                            "💬 \"Hey driver! Ready to set some new drift records today?\""
                        } else {
                            "💬 \"No companion selected yet! Win races with 3 stars for a 50% chance to discover girls!\""
                        },
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Companion Avatar
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CompanionAvatarCanvas(
                            companion = companion,
                            modifier = Modifier.size(90.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = companion?.name ?: "No Partner",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = if (companion != null) "Affection: ${companion.affection}%" else "Locked",
                            color = Color(0xFFFF80AB),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Car Canvas
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val companionAffection = companion?.affection?.toFloat() ?: 0f
                        CarCanvas(
                            car = car,
                            driftAngle = 15f,
                            modifier = Modifier.size(110.dp, 130.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = car.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${car.effectiveTopSpeed(companionAffection)} KM/H",
                            color = Color(0xFF00E5FF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Race Launch Buttons
        Button(
            onClick = onNavigateToRace,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF007F))
        ) {
            Text(
                text = "ENTER RACE CAREER 🏁",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom Navigation Quick Cards
        Text(text = "GAME MENU", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MenuCard(
                title = "GARAGE",
                subtitle = "Tuning",
                icon = "🏎️",
                color = Color(0xFFFF3D00),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToGarage
            )

            MenuCard(
                title = "GIRLS",
                subtitle = "Dates",
                icon = "💕",
                color = Color(0xFFFF007F),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToCompanions
            )

            MenuCard(
                title = "TRACKS",
                subtitle = "Career",
                icon = "🗺️",
                color = Color(0xFF00E5FF),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToTracks
            )

            MenuCard(
                title = "SCORES",
                subtitle = "Leaderboard",
                icon = "🏆",
                color = Color(0xFFFFD600),
                modifier = Modifier.weight(1f),
                onClick = onNavigateToLeaderboard
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Restart / Reset Game Progress Button
        TextButton(
            onClick = { gameEngine.resetGameProgress() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "🔄 RESET GAME PROGRESS (FRESH START)",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MenuCard(
    title: String,
    subtitle: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF220F38)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
            Text(text = subtitle, color = Color.White.copy(alpha = 0.5f), fontSize = 8.sp)
        }
    }
}
