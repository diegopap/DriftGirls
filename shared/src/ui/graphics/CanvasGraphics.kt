package com.driftgirls.ui.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.driftgirls.model.Car
import com.driftgirls.model.GirlCompanion
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CompanionAvatarCanvas(
    companion: GirlCompanion?,
    expression: String = "HAPPY", // HAPPY, WINK, SURPRISED, EXCITED, BLUSHING
    modifier: Modifier = Modifier.size(100.dp)
) {
    if (companion == null) {
        Canvas(modifier = modifier) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height / 2f

            // Locked / placeholder avatar
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF3B1854), Color(0xFF160B24)),
                    center = Offset(centerX, centerY),
                    radius = width * 0.5f
                ),
                radius = width * 0.48f,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = Color(0xFF4A256B).copy(alpha = 0.6f),
                radius = width * 0.22f,
                center = Offset(centerX, centerY - height * 0.08f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.3f),
                radius = width * 0.1f,
                center = Offset(centerX, centerY + height * 0.05f)
            )
        }
        return
    }

    val accentColor = Color(companion.accentColorHex)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        // 1. Vibrant Background Glow & Badge Rim
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(accentColor.copy(alpha = 0.45f), accentColor.copy(alpha = 0.05f)),
                center = Offset(centerX, centerY),
                radius = width * 0.5f
            ),
            radius = width * 0.49f,
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = Color(0xFF160826),
            radius = width * 0.43f,
            center = Offset(centerX, centerY)
        )

        // 2. Hair Back & Volume Layers
        when (companion.id) {
            "aoi" -> {
                // Pink twin tails with shadow & highlights
                drawCircle(Color(0xFFC2185B), width * 0.24f, Offset(centerX - width * 0.33f, centerY - height * 0.04f))
                drawCircle(Color(0xFFFF4081), width * 0.21f, Offset(centerX - width * 0.32f, centerY - height * 0.05f))
                drawCircle(Color(0xFFC2185B), width * 0.24f, Offset(centerX + width * 0.33f, centerY - height * 0.04f))
                drawCircle(Color(0xFFFF4081), width * 0.21f, Offset(centerX + width * 0.32f, centerY - height * 0.05f))
            }
            "sora" -> {
                // Cyan long flowing hair with gradient curves
                val hairPath = Path().apply {
                    moveTo(centerX - width * 0.38f, centerY - height * 0.12f)
                    cubicTo(centerX - width * 0.45f, centerY + height * 0.1f, centerX - width * 0.35f, centerY + height * 0.42f, centerX - width * 0.22f, centerY + height * 0.48f)
                    lineTo(centerX + width * 0.22f, centerY + height * 0.48f)
                    cubicTo(centerX + width * 0.35f, centerY + height * 0.42f, centerX + width * 0.45f, centerY + height * 0.1f, centerX + width * 0.38f, centerY - height * 0.12f)
                    close()
                }
                drawPath(hairPath, Color(0xFF00838F))
                val hairPathInner = Path().apply {
                    moveTo(centerX - width * 0.34f, centerY - height * 0.1f)
                    cubicTo(centerX - width * 0.4f, centerY + height * 0.1f, centerX - width * 0.3f, centerY + height * 0.38f, centerX - width * 0.18f, centerY + height * 0.42f)
                    lineTo(centerX + width * 0.18f, centerY + height * 0.42f)
                    cubicTo(centerX + width * 0.3f, centerY + height * 0.38f, centerX + width * 0.4f, centerY + height * 0.1f, centerX + width * 0.34f, centerY - height * 0.1f)
                    close()
                }
                drawPath(hairPathInner, Color(0xFF00E5FF))
            }
            "elena" -> {
                // Fiery orange high ponytail
                drawCircle(Color(0xFFE65100), width * 0.28f, Offset(centerX, centerY - height * 0.34f))
                drawCircle(Color(0xFFFF9100), width * 0.23f, Offset(centerX, centerY - height * 0.33f))
            }
            else -> {
                // Purple stylish bob
                drawRoundRect(
                    Color(0xFF4A148C),
                    topLeft = Offset(centerX - width * 0.35f, centerY - height * 0.36f),
                    size = Size(width * 0.7f, height * 0.72f),
                    cornerRadius = CornerRadius(28f, 28f)
                )
                drawRoundRect(
                    Color(0xFF7C4DFF),
                    topLeft = Offset(centerX - width * 0.32f, centerY - height * 0.34f),
                    size = Size(width * 0.64f, height * 0.68f),
                    cornerRadius = CornerRadius(24f, 24f)
                )
            }
        }

        // 3. Shoulders & Outfit Collar
        val outfitColor = when (companion.id) {
            "aoi" -> Color(0xFFAD1457)
            "sora" -> Color(0xFF006064)
            "elena" -> Color(0xFFBF360C)
            else -> Color(0xFF311B92)
        }
        drawArc(
            color = outfitColor,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(centerX - width * 0.32f, centerY + height * 0.08f),
            size = Size(width * 0.64f, height * 0.4f)
        )
        // Collar V-cut
        val collarPath = Path().apply {
            moveTo(centerX - width * 0.12f, centerY + height * 0.12f)
            lineTo(centerX, centerY + height * 0.22f)
            lineTo(centerX + width * 0.12f, centerY + height * 0.12f)
            close()
        }
        drawPath(collarPath, Color.White)

        // 4. Face Skin & Soft Contour Shading
        val faceRadius = width * 0.27f
        drawCircle(
            color = Color(0xFFFFE0B2), // Warm fair skin
            radius = faceRadius,
            center = Offset(centerX, centerY + height * 0.02f)
        )
        // Chin shadow
        drawArc(
            color = Color(0xFFFFCC80).copy(alpha = 0.5f),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(centerX - width * 0.15f, centerY + height * 0.14f),
            size = Size(width * 0.3f, height * 0.12f)
        )

        // 5. Dual Blushing Cheeks (Glowing)
        drawCircle(
            color = Color(0xFFFF80AB).copy(alpha = 0.6f),
            radius = width * 0.065f,
            center = Offset(centerX - width * 0.15f, centerY + height * 0.07f)
        )
        drawCircle(
            color = Color(0xFFFF80AB).copy(alpha = 0.6f),
            radius = width * 0.065f,
            center = Offset(centerX + width * 0.15f, centerY + height * 0.07f)
        )

        // 6. Detailed Anime Eyes with Irises & Catchlight Sparkles
        val eyeColor = when (companion.id) {
            "aoi" -> Color(0xFFD81B60)
            "sora" -> Color(0xFF00838F)
            "elena" -> Color(0xFFE65100)
            else -> Color(0xFF4A148C)
        }

        if (expression == "WINK") {
            // Left eye open
            drawCircle(Color.White, width * 0.055f, Offset(centerX - width * 0.1f, centerY - height * 0.02f))
            drawCircle(eyeColor, width * 0.045f, Offset(centerX - width * 0.1f, centerY - height * 0.02f))
            drawCircle(Color.Black, width * 0.02f, Offset(centerX - width * 0.1f, centerY - height * 0.02f))
            drawCircle(Color.White, width * 0.015f, Offset(centerX - width * 0.115f, centerY - height * 0.035f))

            // Right winking eye arc with eyelashes
            drawArc(
                color = Color.Black,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(centerX + width * 0.05f, centerY - height * 0.05f),
                size = Size(width * 0.1f, height * 0.07f),
                style = Stroke(width = 4.5f, cap = StrokeCap.Round)
            )
        } else {
            // Both eyes open with depth and highlights
            val eyeSize = if (expression == "SURPRISED") width * 0.065f else width * 0.05f
            
            // Left Eye
            drawCircle(Color.White, eyeSize * 1.15f, Offset(centerX - width * 0.1f, centerY - height * 0.02f))
            drawCircle(eyeColor, eyeSize, Offset(centerX - width * 0.1f, centerY - height * 0.02f))
            drawCircle(Color.Black, eyeSize * 0.45f, Offset(centerX - width * 0.1f, centerY - height * 0.02f))
            drawCircle(Color.White, eyeSize * 0.35f, Offset(centerX - width * 0.115f, centerY - height * 0.035f))
            drawCircle(Color.White, eyeSize * 0.15f, Offset(centerX - width * 0.085f, centerY - height * 0.005f))

            // Right Eye
            drawCircle(Color.White, eyeSize * 1.15f, Offset(centerX + width * 0.1f, centerY - height * 0.02f))
            drawCircle(eyeColor, eyeSize, Offset(centerX + width * 0.1f, centerY - height * 0.02f))
            drawCircle(Color.Black, eyeSize * 0.45f, Offset(centerX + width * 0.1f, centerY - height * 0.02f))
            drawCircle(Color.White, eyeSize * 0.35f, Offset(centerX + width * 0.085f, centerY - height * 0.035f))
            drawCircle(Color.White, eyeSize * 0.15f, Offset(centerX + width * 0.115f, centerY - height * 0.005f))

            // Eyebrows
            drawLine(Color(0xFF3E2723), Offset(centerX - width * 0.16f, centerY - height * 0.1f), Offset(centerX - width * 0.04f, centerY - height * 0.09f), strokeWidth = 3f, cap = StrokeCap.Round)
            drawLine(Color(0xFF3E2723), Offset(centerX + width * 0.04f, centerY - height * 0.09f), Offset(centerX + width * 0.16f, centerY - height * 0.1f), strokeWidth = 3f, cap = StrokeCap.Round)
        }

        // 7. Mouth Expression
        when (expression) {
            "SURPRISED" -> {
                drawCircle(Color(0xFFB71C1C), width * 0.045f, Offset(centerX, centerY + height * 0.12f))
                drawCircle(Color(0xFFD32F2F), width * 0.03f, Offset(centerX, centerY + height * 0.12f))
            }
            "EXCITED" -> {
                val mouthPath = Path().apply {
                    moveTo(centerX - width * 0.09f, centerY + height * 0.09f)
                    quadraticTo(centerX, centerY + height * 0.22f, centerX + width * 0.09f, centerY + height * 0.09f)
                    close()
                }
                drawPath(mouthPath, Color(0xFFB71C1C))
            }
            else -> { // HAPPY / Default
                drawArc(
                    color = Color(0xFFB71C1C),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(centerX - width * 0.07f, centerY + height * 0.08f),
                    size = Size(width * 0.14f, height * 0.08f)
                )
            }
        }

        // 8. Front Hair Bangs with Shading & Highlights
        val bangColor = when (companion.id) {
            "aoi" -> Color(0xFFFF4081)
            "sora" -> Color(0xFF00E5FF)
            "elena" -> Color(0xFFFF9100)
            else -> Color(0xFF7C4DFF)
        }
        val bangsPath = Path().apply {
            moveTo(centerX - faceRadius * 1.05f, centerY - height * 0.06f)
            quadraticTo(centerX - width * 0.12f, centerY - height * 0.01f, centerX, centerY - height * 0.18f)
            quadraticTo(centerX + width * 0.12f, centerY - height * 0.01f, centerX + faceRadius * 1.05f, centerY - height * 0.06f)
            quadraticTo(centerX, centerY - height * 0.42f, centerX - faceRadius * 1.05f, centerY - height * 0.06f)
            close()
        }
        drawPath(bangsPath, bangColor)

        // Hair shine highlight arc
        drawArc(
            color = Color.White.copy(alpha = 0.4f),
            startAngle = 200f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(centerX - width * 0.2f, centerY - height * 0.35f),
            size = Size(width * 0.4f, height * 0.25f),
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )

        // 9. Hair Accessory / Ribbon
        drawCircle(accentColor, width * 0.065f, Offset(centerX - width * 0.24f, centerY - height * 0.24f))
        drawCircle(Color.White, width * 0.025f, Offset(centerX - width * 0.24f, centerY - height * 0.24f))
        drawCircle(accentColor, width * 0.065f, Offset(centerX + width * 0.24f, centerY - height * 0.24f))
        drawCircle(Color.White, width * 0.025f, Offset(centerX + width * 0.24f, centerY - height * 0.24f))

        // 10. Polished Outer Badge Ring
        drawCircle(
            color = accentColor,
            radius = width * 0.45f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 6.5f)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.6f),
            radius = width * 0.42f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2f)
        )
    }
}

