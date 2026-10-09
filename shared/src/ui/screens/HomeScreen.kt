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
import com.driftgirls.resources.*
import com.driftgirls.ui.graphics.CarCanvas
import com.driftgirls.ui.graphics.CompanionAvatarCanvas
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    gameEngine: GameEngine,
    onNavigateToRace: () -> Unit,
    onNavigateToGarage: () -> Unit,
    onNavigateToCompanions: () -> Unit,
    onNavigateToTracks: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToSettings: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val companion = gameEngine.currentCompanion
    val car = gameEngine.currentCar
    val isGameCompleted = gameEngine.saveData.isGameCompleted

    val heroCardBg = if (isDarkTheme) Color(0xFF210E36) else Color(0xFFFFFFFF)
    val menuCardBg = if (isDarkTheme) Color(0xFF220F38) else Color(0xFFFFFFFF)
    val mainTextColor = if (isDarkTheme) Color.White else Color(0xFF1E0A2A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    if (isDarkTheme) {
                        listOf(Color(0xFF140826), Color(0xFF271040), Color(0xFF0F041D))
                    } else {
                        listOf(Color(0xFFF5F0FF), Color(0xFFE9DCFF), Color(0xFFDFCDFF))
                    }
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
                    text = stringResource(Res.string.app_title),
                    color = Color(0xFFFF007F),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = stringResource(Res.string.app_subtitle),
                    color = mainTextColor.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Settings Wheel Button (⚙️)
                Surface(
                    onClick = onNavigateToSettings,
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDarkTheme) Color(0xFF25133B) else Color(0xFFE2D4F8)
                ) {
                    Text(
                        text = "⚙️",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDarkTheme) Color(0xFF25133B) else Color(0xFFE2D4F8)
                ) {
                    Text(
                        text = "$${gameEngine.saveData.cash}",
                        color = Color(0xFF2E7D32),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDarkTheme) Color(0xFF25133B) else Color(0xFFE2D4F8)
                ) {
                    Text(
                        text = "${gameEngine.saveData.gems} 💎",
                        color = Color(0xFF00838F),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
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
                        text = stringResource(Res.string.game_completed_title),
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = stringResource(Res.string.game_completed_desc),
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
            colors = CardDefaults.cardColors(containerColor = heroCardBg),
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
                            stringResource(Res.string.speech_partner_ready)
                        } else {
                            stringResource(Res.string.speech_no_partner)
                        },
                        color = mainTextColor,
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
                            text = companion?.name ?: stringResource(Res.string.no_partner),
                            color = mainTextColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = if (companion != null) stringResource(Res.string.affection_format, companion.affection) else stringResource(Res.string.locked),
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
                            color = mainTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${car.effectiveTopSpeed(companionAffection)}${stringResource(Res.string.speed_unit)}",
                            color = Color(0xFF00B8D4),
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
                text = stringResource(Res.string.enter_race_career),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom Navigation Quick Cards
        Text(text = stringResource(Res.string.game_menu), color = mainTextColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MenuCard(
                title = stringResource(Res.string.menu_garage),
                subtitle = stringResource(Res.string.menu_garage_sub),
                icon = "🏎️",
                color = Color(0xFFFF3D00),
                cardBg = menuCardBg,
                textColor = mainTextColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToGarage
            )

            MenuCard(
                title = stringResource(Res.string.menu_girls),
                subtitle = stringResource(Res.string.menu_girls_sub),
                icon = "💕",
                color = Color(0xFFFF007F),
                cardBg = menuCardBg,
                textColor = mainTextColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToCompanions
            )

            MenuCard(
                title = stringResource(Res.string.menu_tracks),
                subtitle = stringResource(Res.string.menu_tracks_sub),
                icon = "🗺️",
                color = Color(0xFF00E5FF),
                cardBg = menuCardBg,
                textColor = mainTextColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToTracks
            )

            MenuCard(
                title = stringResource(Res.string.menu_scores),
                subtitle = stringResource(Res.string.menu_scores_sub),
                icon = "🏆",
                color = Color(0xFFFFD600),
                cardBg = menuCardBg,
                textColor = mainTextColor,
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
                text = stringResource(Res.string.reset_progress),
                color = mainTextColor.copy(alpha = 0.4f),
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
    cardBg: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.Black)
            Text(text = subtitle, color = textColor.copy(alpha = 0.5f), fontSize = 8.sp)
        }
    }
}
