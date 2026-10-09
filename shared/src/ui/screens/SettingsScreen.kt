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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.driftgirls.engine.GameEngine
import com.driftgirls.i18n.AppSettings
import com.driftgirls.i18n.LanguageSetting
import com.driftgirls.i18n.ThemeSetting
import com.driftgirls.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    gameEngine: GameEngine,
    onBackToHome: () -> Unit,
    isDarkTheme: Boolean = true
) {
    val backgroundColor = if (isDarkTheme) Color(0xFF0F041D) else Color(0xFFF3EFFF)
    val cardBg = if (isDarkTheme) Color(0xFF210E36) else Color(0xFFFFFFFF)
    val textColor = if (isDarkTheme) Color.White else Color(0xFF1E0A2A)
    val subTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color(0xFF1E0A2A).copy(alpha = 0.6f)

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
                text = stringResource(Res.string.settings_title),
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.width(48.dp)) // Spacer for alignment
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Language Settings Section
        SettingsCard(
            title = stringResource(Res.string.settings_language),
            cardBg = cardBg,
            textColor = textColor
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OptionTile(
                    label = stringResource(Res.string.settings_language_system),
                    isSelected = AppSettings.languageSetting == LanguageSetting.SYSTEM,
                    isDark = isDarkTheme,
                    onClick = { gameEngine.updateLanguageSetting(LanguageSetting.SYSTEM) }
                )
                OptionTile(
                    label = stringResource(Res.string.settings_language_es),
                    isSelected = AppSettings.languageSetting == LanguageSetting.ES,
                    isDark = isDarkTheme,
                    onClick = { gameEngine.updateLanguageSetting(LanguageSetting.ES) }
                )
                OptionTile(
                    label = stringResource(Res.string.settings_language_en),
                    isSelected = AppSettings.languageSetting == LanguageSetting.EN,
                    isDark = isDarkTheme,
                    onClick = { gameEngine.updateLanguageSetting(LanguageSetting.EN) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Audio & Music Settings Section
        SettingsCard(
            title = stringResource(Res.string.settings_audio),
            cardBg = cardBg,
            textColor = textColor
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (AppSettings.isMuted) stringResource(Res.string.settings_music_muted) else stringResource(Res.string.settings_music_enabled),
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = !AppSettings.isMuted,
                        onCheckedChange = { enabled ->
                            gameEngine.updateMuted(!enabled)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFFF007F)
                        )
                    )
                }

                if (!AppSettings.isMuted) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(Res.string.settings_music_volume),
                                color = subTextColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${(AppSettings.musicVolume * 100).toInt()}%",
                                color = Color(0xFFFF007F),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Slider(
                            value = AppSettings.musicVolume,
                            onValueChange = { vol ->
                                gameEngine.updateMusicVolume(vol)
                            },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFFF007F),
                                activeTrackColor = Color(0xFFFF007F)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Display Theme Section
        SettingsCard(
            title = stringResource(Res.string.settings_theme),
            cardBg = cardBg,
            textColor = textColor
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OptionTile(
                    label = stringResource(Res.string.settings_theme_system),
                    isSelected = AppSettings.themeSetting == ThemeSetting.SYSTEM,
                    isDark = isDarkTheme,
                    onClick = { gameEngine.updateThemeSetting(ThemeSetting.SYSTEM) }
                )
                OptionTile(
                    label = stringResource(Res.string.settings_theme_dark),
                    isSelected = AppSettings.themeSetting == ThemeSetting.DARK,
                    isDark = isDarkTheme,
                    onClick = { gameEngine.updateThemeSetting(ThemeSetting.DARK) }
                )
                OptionTile(
                    label = stringResource(Res.string.settings_theme_light),
                    isSelected = AppSettings.themeSetting == ThemeSetting.LIGHT,
                    isDark = isDarkTheme,
                    onClick = { gameEngine.updateThemeSetting(ThemeSetting.LIGHT) }
                )
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    cardBg: Color,
    textColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF007F).copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = Color(0xFFFF007F),
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun OptionTile(
    label: String,
    isSelected: Boolean,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) Color(0xFFFF007F) else if (isDark) Color(0xFF2B1645) else Color(0xFFF0E5FF)
    val fg = if (isSelected) Color.White else if (isDark) Color.White else Color(0xFF1E0A2A)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = fg,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            if (isSelected) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