private data class TrackTheme(
    val skyColors: List<Color>,
    val groundColors: List<Color>,
    val asphaltColor: Color,
    val dashColor: Color,
    val curbColor: Color
)

@Composable
fun ArcadeTrackCanvas(
    trackId: String = "track_tutorial",
    cornerProgress: Float,
    isInsideCorner: Boolean,
    cornerDistance: Float,
    cornerDirection: com.driftgirls.model.CornerDirection = com.driftgirls.model.CornerDirection.RIGHT,
    isNitroActive: Boolean,
    isLastDrift: Boolean = false,
    distanceTravelled: Float = 0f,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val roadHorizonY = h * 0.42f
        val roadBottomY = h

        // Theme configuration per track
        val theme = when (trackId) {
            "track_tutorial" -> TrackTheme(
                skyColors = listOf(Color(0xFF0D1B12), Color(0xFF1B2E24), Color(0xFF2C4436)),
                groundColors = listOf(Color(0xFF152219), Color(0xFF0B140E)),
                asphaltColor = Color(0xFF222623),
                dashColor = Color(0xFFEEEEEE),
                curbColor = Color(0xFFFF1744)
            )
            "track_mountain" -> TrackTheme(
                skyColors = listOf(Color(0xFF2A0818), Color(0xFF4A102A), Color(0xFF7A1D3B)),
                groundColors = listOf(Color(0xFF210A14), Color(0xFF12050B)),
                asphaltColor = Color(0xFF211D24),
                dashColor = Color(0xFFFF6D00),
                curbColor = Color(0xFFFF1744)
            )
            "track_ocean" -> TrackTheme(
                skyColors = listOf(Color(0xFF220033), Color(0xFF6A1B4D), Color(0xFFD84315), Color(0xFFFF8F00)),
                groundColors = listOf(Color(0xFF2B1A0E), Color(0xFF120A04)),
                asphaltColor = Color(0xFF282420),
                dashColor = Color(0xFFFFD600),
                curbColor = Color(0xFFFFD600)
            )
            "track_endless" -> TrackTheme(
                skyColors = listOf(Color(0xFF050014), Color(0xFF120036), Color(0xFF2A0054)),
                groundColors = listOf(Color(0xFF0B001F), Color(0xFF03000A)),
                asphaltColor = Color(0xFF1A1035),
                dashColor = Color(0xFF00E5FF),
                curbColor = Color(0xFFD500F9)
            )
            else -> TrackTheme( // "track_neon_city" & default
                skyColors = listOf(Color(0xFF0F0C20), Color(0xFF241442), Color(0xFF511A65)),
                groundColors = listOf(Color(0xFF1B0038), Color(0xFF0A0014)),
                asphaltColor = Color(0xFF181822),
                dashColor = Color(0xFF00E5FF),
                curbColor = Color(0xFFFF1744)
            )
        }

        // 1. Sky Background
        val skyGradient = Brush.verticalGradient(colors = theme.skyColors)
        drawRect(skyGradient, topLeft = Offset.Zero, size = Size(w, roadHorizonY))

        // 2. Custom Background Scenery
        when (trackId) {
            "track_tutorial" -> {
                // Mountain Hill Silhouettes & Silver Moon
                drawCircle(Color(0xFFE0E6ED), radius = w * 0.08f, center = Offset(w * 0.78f, h * 0.14f))
                val mountainPath = Path().apply {
                    moveTo(0f, roadHorizonY)
                    lineTo(w * 0.15f, roadHorizonY - h * 0.10f)
                    lineTo(w * 0.35f, roadHorizonY - h * 0.05f)
                    lineTo(w * 0.55f, roadHorizonY - h * 0.14f)
                    lineTo(w * 0.8f, roadHorizonY - h * 0.07f)
                    lineTo(w, roadHorizonY - h * 0.12f)
                    lineTo(w, roadHorizonY)
                    close()
                }
                drawPath(mountainPath, Color(0xFF09140C))
            }
            "track_mountain" -> {
                // Mount Fuji Peak & Crimson Blood Moon
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFF1744), Color(0xFFFF80AB), Color.Transparent)
                    ),
                    radius = w * 0.15f,
                    center = Offset(w * 0.3f, h * 0.18f)
                )
                val fujiPath = Path().apply {
                    moveTo(w * 0.35f, roadHorizonY)
                    lineTo(w * 0.65f, roadHorizonY - h * 0.28f)
                    lineTo(w * 0.95f, roadHorizonY)
                    close()
                }
                drawPath(fujiPath, Color(0xFF16050E))
                val snowCap = Path().apply {
                    moveTo(w * 0.60f, roadHorizonY - h * 0.23f)
                    lineTo(w * 0.65f, roadHorizonY - h * 0.28f)
                    lineTo(w * 0.70f, roadHorizonY - h * 0.23f)
                    lineTo(w * 0.65f, roadHorizonY - h * 0.20f)
                    close()
                }
                drawPath(snowCap, Color(0xFFFFD1DC))
            }
            "track_ocean" -> {
                // Sunset Sun & Ocean Reflection
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFD600), Color(0xFFFF3D00), Color.Transparent)
                    ),
                    radius = w * 0.22f,
                    center = Offset(w * 0.5f, h * 0.38f)
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFF8F00).copy(alpha = 0.5f), Color(0xFF003366))
                    ),
                    topLeft = Offset(0f, roadHorizonY - h * 0.08f),
                    size = Size(w, h * 0.08f)
                )
            }
            "track_endless" -> {
                // Celestial Orb
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFD500F9), Color(0xFF00E5FF), Color.Transparent)
                    ),
                    radius = w * 0.20f,
                    center = Offset(w * 0.5f, h * 0.22f)
                )
            }
            else -> { // "track_neon_city"
                // Synthwave Sun & Skyscraper Skyline
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFF007F), Color(0xFFFF9100), Color.Transparent)
                    ),
                    radius = w * 0.22f,
                    center = Offset(w * 0.5f, h * 0.25f)
                )
                val cityColor = Color(0xFF0D061A)
                drawRect(cityColor, Offset(w * 0.05f, roadHorizonY - h * 0.16f), Size(w * 0.12f, h * 0.16f))
                drawRect(cityColor, Offset(w * 0.20f, roadHorizonY - h * 0.22f), Size(w * 0.15f, h * 0.22f))
                drawRect(cityColor, Offset(w * 0.65f, roadHorizonY - h * 0.18f), Size(w * 0.14f, h * 0.18f))
                drawRect(cityColor, Offset(w * 0.82f, roadHorizonY - h * 0.24f), Size(w * 0.13f, h * 0.24f))
            }
        }

        // 3. Ground / Floor Gradient
        val groundGradient = Brush.verticalGradient(
            colors = theme.groundColors,
            startY = roadHorizonY,
            endY = roadBottomY
        )
        drawRect(groundGradient, topLeft = Offset(0f, roadHorizonY), size = Size(w, h - roadHorizonY))

        // Ground / Road Perspective Slices
        val roadTopWidth = w * 0.15f
        val roadBottomWidth = w * 0.92f

        val dirMult = if (cornerDirection == com.driftgirls.model.CornerDirection.LEFT) -1f else 1f
        val maxBend = w * 0.45f

        val numSlices = 28
        val slicePointsLeft = ArrayList<Offset>(numSlices + 1)
        val slicePointsRight = ArrayList<Offset>(numSlices + 1)

        val approachFactor = ((150f - cornerDistance) / 150f).coerceIn(0f, 1f)

        for (i in 0..numSlices) {
            val v = i / numSlices.toFloat()
            val y = roadHorizonY + (roadBottomY - roadHorizonY) * v
            val sliceWidth = roadTopWidth + (roadBottomWidth - roadTopWidth) * v

            val xOffset = if (v < 0.40f) {
                val upperFactor = 1f - (v / 0.40f)
                val bendAmount = upperFactor * upperFactor * (maxBend * 0.85f)

                if (isInsideCorner) {
                    val turnIntensity = cos(cornerProgress * kotlin.math.PI.toFloat() / 2f)
                    dirMult * bendAmount * turnIntensity
                } else if (cornerDistance < 150f) {
                    dirMult * bendAmount * approachFactor
                } else {
                    0f
                }
            } else {
                0f
            }

            val centerX = (w / 2f) + xOffset

            slicePointsLeft.add(Offset(centerX - (sliceWidth / 2f), y))
            slicePointsRight.add(Offset(centerX + (sliceWidth / 2f), y))
        }

        // Render Multi-Section Bending Road Surface
        val roadPath = Path().apply {
            moveTo(slicePointsLeft[0].x, slicePointsLeft[0].y)
            for (i in 1..numSlices) {
                lineTo(slicePointsLeft[i].x, slicePointsLeft[i].y)
            }
            lineTo(slicePointsRight[numSlices].x, slicePointsRight[numSlices].y)
            for (i in numSlices - 1 downTo 0) {
                lineTo(slicePointsRight[i].x, slicePointsRight[i].y)
            }
            close()
        }

        // Road Asphalt Color
        drawPath(roadPath, theme.asphaltColor)

        // Render Glowing Neon Drift Zone on the road surface synchronized with the CAR (v = 0.70)
        val carV = 0.70f
        val isYellowZoneActive = isInsideCorner || cornerDistance < 180f
        if (isYellowZoneActive) {
            val entryV = if (isInsideCorner) 1.0f else (((180f - cornerDistance) / 180f).coerceIn(0f, 1f) * carV)
            val exitV = if (isInsideCorner) (cornerProgress.coerceIn(0f, 1f) * carV) else 0.0f

            val zonePath = Path()
            var startedPath = false

            for (i in 0..numSlices) {
                val v = i / numSlices.toFloat()
                if (v in exitV..entryV) {
                    if (!startedPath) {
                        zonePath.moveTo(slicePointsLeft[i].x, slicePointsLeft[i].y)
                        startedPath = true
                    } else {
                        zonePath.lineTo(slicePointsLeft[i].x, slicePointsLeft[i].y)
                    }
                }
            }

            if (startedPath) {
                for (i in numSlices downTo 0) {
                    val v = i / numSlices.toFloat()
                    if (v in exitV..entryV) {
                        zonePath.lineTo(slicePointsRight[i].x, slicePointsRight[i].y)
                    }
                }
                zonePath.close()

                val zoneColor = if (isLastDrift) Color(0xFFFF1744) else Color(0xFFFFD600)
                val zoneAlpha = if (isLastDrift) 0.85f else 0.65f

                drawPath(zonePath, zoneColor.copy(alpha = zoneAlpha))
                drawPath(
                    path = zonePath,
                    color = if (isLastDrift) Color(0xFFFF1744) else Color.White.copy(alpha = 0.5f),
                    style = Stroke(width = if (isLastDrift) 5f else 2f)
                )
            }
        }

        // Red/White Striped Curbs along the multi-section path
        val leftCurbPath = Path().apply {
            val topWidth = 14f
            val botWidth = 40f
            moveTo(slicePointsLeft[0].x - topWidth, slicePointsLeft[0].y)
            for (i in 0..numSlices) {
                val v = i / numSlices.toFloat()
                val curbW = topWidth + (botWidth - topWidth) * v
                lineTo(slicePointsLeft[i].x - curbW, slicePointsLeft[i].y)
            }
            for (i in numSlices downTo 0) {
                lineTo(slicePointsLeft[i].x, slicePointsLeft[i].y)
            }
            close()
        }

        val rightCurbPath = Path().apply {
            val topWidth = 14f
            val botWidth = 40f
            moveTo(slicePointsRight[0].x, slicePointsRight[0].y)
            for (i in 0..numSlices) {
                lineTo(slicePointsRight[i].x, slicePointsRight[i].y)
            }
            for (i in numSlices downTo 0) {
                val v = i / numSlices.toFloat()
                val curbW = topWidth + (botWidth - topWidth) * v
                lineTo(slicePointsRight[i].x + curbW, slicePointsRight[i].y)
            }
            close()
        }

        drawPath(leftCurbPath, theme.curbColor)
        drawPath(rightCurbPath, theme.curbColor)

        // Center Animated Dash Lines (Scrolling smoothly with distanceTravelled)
        val dashStep = 0.15f
        val offsetV = (distanceTravelled * 0.02f) % dashStep
        var vDash = -dashStep + offsetV

        while (vDash <= 1.0f) {
            if (vDash in 0.01f..0.98f) {
                val sliceFloat = vDash * numSlices
                val idx = sliceFloat.toInt().coerceIn(0, numSlices - 1)
                val frac = sliceFloat - idx

                val ptL1 = slicePointsLeft[idx]
                val ptL2 = slicePointsLeft[(idx + 1).coerceAtMost(numSlices)]
                val ptR1 = slicePointsRight[idx]
                val ptR2 = slicePointsRight[(idx + 1).coerceAtMost(numSlices)]

                val y = ptL1.y + (ptL2.y - ptL1.y) * frac
                val xL = ptL1.x + (ptL2.x - ptL1.x) * frac
                val xR = ptR1.x + (ptR2.x - ptR1.x) * frac
                val centerX = (xL + xR) / 2f

                val lineWidth = 4f + (vDash * 18f)
                val lineHeight = 8f + (vDash * 30f)

                drawRoundRect(
                    color = theme.dashColor,
                    topLeft = Offset(centerX - (lineWidth / 2f), y),
                    size = Size(lineWidth, lineHeight),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
            vDash += dashStep
        }

        // Speed Blur Lines during Nitro
        if (isNitroActive) {
            for (i in 0..12) {
                val startX = kotlin.random.Random.nextFloat() * w
                val startY = roadHorizonY + kotlin.random.Random.nextFloat() * (h - roadHorizonY)
                drawLine(
                    color = Color(0xFF00E5FF).copy(alpha = 0.6f),
                    start = Offset(startX, startY),
                    end = Offset(startX + (startX - w / 2f) * 0.2f, startY + 40f),
                    strokeWidth = 3f
                )
            }
        }
    }
}

