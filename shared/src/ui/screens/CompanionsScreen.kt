package com.driftgirls.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.driftgirls.engine.GameDataDefaults
import com.driftgirls.engine.GameEngine
import com.driftgirls.ui.graphics.CompanionAvatarCanvas

@Composable
fun CompanionsScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit
) {
    val companions = gameEngine.companions
    val selectedCompanionId = gameEngine.saveData.selectedCompanionId
    var previewCompanionId by remember { mutableStateOf(selectedCompanionId) }

    val baseCompanion = companions.find { it.id == previewCompanionId } ?: companions.first()
    val savedAffection = gameEngine.saveData.companionAffinities[baseCompanion.id] ?: baseCompanion.affection
    val activeCompanion = baseCompanion.copy(affection = savedAffection)

    val isEquipped = activeCompanion.id == selectedCompanionId
    var reactionMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Dates, 1 = Gifts

    val dates = GameDataDefaults.datesList
    val gifts = GameDataDefaults.giftsList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1E0A2A), Color(0xFF2C103D), Color(0xFF110519))
                )
            )
            .statusBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToHome) {
                Text("◄", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "COMPANIONS & DATING",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = "${gameEngine.saveData.gems} 💎",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Companion Selection Carousel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(companions) { companion ->
                val aff = gameEngine.saveData.companionAffinities[companion.id] ?: companion.affection
                val isSelected = companion.id == previewCompanionId

                Card(
                    modifier = Modifier
                        .width(130.dp)
                        .clickable {
                            previewCompanionId = companion.id
                            reactionMessage = null
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFFF007F) else Color(0xFF3B1854)
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CompanionAvatarCanvas(
                            companion = companion.copy(affection = aff),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = companion.name,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Affection: $aff%",
                            color = Color(0xFFFF80AB),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Active Companion Profile Showcase Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF28113D)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF80AB).copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompanionAvatarCanvas(
                    companion = activeCompanion,
                    expression = if (reactionMessage != null) "EXCITED" else "HAPPY",
                    modifier = Modifier.size(110.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = activeCompanion.name,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = activeCompanion.title,
                    color = Color(activeCompanion.accentColorHex),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = activeCompanion.bio,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Affection Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💕 AFFECTION (${activeCompanion.affection}%)",
                        color = Color(0xFFFF4081),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    LinearProgressIndicator(
                        progress = { activeCompanion.affection / 100f },
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(CircleShape),
                        color = Color(0xFFFF4081),
                        trackColor = Color.White.copy(alpha = 0.15f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Racing Buff Description Badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF381B54), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "🏎️ RACE PERK: ${activeCompanion.bonusType.description} (+${activeCompanion.effectiveBonusValue().toInt()}%)",
                        color = Color(0xFFFFD600),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Live Reaction Speech Bubble
                reactionMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFF007F).copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFFF007F), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "💬 \"$msg\"",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Equip Companion Button
                Button(
                    onClick = { gameEngine.selectCompanion(activeCompanion.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isEquipped) Color(0xFF76FF03) else Color(0xFFFF007F)
                    )
                ) {
                    Text(
                        text = if (isEquipped) "EQUIPPED PARTNER ✓" else "EQUIP AS RACING PARTNER",
                        color = if (isEquipped) Color.Black else Color.White,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Interaction Tabs (Dates / Gifts)
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { selectedTab = 0 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 0) Color(0xFFFF007F) else Color(0xFF28113D)
                )
            ) {
                Text("DATES 🌹", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { selectedTab = 1 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 1) Color(0xFFFF007F) else Color(0xFF28113D)
                )
            ) {
                Text("GIFTS 🎁", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            // Dates List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                dates.forEach { dateActivity ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF28113D))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = dateActivity.title,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${dateActivity.location} • ${dateActivity.description}",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "+${dateActivity.affectionGain}% Affection Gain",
                                    color = Color(0xFFFF80AB),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    val success = gameEngine.goOnDate(activeCompanion.id, dateActivity)
                                    if (success) {
                                        reactionMessage = "I had the most magical time with you on our date! 💕"
                                    }
                                },
                                enabled = gameEngine.saveData.gems >= dateActivity.costGems,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
                            ) {
                                Text(
                                    text = "${dateActivity.costGems} 💎",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Gifts List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                gifts.forEach { giftItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF28113D))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = giftItem.iconSymbol, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = giftItem.name,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "+${giftItem.affectionGain}% Affection Gain",
                                    color = Color(0xFFFF80AB),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    val success = gameEngine.giveGift(activeCompanion.id, giftItem)
                                    if (success) {
                                        reactionMessage = "Kyaa~! Thank you so much for the ${giftItem.name}! You're amazing!"
                                    }
                                },
                                enabled = gameEngine.saveData.gems >= giftItem.costGems,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF007F))
                            ) {
                                Text(
                                    text = "${giftItem.costGems} 💎",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
