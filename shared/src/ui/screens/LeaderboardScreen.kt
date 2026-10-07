package com.driftgirls.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
fun LeaderboardScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit
) {
    val leaderboard = gameEngine.saveData.leaderboardEntries.sortedWith(
        compareByDescending<com.driftgirls.model.LeaderboardEntry> { it.driftScore }
            .thenByDescending { it.dateString }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF140728), Color(0xFF260F45), Color(0xFF0D0418))
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
                text = "DRIFT LEADERBOARD 🏆",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "$${gameEngine.saveData.cash}",
                color = Color(0xFF76FF03),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Sorting Subtitle Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF007F).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFFF007F), RoundedCornerShape(12.dp))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "📊 RANKED BY HIGHEST DRIFT SCORE & RECENT DATE",
                color = Color(0xFFFFD600),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (leaderboard.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF23103D))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏁", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "NO HIGH SCORES YET!",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Complete races on Career Tracks or Infinity Drift Pass to record your scores!",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(leaderboard) { index, entry ->
                    val rank = index + 1
                    val rankBadgeColor = when (rank) {
                        1 -> Color(0xFFFFD700) // Gold
                        2 -> Color(0xFFC0C0C0) // Silver
                        3 -> Color(0xFFCD7F32) // Bronze
                        else -> Color(0xFF3A1B54)
                    }
                    val rankTextColor = if (rank <= 3) Color.Black else Color.White

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF220F3A)),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (rank == 1) Color(0xFFFFD700) else Color.White.copy(alpha = 0.15f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rank Badge
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(rankBadgeColor, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (rank) {
                                        1 -> "🥇"
                                        2 -> "🥈"
                                        3 -> "🥉"
                                        else -> "#$rank"
                                    },
                                    color = rankTextColor,
                                    fontSize = if (rank <= 3) 18.sp else 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Score & Track Info
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${entry.driftScore} PTS",
                                        color = Color(0xFFFF4081),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = entry.dateString,
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 10.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "${entry.trackName} • ${entry.carName}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                if (entry.companionName != null) {
                                    Text(
                                        text = "Partner: ${entry.companionName}",
                                        color = Color(0xFF00E5FF),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
