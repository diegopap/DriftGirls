package com.driftgirls.ui.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.driftgirls.model.Car
import com.driftgirls.model.GirlCompanion
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CompanionAvatarCanvas(
    companion: GirlCompanion,
    expression: String = "HAPPY", // HAPPY, WINK, SURPRISED, EXCITED
    modifier: Modifier = Modifier.size(100.dp)
) {
    val accentColor = Color(companion.accentColorHex)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        // Background circle glow
        drawCircle(
            color = accentColor.copy(alpha = 0.25f),
            radius = width * 0.48f,
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = Color.White,
            radius = width * 0.42f,
            center = Offset(centerX, centerY)
        )

        // Hair Back
        when (companion.id) {
            "aoi" -> {
                // Pink twin tails
                drawCircle(Color(0xFFFF4081), width * 0.22f, Offset(centerX - width * 0.32f, centerY - height * 0.05f))
                drawCircle(Color(0xFFFF4081), width * 0.22f, Offset(centerX + width * 0.32f, centerY - height * 0.05f))
            }
            "sora" -> {
                // Cyan long hair flow
                val hairPath = Path().apply {
                    moveTo(centerX - width * 0.35f, centerY - height * 0.1f)
                    quadraticTo(centerX - width * 0.4f, centerY + height * 0.4f, centerX - width * 0.2f, centerY + height * 0.45f)
                    lineTo(centerX + width * 0.2f, centerY + height * 0.45f)
                    quadraticTo(centerX + width * 0.4f, centerY + height * 0.4f, centerX + width * 0.35f, centerY - height * 0.1f)
                    close()
                }
                drawPath(hairPath, Color(0xFF00E5FF))
            }
            "elena" -> {
                // Orange ponytail
                drawCircle(Color(0xFFFF9100), width * 0.25f, Offset(centerX, centerY - height * 0.32f))
            }
            "mia" -> {
                // Purple bob
                drawRoundRect(
                    Color(0xFF7C4DFF),
                    topLeft = Offset(centerX - width * 0.32f, centerY - height * 0.35f),
                    size = Size(width * 0.64f, height * 0.65f),
                    cornerRadius = CornerRadius(24f, 24f)
                )
            }
        }

        // Face Skin
        val faceRadius = width * 0.28f
        drawCircle(
            color = Color(0xFFFFE0B2), // Warm fair skin tone
            radius = faceRadius,
            center = Offset(centerX, centerY + height * 0.02f)
        )

        // Blush
        drawCircle(
            color = Color(0xFFFF80AB).copy(alpha = 0.5f),
            radius = width * 0.06f,
            center = Offset(centerX - width * 0.16f, centerY + height * 0.08f)
        )
        drawCircle(
            color = Color(0xFFFF80AB).copy(alpha = 0.5f),
            radius = width * 0.06f,
            center = Offset(centerX + width * 0.16f, centerY + height * 0.08f)
        )

        // Eyes
        val eyeColor = when (companion.id) {
            "aoi" -> Color(0xFFD81B60)
            "sora" -> Color(0xFF00838F)
            "elena" -> Color(0xFFE65100)
            else -> Color(0xFF4A148C)
        }

        if (expression == "WINK") {
            // Left eye open, right eye closed curve
            drawCircle(eyeColor, width * 0.045f, Offset(centerX - width * 0.1f, centerY - height * 0.02f))
            drawArc(
                color = Color.Black,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(centerX + width * 0.06f, centerY - height * 0.04f),
                size = Size(width * 0.08f, height * 0.06f),
                style = Stroke(width = 4f)
            )
        } else {
            // Both eyes
            val eyeSize = if (expression == "SURPRISED") width * 0.06f else width * 0.045f
            drawCircle(eyeColor, eyeSize, Offset(centerX - width * 0.1f, centerY - height * 0.02f))
            drawCircle(eyeColor, eyeSize, Offset(centerX + width * 0.1f, centerY - height * 0.02f))
            drawCircle(Color.White, eyeSize * 0.4f, Offset(centerX - width * 0.11f, centerY - height * 0.035f))
            drawCircle(Color.White, eyeSize * 0.4f, Offset(centerX + width * 0.09f, centerY - height * 0.035f))
        }

        // Mouth
        when (expression) {
            "SURPRISED" -> {
                drawCircle(Color(0xFFD32F2F), width * 0.04f, Offset(centerX, centerY + height * 0.12f))
            }
            "EXCITED" -> {
                val mouthPath = Path().apply {
                    moveTo(centerX - width * 0.08f, centerY + height * 0.1f)
                    quadraticTo(centerX, centerY + height * 0.2f, centerX + width * 0.08f, centerY + height * 0.1f)
                    close()
                }
                drawPath(mouthPath, Color(0xFFD32F2F))
            }
            else -> { // HAPPY
                drawArc(
                    color = Color.Black,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(centerX - width * 0.06f, centerY + height * 0.08f),
                    size = Size(width * 0.12f, height * 0.08f),
                    style = Stroke(width = 4f)
                )
            }
        }

        // Hair Bangs Front
        val bangColor = when (companion.id) {
            "aoi" -> Color(0xFFFF4081)
            "sora" -> Color(0xFF00E5FF)
            "elena" -> Color(0xFFFF9100)
            else -> Color(0xFF7C4DFF)
        }
        val bangsPath = Path().apply {
            moveTo(centerX - faceRadius, centerY - height * 0.08f)
            quadraticTo(centerX - width * 0.1f, centerY - height * 0.02f, centerX, centerY - height * 0.16f)
            quadraticTo(centerX + width * 0.1f, centerY - height * 0.02f, centerX + faceRadius, centerY - height * 0.08f)
            quadraticTo(centerX, centerY - height * 0.38f, centerX - faceRadius, centerY - height * 0.08f)
            close()
        }
        drawPath(bangsPath, bangColor)

        // Accent hair ribbon or bow
        drawCircle(accentColor, width * 0.05f, Offset(centerX - width * 0.22f, centerY - height * 0.22f))
        drawCircle(accentColor, width * 0.05f, Offset(centerX + width * 0.22f, centerY - height * 0.22f))

        // Outer Border
        drawCircle(
            color = accentColor,
            radius = width * 0.44f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 6f)
        )
    }
}

