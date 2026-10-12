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
import com.driftgirls.i18n.*
import com.driftgirls.resources.*
import com.driftgirls.ui.graphics.CompanionAvatarCanvas
import org.jetbrains.compose.resources.stringResource

@Composable
fun CompanionsScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val cardBg = if (isDarkTheme) Color(0xFF28113D) else Color(0xFFFFFFFF)
    val textColor = if (isDarkTheme) Color.White else Color(0xFF1E0A2A)
    val subTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color(0xFF1E0A2A).copy(alpha = 0.6f)

    val companions = gameEngine.companions
    val discoveredIds = gameEngine.saveData.discoveredCompanionIds
    val selectedCompanionId = gameEngine.saveData.selectedCompanionId

    // Default preview companion to selected or first discovered or first companion
    var previewCompanionId by remember {
        mutableStateOf(selectedCompanionId ?: companions.first().id)
    }

    val baseCompanion = companions.find { it.id == previewCompanionId } ?: companions.first()
    val isPreviewDiscovered = discoveredIds.contains(baseCompanion.id)
    val savedAffection = gameEngine.saveData.companionAffinities[baseCompanion.id] ?: baseCompanion.affection
    val activeCompanion = baseCompanion.copy(affection = savedAffection)

    val isEquipped = activeCompanion.id == selectedCompanionId
    var reactionMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Dates, 1 = Gifts

    val dateSuccessMsg = stringResource(Res.string.date_success_msg)

    val trackGirlRequirements = mapOf(
        "aoi" to "Akina Training Pass (Tutorial)",
        "sora" to "Neon City Strip",
        "elena" to "Dragon Hairpins",
        "mia" to "Sunset Coastal Run"
    )

    val dates = GameDataDefaults.datesList
    val gifts = GameDataDefaults.giftsList

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
        // Top Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToHome) {
                Text("◄", color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = stringResource(Res.string.companions_title),
                color = textColor,
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

        Spacer(modifier = Modifier.height(12.dp))

        // Discovered Companions Notice Banner
        if (discoveredIds.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF880E4F)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF80AB))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔒", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(Res.string.no_companions_banner),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Companion Selection Carousel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(companions) { companion ->
                val isDiscovered = discoveredIds.contains(companion.id)
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
                        containerColor = if (isSelected) Color(0xFFFF007F) else if (isDiscovered) (if (isDarkTheme) Color(0xFF3B1854) else Color(0xFFE2D4F8)) else (if (isDarkTheme) Color(0xFF1C0D2B) else Color(0xFFD6C4F1))
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, if (isDarkTheme) Color.White else Color(0xFF1E0A2A)) else null
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CompanionAvatarCanvas(
                            companion = if (isDiscovered) companion.copy(affection = aff) else null,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isDiscovered) companion.name else "🔒 ${companion.name}",
                            color = if (isSelected || isDiscovered) textColor else (if (isDarkTheme) Color.Gray else Color(0xFF555555)),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isDiscovered) stringResource(Res.string.affection_format, aff) else stringResource(Res.string.locked),
                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else if (isDiscovered) Color(0xFFFF80AB) else subTextColor,
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
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF80AB).copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompanionAvatarCanvas(
                    companion = if (isPreviewDiscovered) activeCompanion else null,
                    expression = if (reactionMessage != null) "EXCITED" else "HAPPY",
                    modifier = Modifier.size(110.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isPreviewDiscovered) activeCompanion.name else "🔒 ${activeCompanion.name} (${stringResource(Res.string.locked).uppercase()})",
                    color = textColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = activeCompanion.localizedTitle(),
                    color = Color(activeCompanion.accentColorHex),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (isPreviewDiscovered) {
                    Text(
                        text = activeCompanion.localizedBio(),
                        color = subTextColor,
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
                            text = "💕 ${stringResource(Res.string.affection_label)} (${activeCompanion.affection}%)",
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
                            trackColor = subTextColor.copy(alpha = 0.2f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Racing Buff Description Badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isDarkTheme) Color(0xFF381B54) else Color(0xFFEEDEFF), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = stringResource(
                                Res.string.race_perk_format,
                                activeCompanion.bonusType.localizedDescription(),
                                activeCompanion.effectiveBonusValue().toInt()
                            ),
                            color = if (isDarkTheme) Color(0xFFFFD600) else Color(0xFF8B6B00),
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
                                .background(Color(0xFFFF007F).copy(alpha = if (isDarkTheme) 0.25f else 0.15f), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFFF007F), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "💬 \"$msg\"",
                                color = textColor,
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
                            text = if (isEquipped) stringResource(Res.string.equipped_partner) else stringResource(Res.string.equip_partner),
                            color = if (isEquipped) Color.Black else Color.White,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else {
                    val reqTrack = trackGirlRequirements[activeCompanion.id] ?: "a Career Track"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isDarkTheme) Color(0xFF1E0A2A) else Color(0xFFEEDEFF), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFFFD600).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.how_to_unlock_format, reqTrack, activeCompanion.name),
                            color = if (isDarkTheme) Color(0xFFFFD600) else Color(0xFF8B6B00),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Interaction Tabs (Dates / Gifts)
        if (isPreviewDiscovered) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 0) Color(0xFFFF007F) else cardBg
                    )
                ) {
                    Text(
                        stringResource(Res.string.tab_dates),
                        color = if (selectedTab == 0) Color.White else textColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 1) Color(0xFFFF007F) else cardBg
                    )
                ) {
                    Text(
                        stringResource(Res.string.tab_gifts),
                        color = if (selectedTab == 1) Color.White else textColor,
                        fontWeight = FontWeight.Bold
                    )
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
                            colors = CardDefaults.cardColors(containerColor = cardBg)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = dateActivity.localizedTitle(),
                                        color = textColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${dateActivity.localizedLocation()} • ${dateActivity.localizedDescription()}",
                                        color = subTextColor,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = stringResource(Res.string.affection_gain_format, dateActivity.affectionGain),
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
                                            reactionMessage = dateSuccessMsg
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
                        val giftName = giftItem.localizedName()
                        val giftSuccessFmt = stringResource(Res.string.gift_success_format, giftName)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg)
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
                                        text = giftName,
                                        color = textColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = stringResource(Res.string.affection_gain_format, giftItem.affectionGain),
                                        color = Color(0xFFFF80AB),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = {
                                        val success = gameEngine.giveGift(activeCompanion.id, giftItem)
                                        if (success) {
                                            reactionMessage = giftSuccessFmt
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
}
