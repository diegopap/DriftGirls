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

@Composable
fun TrackSelectScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    onStartRace: () -> Unit
) {
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
                    listOf(Color(0xFF091321), Color(0xFF102138), Color(0xFF060D17))
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
                Text("◄", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "CAREER TRACKS",
                color = Color.White,
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
                        containerColor = if (isUnlocked) Color(0xFF162B48) else Color(0xFF0D182A)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = if (isUnlocked) Color(track.primaryColorHex).copy(alpha = 0.6f) else Color.Gray.copy(alpha = 0.2f)
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
                                        color = if (isUnlocked) Color.White else Color.Gray,
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
                                            Text("TUTORIAL", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Text(
                                    text = "${track.location} • Difficulty: ${track.difficulty}",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isUnlocked) Color(track.primaryColorHex) else Color.Gray.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isUnlocked) "UNLOCKED" else "LOCKED 🔒",
                                    color = if (isUnlocked) Color.Black else Color.White.copy(alpha = 0.6f),
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
                                        "💕 COMPANION DISCOVERED: $girlName ✓"
                                    } else {
                                        "🔒 DISCOVER GIRL: $girlName (3 Stars = 50% Chance)"
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
                            Text("Distance: ${track.distanceMeters}m", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            Text("Hairpins: ${track.cornersCount} Corners", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Rewards
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("REWARDS:", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("+$${track.rewardCash}", color = Color(0xFF76FF03), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("+${track.rewardGems} 💎", color = Color(0xFF00E5FF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (!isUnlocked) {
                            Text(
                                text = "🔒 Race locked! You need to discover the girl from the previous race to unlock!",
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
                                disabledContainerColor = Color.Gray.copy(alpha = 0.2f)
                            )
                        ) {
                            Text(
                                text = if (isUnlocked) "ENTER RACE 🏁" else "LOCKED",
                                color = if (isUnlocked) Color.Black else Color.White.copy(alpha = 0.4f),
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