@Composable
fun ArcadeTrackCanvas(
    cornerProgress: Float,
    isInsideCorner: Boolean,
    cornerDistance: Float,
    cornerDirection: com.driftgirls.model.CornerDirection = com.driftgirls.model.CornerDirection.RIGHT,
    isNitroActive: Boolean,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Horizon & Sky
        val skyGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F0C20), // Deep Cyber Midnight
                Color(0xFF241442),
                Color(0xFF511A65)
            )
        )
        drawRect(skyGradient, topLeft = Offset.Zero, size = Size(w, h * 0.42f))

        // Sun / Neon Grid City horizon line
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFF007F), Color(0xFFFF9100), Color.Transparent)
            ),
            radius = w * 0.3f,
            center = Offset(w * 0.5f, h * 0.42f)
        )

        // Ground / Road Perspective Slices
        val roadHorizonY = h * 0.42f
        val roadBottomY = h

        val roadTopWidth = w * 0.15f
        val roadBottomWidth = w * 0.92f

        val dirMult = if (cornerDirection == com.driftgirls.model.CornerDirection.LEFT) -1f else 1f
        val maxBend = w * 0.45f

        // Multi-Section Curve Calculation:
        // ONLY the upper part of the track near the horizon (v < 0.40) curves to show the turn!
        // The middle and bottom parts of the track (v >= 0.40) remain 100% FIXED and CENTERED (xOffset = 0)!
        val numSlices = 28
        val slicePointsLeft = ArrayList<Offset>(numSlices + 1)
        val slicePointsRight = ArrayList<Offset>(numSlices + 1)

        val approachFactor = ((150f - cornerDistance) / 150f).coerceIn(0f, 1f)

        for (i in 0..numSlices) {
            val v = i / numSlices.toFloat() // 0.0 at horizon, 1.0 at car
            val y = roadHorizonY + (roadBottomY - roadHorizonY) * v
            val sliceWidth = roadTopWidth + (roadBottomWidth - roadTopWidth) * v

            val xOffset = if (v < 0.40f) {
                val upperFactor = 1f - (v / 0.40f) // 1.0 at horizon (v=0), 0.0 at v=0.40
                val bendAmount = upperFactor * upperFactor * (maxBend * 0.85f)

                if (isInsideCorner) {
                    // turnIntensity starts at 1.0 at entry (100% matching approach offset),
                    // then smoothly decays to 0.0 at exit, returning the horizon to center without any jumps!
                    val turnIntensity = cos(cornerProgress * kotlin.math.PI.toFloat() / 2f)
                    dirMult * bendAmount * turnIntensity
                } else if (cornerDistance < 150f) {
                    dirMult * bendAmount * approachFactor
                } else {
                    0f
                }
            } else {
                0f // Middle & bottom of track stay 100% fixed & straight!
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
        drawPath(roadPath, Color(0xFF1D1E22))

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

        drawPath(leftCurbPath, Color(0xFFFF1744)) // Red Curb
        drawPath(rightCurbPath, Color(0xFFFF1744))

        // Center Animated Dash Lines
        for (i in 0..numSlices step 3) {
            val ptL = slicePointsLeft[i]
            val ptR = slicePointsRight[i]
            val centerX = (ptL.x + ptR.x) / 2f
            val v = i / numSlices.toFloat()

            val lineWidth = 4f + (v * 18f)
            val lineHeight = 8f + (v * 30f)

            drawRoundRect(
                color = Color(0xFFFFD600), // Neon Yellow
                topLeft = Offset(centerX - (lineWidth / 2f), ptL.y),
                size = Size(lineWidth, lineHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )
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
            // Tire Skid Marks / Tire Smoke
            if (kotlin.math.abs(driftAngle) > 10f) {
                drawCircle(Color.LightGray.copy(alpha = 0.5f), w * 0.18f, Offset(w * 0.15f, h * 0.85f))
                drawCircle(Color.LightGray.copy(alpha = 0.5f), w * 0.18f, Offset(w * 0.85f, h * 0.85f))
            }

            // Nitro Tail Flames
            if (isNitroActive) {
                val flamePath = Path().apply {
                    moveTo(w * 0.35f, h * 0.9f)
                    lineTo(w * 0.5f, h * 1.25f)
                    lineTo(w * 0.65f, h * 0.9f)
                    close()
                }
                drawPath(flamePath, Color(0xFF00E5FF)) // Nitro Cyan Flame
                drawPath(flamePath, Color.White)
            }

            // Wheels
            val wheelColor = Color(0xFF212121)
            drawRoundRect(wheelColor, Offset(w * 0.08f, h * 0.18f), Size(w * 0.14f, h * 0.22f), CornerRadius(8f, 8f))
            drawRoundRect(wheelColor, Offset(w * 0.78f, h * 0.18f), Size(w * 0.14f, h * 0.22f), CornerRadius(8f, 8f))
            drawRoundRect(wheelColor, Offset(w * 0.08f, h * 0.62f), Size(w * 0.14f, h * 0.22f), CornerRadius(8f, 8f))
            drawRoundRect(wheelColor, Offset(w * 0.78f, h * 0.62f), Size(w * 0.14f, h * 0.22f), CornerRadius(8f, 8f))

            // Main Car Chassis
            val carBodyPath = Path().apply {
                moveTo(w * 0.25f, h * 0.12f) // Nose
                lineTo(w * 0.75f, h * 0.12f)
                lineTo(w * 0.88f, h * 0.38f) // Hood to cabin
                lineTo(w * 0.88f, h * 0.82f) // Side to tail
                lineTo(w * 0.72f, h * 0.9f)  // Rear bumper
                lineTo(w * 0.28f, h * 0.9f)
                lineTo(w * 0.12f, h * 0.82f)
                lineTo(w * 0.12f, h * 0.38f)
                close()
            }
            drawPath(carBodyPath, bodyColor)

            // Roof & Windshield
            drawRoundRect(
                Color(0xFF101010),
                topLeft = Offset(w * 0.22f, h * 0.35f),
                size = Size(w * 0.56f, h * 0.32f),
                cornerRadius = CornerRadius(16f, 16f)
            )

            // Front Windshield Glass
            drawRoundRect(
                Color(0xFF81D4FA).copy(alpha = 0.8f),
                topLeft = Offset(w * 0.26f, h * 0.37f),
                size = Size(w * 0.48f, h * 0.14f),
                cornerRadius = CornerRadius(12f, 12f)
            )

            // Headlights
            drawCircle(Color(0xFFFFF59D), w * 0.08f, Offset(w * 0.22f, h * 0.14f))
            drawCircle(Color(0xFFFFF59D), w * 0.08f, Offset(w * 0.78f, h * 0.14f))

            // Rear Spoiler
            drawRoundRect(
                Color(0xFF212121),
                topLeft = Offset(w * 0.15f, h * 0.86f),
                size = Size(w * 0.7f, h * 0.05f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}
