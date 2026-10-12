package com.driftgirls.ui.screens

import androidx.compose.animation.*
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
import com.driftgirls.engine.GameEngine
import com.driftgirls.i18n.localizedSubtitle
import com.driftgirls.model.Car
import com.driftgirls.resources.*
import com.driftgirls.ui.graphics.CarCanvas
import org.jetbrains.compose.resources.stringResource

@Composable
fun GarageScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    onStartRace: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val cardBg = if (isDarkTheme) Color(0xFF28113D) else Color(0xFFFFFFFF)
    val textColor = if (isDarkTheme) Color.White else Color(0xFF1E0A2A)
    val subTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color(0xFF1E0A2A).copy(alpha = 0.6f)

    val cars = gameEngine.cars
    val activeCarId = gameEngine.saveData.selectedCarId
    var previewCarId by remember { mutableStateOf(activeCarId) }
    val previewCar = cars.find { it.id == previewCarId } ?: cars.first()

    val companion = gameEngine.currentCompanion
    val isOwned = gameEngine.saveData.ownedCarIds.contains(previewCar.id)
    val isSelected = previewCar.id == activeCarId

    val paintColors = listOf(
        0xFFECEFF1, // Pure White
        0xFFFF3D00, // Crimson Red
        0xFF29B6F6, // Midnight Blue
        0xFFFFD700, // Sunburst Gold
        0xFFFF007F, // Cyber Pink
        0xFF212121  // Matte Black
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
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToHome) {
                Text("◄", color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = stringResource(Res.string.garage_title),
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )

            // Currency Status
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$${gameEngine.saveData.cash}",
                    color = Color(0xFF76FF03),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${gameEngine.saveData.gems} 💎",
                    color = Color(0xFF00E5FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Car Carousel Selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(cars) { car ->
                val isCarOwned = gameEngine.saveData.ownedCarIds.contains(car.id)
                val isCarUnlocked = gameEngine.isCarUnlocked(car)
                val isCarActive = car.id == previewCarId

                Card(
                    modifier = Modifier
                        .width(140.dp)
                        .clickable { previewCarId = car.id },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCarActive) Color(0xFFFF007F) else (if (isDarkTheme) Color(0xFF2B1947) else Color(0xFFE2D4F8))
                    ),
                    border = if (isCarActive) androidx.compose.foundation.BorderStroke(2.dp, if (isDarkTheme) Color.White else Color(0xFF1E0A2A)) else null
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = car.name,
                            color = if (isCarActive) Color.White else textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val statusText = when {
                            isCarOwned -> stringResource(Res.string.owned)
                            isCarUnlocked -> "$${car.price}"
                            else -> "🔒 " + stringResource(Res.string.car_locked)
                        }
                        val statusColor = when {
                            isCarActive && !isCarOwned && !isCarUnlocked -> Color.White.copy(alpha = 0.8f)
                            isCarActive && isCarOwned -> Color(0xFF76FF03)
                            isCarActive -> Color(0xFFFFD600)
                            isCarOwned -> Color(0xFF76FF03)
                            isCarUnlocked -> if (isDarkTheme) Color(0xFFFFD600) else Color(0xFFB8860B)
                            else -> subTextColor
                        }
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Center Showroom Car Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(if (isDarkTheme) Color(0xFF140B28) else Color(0xFFFFFFFF), RoundedCornerShape(24.dp))
                .border(1.dp, textColor.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            CarCanvas(
                car = previewCar,
                driftAngle = 12f,
                modifier = Modifier.size(160.dp, 200.dp)
            )

            // Companion Affection Stat Boost Tag
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(Color(0xFFFF007F).copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (companion != null) stringResource(Res.string.partner_buff_format, companion.name, companion.affection) else stringResource(Res.string.no_partner_buff),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vehicle Details
        Text(text = previewCar.name, color = textColor, fontSize = 22.sp, fontWeight = FontWeight.Black)
        Text(text = previewCar.localizedSubtitle(), color = subTextColor, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Performance Stat Bars
        val affectionBonus = companion?.affection?.toFloat() ?: 0f
        val effSpeed = previewCar.effectiveTopSpeed(affectionBonus)
        val effDrift = previewCar.effectiveDriftControl(affectionBonus)
        val effNitro = previewCar.effectiveNitroCapacity(affectionBonus)

        val engineCost = gameEngine.getUpgradeCost(previewCar, "ENGINE")
        val tiresCost = gameEngine.getUpgradeCost(previewCar, "TIRES")
        val nitroCost = gameEngine.getUpgradeCost(previewCar, "NITRO")

        StatRow(
            label = stringResource(Res.string.stat_top_speed),
            valueText = "$effSpeed${stringResource(Res.string.speed_unit)}",
            progress = effSpeed / 400f,
            level = previewCar.engineUpgradeLevel,
            upgradeCost = engineCost,
            canAfford = gameEngine.saveData.cash >= engineCost,
            barColor = Color(0xFFFF3D00),
            textColor = textColor,
            subTextColor = subTextColor,
            isDarkTheme = isDarkTheme
        ) {
            if (isOwned) gameEngine.upgradeCarComponent(previewCar.id, "ENGINE")
        }
        Spacer(modifier = Modifier.height(8.dp))
        StatRow(
            label = stringResource(Res.string.stat_drift_control),
            valueText = "$effDrift / 100",
            progress = effDrift / 100f,
            level = previewCar.tiresUpgradeLevel,
            upgradeCost = tiresCost,
            canAfford = gameEngine.saveData.cash >= tiresCost,
            barColor = Color(0xFFFF007F),
            textColor = textColor,
            subTextColor = subTextColor,
            isDarkTheme = isDarkTheme
        ) {
            if (isOwned) gameEngine.upgradeCarComponent(previewCar.id, "TIRES")
        }
        Spacer(modifier = Modifier.height(8.dp))
        StatRow(
            label = stringResource(Res.string.stat_nitro_capacity),
            valueText = "$effNitro / 100",
            progress = effNitro / 100f,
            level = previewCar.nitroUpgradeLevel,
            upgradeCost = nitroCost,
            canAfford = gameEngine.saveData.cash >= nitroCost,
            barColor = Color(0xFF00E5FF),
            textColor = textColor,
            subTextColor = subTextColor,
            isDarkTheme = isDarkTheme
        ) {
            if (isOwned) gameEngine.upgradeCarComponent(previewCar.id, "NITRO")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Custom Paint Color Palette
        Text(stringResource(Res.string.custom_paint), color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            paintColors.forEach { colorHex ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(colorHex))
                        .border(
                            width = if (previewCar.colorHex == colorHex) 3.dp else 1.dp,
                            color = if (previewCar.colorHex == colorHex) textColor else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable {
                            if (isOwned) {
                                gameEngine.updateCarColor(previewCar.id, colorHex)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Main Action Button (Buy / Select / Race)
        if (isOwned) {
            if (isSelected) {
                Button(
                    onClick = onStartRace,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF007F))
                ) {
                    Text(stringResource(Res.string.drive_now), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            } else {
                Button(
                    onClick = { gameEngine.selectCar(previewCar.id) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
                ) {
                    Text(stringResource(Res.string.select_car), color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            }
        } else {
            val isUnlocked = gameEngine.isCarUnlocked(previewCar)
            val previousCar = gameEngine.getPreviousCar(previewCar)

            if (isUnlocked) {
                Button(
                    onClick = { gameEngine.buyCar(previewCar) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF76FF03)),
                    enabled = gameEngine.canBuyCar(previewCar)
                ) {
                    Text(
                        text = stringResource(Res.string.buy_car_format, previewCar.price),
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            } else {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = textColor.copy(alpha = 0.15f),
                        disabledContainerColor = textColor.copy(alpha = 0.15f)
                    ),
                    enabled = false
                ) {
                    Text(
                        text = stringResource(Res.string.buy_previous_car_first, previousCar?.name ?: ""),
                        color = textColor.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StatRow(
    label: String,
    valueText: String,
    progress: Float,
    level: Int,
    upgradeCost: Int,
    canAfford: Boolean,
    barColor: Color,
    textColor: Color,
    subTextColor: Color,
    isDarkTheme: Boolean,
    onUpgrade: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, color = subTextColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(textColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = if (level >= 5) stringResource(Res.string.max_level) else stringResource(Res.string.level_format, level),
                        color = if (level >= 5) Color(0xFFFFD600) else Color(0xFF00E5FF),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Text(valueText, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Black)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(CircleShape),
                color = barColor,
                trackColor = textColor.copy(alpha = 0.15f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onUpgrade,
                enabled = level < 5 && canAfford,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (level >= 5) Color.Gray.copy(alpha = 0.3f) else if (canAfford) Color(0xFF76FF03) else textColor.copy(alpha = 0.15f),
                    disabledContainerColor = textColor.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (level >= 5) stringResource(Res.string.max_level) else stringResource(Res.string.upgrade_cost_format, upgradeCost),
                    color = if (level >= 5) textColor.copy(alpha = 0.5f) else if (canAfford) Color.Black else textColor.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
