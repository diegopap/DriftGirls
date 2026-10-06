package com.driftgirls.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.driftgirls.model.RaceTrack

@Composable
fun TrackSelectScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    onStartRace: () -> Unit
) {
    val tracks = gameEngine.tracks
    val unlockedTrackIds = gameEngine.saveData.unlockedTrackIds

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

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF162B48)),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = Color(track.primaryColorHex).copy(alpha = 0.6f)
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
                                        color = Color.White,
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
                                    .background(Color(track.primaryColorHex), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isUnlocked) "UNLOCKED" else "LOCKED",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Distance & Corners stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Distance: ${track.distanceMeters}m", color = Color.White, fontSize = 12.sp)
                            Text("Hairpins: ${track.cornersCount} Corners", color = Color.White, fontSize = 12.sp)
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                gameEngine.startRace(track.id)
                                onStartRace()
                            },
                            enabled = isUnlocked,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(track.primaryColorHex))
                        ) {
                            Text(
                                text = "ENTER RACE 🏁",
                                color = Color.Black,
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