@Composable
fun CarCanvas(
    car: Car,
    driftAngle: Float = 0f,
    isNitroActive: Boolean = false,
    modifier: Modifier = Modifier.size(180.dp, 220.dp)
) {
    val bodyColor = Color(car.colorHex)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        withTransform({
            rotate(degrees = driftAngle, pivot = Offset(w / 2f, h / 2f))
        }) {
            // 1. Tire Skid Marks & Smoke Clouds during Drifting
            if (kotlin.math.abs(driftAngle) > 8f) {
                drawCircle(Color.White.copy(alpha = 0.45f), w * 0.22f, Offset(w * 0.12f, h * 0.88f))
                drawCircle(Color.LightGray.copy(alpha = 0.35f), w * 0.28f, Offset(w * 0.88f, h * 0.88f))
                drawLine(Color.DarkGray.copy(alpha = 0.7f), Offset(w * 0.15f, h * 0.75f), Offset(w * 0.08f, h * 0.98f), strokeWidth = 8f, cap = StrokeCap.Round)
                drawLine(Color.DarkGray.copy(alpha = 0.7f), Offset(w * 0.85f, h * 0.75f), Offset(w * 0.92f, h * 0.98f), strokeWidth = 8f, cap = StrokeCap.Round)
            }

            // 2. Nitro Flames & Glowing Exhaust Burst
            if (isNitroActive) {
                val flamePathOuter = Path().apply {
                    moveTo(w * 0.32f, h * 0.92f)
                    lineTo(w * 0.5f, h * 1.35f)
                    lineTo(w * 0.68f, h * 0.92f)
                    close()
                }
                drawPath(flamePathOuter, Color(0xFF00E5FF).copy(alpha = 0.85f))
                val flamePathInner = Path().apply {
                    moveTo(w * 0.4f, h * 0.92f)
                    lineTo(w * 0.5f, h * 1.2f)
                    lineTo(w * 0.6f, h * 0.92f)
                    close()
                }
                drawPath(flamePathInner, Color.White)
                drawCircle(Color(0xFF76FF03), w * 0.04f, Offset(w * 0.35f, h * 1.2f))
                drawCircle(Color(0xFFFFD700), w * 0.04f, Offset(w * 0.65f, h * 1.22f))
            }

            // 3. Neon Underglow
            drawRoundRect(
                bodyColor.copy(alpha = 0.4f),
                topLeft = Offset(w * 0.15f, h * 0.18f),
                size = Size(w * 0.7f, h * 0.68f),
                cornerRadius = CornerRadius(24f, 24f)
            )

            // 4. Wheels with Alloy Spokes & Brake Calipers
            val tireColor = Color(0xFF111111)
            val rimColor = Color(0xFFE0E0E0)
            val caliperColor = Color(0xFFFF3D00)

            val wheelOffsets = listOf(
                Offset(w * 0.06f, h * 0.18f), // Front Left
                Offset(w * 0.78f, h * 0.18f), // Front Right
                Offset(w * 0.06f, h * 0.62f), // Rear Left
                Offset(w * 0.78f, h * 0.62f)  // Rear Right
            )

            wheelOffsets.forEach { offset ->
                drawRoundRect(tireColor, offset, Size(w * 0.16f, h * 0.24f), CornerRadius(10f, 10f))
                drawCircle(rimColor, w * 0.06f, Offset(offset.x + w * 0.08f, offset.y + h * 0.12f))
                drawCircle(Color.Black, w * 0.025f, Offset(offset.x + w * 0.08f, offset.y + h * 0.12f))
                drawCircle(caliperColor, w * 0.02f, Offset(offset.x + w * 0.08f, offset.y + h * 0.06f))
            }

            // 5. Main Car Chassis Body Path with Aerodynamic Curves
            val carBodyPath = Path().apply {
                moveTo(w * 0.28f, h * 0.1f)  // Nose tip
                lineTo(w * 0.72f, h * 0.1f)
                quadraticTo(w * 0.8f, h * 0.2f, w * 0.86f, h * 0.35f)
                lineTo(w * 0.88f, h * 0.78f)
                quadraticTo(w * 0.85f, h * 0.9f, w * 0.72f, h * 0.92f)
                lineTo(w * 0.28f, h * 0.92f)
                quadraticTo(w * 0.15f, h * 0.9f, w * 0.12f, h * 0.78f)
                lineTo(w * 0.14f, h * 0.35f)
                quadraticTo(w * 0.2f, h * 0.2f, w * 0.28f, h * 0.1f)
                close()
            }
            drawPath(carBodyPath, bodyColor)

            // Body Metallic Shading / Gradient Highlight
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.25f), Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                    startY = h * 0.15f,
                    endY = h * 0.85f
                ),
                topLeft = Offset(w * 0.15f, h * 0.15f),
                size = Size(w * 0.7f, h * 0.72f)
            )

            // 6. Cabin Roof & Glass
            drawRoundRect(
                Color(0xFF181818),
                topLeft = Offset(w * 0.24f, h * 0.33f),
                size = Size(w * 0.52f, h * 0.35f),
                cornerRadius = CornerRadius(18f, 18f)
            )

            // Front Windshield Glass (Cyan Gradient)
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF81D4FA).copy(alpha = 0.9f), Color(0xFF00ACC1).copy(alpha = 0.7f))
                ),
                topLeft = Offset(w * 0.27f, h * 0.35f),
                size = Size(w * 0.46f, h * 0.15f),
                cornerRadius = CornerRadius(12f, 12f)
            )

            // Rear Window Glass
            drawRoundRect(
                Color(0xFF263238).copy(alpha = 0.8f),
                topLeft = Offset(w * 0.28f, h * 0.53f),
                size = Size(w * 0.44f, h * 0.12f),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // 7. Headlights with Glow & Light Beams
            drawCircle(Color(0xFFFFF59D), w * 0.07f, Offset(w * 0.22f, h * 0.13f))
            drawCircle(Color.White, w * 0.035f, Offset(w * 0.22f, h * 0.13f))
            drawCircle(Color(0xFFFFF59D), w * 0.07f, Offset(w * 0.78f, h * 0.13f))
            drawCircle(Color.White, w * 0.035f, Offset(w * 0.78f, h * 0.13f))

            val beamPathLeft = Path().apply {
                moveTo(w * 0.22f, h * 0.13f)
                lineTo(w * 0.05f, h * -0.1f)
                lineTo(w * 0.3f, h * 0.05f)
                close()
            }
            drawPath(beamPathLeft, Color(0xFFFFF59D).copy(alpha = 0.15f))

            val beamPathRight = Path().apply {
                moveTo(w * 0.78f, h * 0.13f)
                lineTo(w * 0.95f, h * -0.1f)
                lineTo(w * 0.7f, h * 0.05f)
                close()
            }
            drawPath(beamPathRight, Color(0xFFFFF59D).copy(alpha = 0.15f))

            // 8. Rear LED Taillights & Dual Exhaust Pipes
            drawRoundRect(
                Color(0xFFFF1744),
                topLeft = Offset(w * 0.2f, h * 0.9f),
                size = Size(w * 0.6f, h * 0.03f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawCircle(Color(0xFF37474F), w * 0.045f, Offset(w * 0.38f, h * 0.93f))
            drawCircle(Color(0xFF37474F), w * 0.045f, Offset(w * 0.62f, h * 0.93f))

            // 9. Multi-Element GT Rear Spoiler (Carbon Fiber Look)
            drawRoundRect(
                Color(0xFF212121),
                topLeft = Offset(w * 0.16f, h * 0.88f),
                size = Size(w * 0.68f, h * 0.06f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRect(
                Color(0xFF424242),
                topLeft = Offset(w * 0.32f, h * 0.86f),
                size = Size(w * 0.06f, h * 0.04f)
            )
            drawRect(
                Color(0xFF424242),
                topLeft = Offset(w * 0.62f, h * 0.86f),
                size = Size(w * 0.06f, h * 0.04f)
            )
        }
    }
}

@Composable
fun CircularTachometer(
    engineRpm: Float,
    maxRpm: Float = 9000f,
    greenZoneMin: Float = 6400f,
    greenZoneMax: Float = 7300f,
    modifier: Modifier = Modifier.size(180.dp)
) {
    val inGreenZone = engineRpm in greenZoneMin..greenZoneMax
    val rpmFraction = (engineRpm / maxRpm).coerceIn(0f, 1f)

    val startAngle = 140f
    val sweepAngle = 260f

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val center = Offset(w / 2f, h / 2f)
            val strokeWidth = w * 0.08f
            val radius = (minOf(w, h) - strokeWidth) / 2f

            val arcSize = Size(radius * 2f, radius * 2f)
            val arcTopLeft = Offset(center.x - radius, center.y - radius)

            // 1. Outer Dark Dial Background
            drawCircle(
                color = Color(0xFF12121A),
                radius = minOf(w, h) / 2f,
                center = center
            )
            drawCircle(
                color = Color(0xFF00E5FF).copy(alpha = 0.3f),
                radius = minOf(w, h) / 2f,
                center = center,
                style = Stroke(width = 3f)
            )

            // 2. Base Background Track Arc
            drawArc(
                color = Color.White.copy(alpha = 0.15f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 3. Green Zone Target Arc (6,400 - 7,300 RPM)
            val greenStartFrac = greenZoneMin / maxRpm
            val greenEndFrac = greenZoneMax / maxRpm
            val greenStartAngle = startAngle + sweepAngle * greenStartFrac
            val greenSweepAngle = sweepAngle * (greenEndFrac - greenStartFrac)

            drawArc(
                color = Color(0xFF76FF03), // Bright Neon Green
                startAngle = greenStartAngle,
                sweepAngle = greenSweepAngle,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth + 4f, cap = StrokeCap.Butt)
            )

            // 4. Redline Track Arc (7,300 - 9,000 RPM)
            val redlineStartAngle = startAngle + sweepAngle * greenEndFrac
            val redlineSweepAngle = sweepAngle * (1f - greenEndFrac)

            drawArc(
                color = Color(0xFFFF0055).copy(alpha = 0.8f),
                startAngle = redlineStartAngle,
                sweepAngle = redlineSweepAngle,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )

            // 6. Tick Marks (0 to 9 x1000 RPM)
            val numTicks = 9
            val tickRadiusOuter = radius - strokeWidth / 2f - 2f
            for (i in 0..numTicks) {
                val fraction = i / numTicks.toFloat()
                val angleDeg = startAngle + sweepAngle * fraction
                val angleRad = angleDeg * (kotlin.math.PI / 180f)

                val tickLength = if (i % 3 == 0) w * 0.08f else w * 0.05f
                val strokeW = if (i % 3 == 0) 3f else 1.5f

                val isTickInGreen = (fraction * maxRpm) in greenZoneMin..greenZoneMax
                val tickColor = when {
                    isTickInGreen -> Color(0xFF76FF03)
                    fraction * maxRpm > greenZoneMax -> Color(0xFFFF0055)
                    else -> Color.White.copy(alpha = 0.7f)
                }

                val startX = center.x + (tickRadiusOuter - tickLength) * cos(angleRad).toFloat()
                val startY = center.y + (tickRadiusOuter - tickLength) * sin(angleRad).toFloat()
                val endX = center.x + tickRadiusOuter * cos(angleRad).toFloat()
                val endY = center.y + tickRadiusOuter * sin(angleRad).toFloat()

                drawLine(
                    color = tickColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = strokeW
                )
            }

            // 7. Rotating Needle / Pointer
            val needleAngleDeg = startAngle + sweepAngle * rpmFraction
            val needleAngleRad = needleAngleDeg * (kotlin.math.PI / 180f)
            val needleLength = radius - strokeWidth / 2f - 4f

            val needleEndX = center.x + needleLength * cos(needleAngleRad).toFloat()
            val needleEndY = center.y + needleLength * sin(needleAngleRad).toFloat()

            val needleColor = if (inGreenZone) Color(0xFF76FF03) else Color(0xFFFF3D00)

            // Needle Glow line
            drawLine(
                color = needleColor.copy(alpha = 0.4f),
                start = center,
                end = Offset(needleEndX, needleEndY),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
            // Main Needle line
            drawLine(
                color = needleColor,
                start = center,
                end = Offset(needleEndX, needleEndY),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )

            // Center Hub Cap
            drawCircle(
                color = Color.Black,
                radius = w * 0.08f,
                center = center
            )
            drawCircle(
                color = needleColor,
                radius = w * 0.05f,
                center = center
            )
            drawCircle(
                color = Color.White,
                radius = w * 0.02f,
                center = center
            )
        }

        // 8. Center Text Overlay
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(100.dp)) // push slightly down below needle pivot
            Text(
                text = "${engineRpm.toInt()}",
                color = if (inGreenZone) Color(0xFF76FF03) else Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "RPM",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

