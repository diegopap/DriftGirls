package com.driftgirls.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.driftgirls.engine.GameEngine
import com.driftgirls.i18n.*
import com.driftgirls.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun TrackSelectScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    onStartRace: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val textColor = if (isDarkTheme) Color.White else Color(0xFF1E0A2A)
    val subTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color(0xFF1E0A2A).copy(alpha = 0.6f)

    val tracks = gameEngine.tracks
    val unlockedTrackIds = gameEngine.saveData.unlockedTrackIds
    val discoveredCompanionIds = gameEngine.saveData.discoveredCompanionIds

    val girlNamesMap = mapOf(
        "aoi" to "Aoi",
        "sora" to "Sora",
        "elena" to "Elena",
        "mia" to "Mia"
    )

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
    ) {
        // Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToHome) {
                Text("◄", color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = stringResource(Res.string.tracks_title),
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "$${gameEngine.saveData.cash}",
                color = Color(0xFF76FF03),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tracks List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tracks) { track ->
                val isUnlocked = unlockedTrackIds.contains(track.id)
                val girlId = track.discoverableGirlId
                val girlName = girlNamesMap[girlId] ?: ""
                val isGirlDiscovered = girlId != null && discoveredCompanionIds.contains(girlId)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) {
                            if (isDarkTheme) Color(0xFF162B48) else Color(0xFFFFFFFF)
                        } else {
                            if (isDarkTheme) Color(0xFF0D182A) else Color(0xFFE9E4F8)
                        }
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = if (isUnlocked) Color(track.primaryColorHex).copy(alpha = 0.6f) else textColor.copy(alpha = 0.2f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = track.name,
                                        color = if (isUnlocked) textColor else subTextColor,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    if (track.isTutorial) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFFFD600), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(stringResource(Res.string.tutorial_tag), color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Text(
                                    text = "${track.localizedLocation()} • ${stringResource(Res.string.difficulty_format, track.localizedDifficulty())}",
                                    color = subTextColor,
                                    fontSize = 12.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isUnlocked) Color(track.primaryColorHex) else subTextColor.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isUnlocked) stringResource(Res.string.unlocked) else "${stringResource(Res.string.locked).uppercase()} 🔒",
                                    color = if (isUnlocked) Color.Black else textColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Discoverable Girl Badge
                        if (girlName.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isGirlDiscovered) Color(0xFFFF007F).copy(alpha = 0.25f) else Color(0xFFFFD600).copy(alpha = 0.15f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (isGirlDiscovered) {
                                        stringResource(Res.string.companion_discovered_format, girlName)
                                    } else {
                                        stringResource(Res.string.discover_girl_format, girlName)
                                    },
                                    color = if (isGirlDiscovered) Color(0xFFFF80AB) else Color(0xFFFFD600),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Distance & Corners stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(Res.string.distance_format, track.distanceMeters), color = subTextColor, fontSize = 12.sp)
                            Text(stringResource(Res.string.hairpins_format, track.cornersCount), color = subTextColor, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Rewards or High Score
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (track.isEndless) {
                                val highScore = gameEngine.getHighestEndlessScore()
                                Text(stringResource(Res.string.rewards_label), color = subTextColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("👑 $highScore PTS", color = Color(0xFFFFD600), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Text(stringResource(Res.string.rewards_label), color = subTextColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("+$${track.rewardCash}", color = Color(0xFF76FF03), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("+${track.rewardGems} 💎", color = Color(0xFF00E5FF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (!isUnlocked) {
                            val lockMessage = if (track.id == "track_endless") {
                                stringResource(Res.string.lock_msg_endless)
                            } else {
                                stringResource(Res.string.lock_msg_standard)
                            }
                            Text(
                                text = lockMessage,
                                color = Color(0xFFFF5252),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(
                            onClick = {
                                gameEngine.startRace(track.id)
                                onStartRace()
                            },
                            enabled = isUnlocked,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(track.primaryColorHex),
                                disabledContainerColor = subTextColor.copy(alpha = 0.2f)
                            )
                        ) {
                            Text(
                                text = if (isUnlocked) stringResource(Res.string.enter_race) else stringResource(Res.string.locked).uppercase(),
                                color = if (isUnlocked) Color.Black else textColor.copy(alpha = 0.4f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
