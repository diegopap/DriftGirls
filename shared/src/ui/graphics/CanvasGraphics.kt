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

        // Ground / Road Perspective
        val roadHorizonY = h * 0.42f
        val roadBottomY = h

        val roadTopWidth = w * 0.15f
        val roadBottomWidth = w * 0.92f

        // Curve shift calculated from corner progress
        val curveOffset = if (isInsideCorner) {
            sin(cornerProgress * kotlin.math.PI.toFloat()) * (w * 0.35f)
        } else if (cornerDistance < 100f) {
            (100f - cornerDistance) * 0.8f
        } else {
            0f
        }

        val topLeftX = (w / 2f) - (roadTopWidth / 2f) + curveOffset * 0.3f
        val topRightX = (w / 2f) + (roadTopWidth / 2f) + curveOffset * 0.3f

        val bottomLeftX = (w / 2f) - (roadBottomWidth / 2f) - curveOffset
        val bottomRightX = (w / 2f) + (roadBottomWidth / 2f) - curveOffset

        // Road Surface Polygon
        val roadPath = Path().apply {
            moveTo(topLeftX, roadHorizonY)
            lineTo(topRightX, roadHorizonY)
            lineTo(bottomRightX, roadBottomY)
            lineTo(bottomLeftX, roadBottomY)
            close()
        }

        // Road Asphalt Color
        drawPath(roadPath, Color(0xFF1D1E22))

        // Red/White Striped Curbs
        val leftCurbPath = Path().apply {
            moveTo(topLeftX - 12f, roadHorizonY)
            lineTo(topLeftX, roadHorizonY)
            lineTo(bottomLeftX, roadBottomY)
            lineTo(bottomLeftX - 35f, roadBottomY)
            close()
        }
        val rightCurbPath = Path().apply {
            moveTo(topRightX, roadHorizonY)
            lineTo(topRightX + 12f, roadHorizonY)
            lineTo(bottomRightX + 35f, roadBottomY)
            lineTo(bottomRightX, roadBottomY)
            close()
        }

        drawPath(leftCurbPath, Color(0xFFFF1744)) // Red Curb
        drawPath(rightCurbPath, Color(0xFFFF1744))

        // Center Animated Dash Lines
        for (i in 0..8) {
            val progress = (i / 8f)
            val lineY = roadHorizonY + (h - roadHorizonY) * progress
            val lineX = (w / 2f) + (curveOffset * progress * 0.6f)
            val lineWidth = 4f + (progress * 18f)
            val lineHeight = 8f + (progress * 30f)

            drawRoundRect(
                color = Color(0xFFFFD600), // Neon Yellow
                topLeft = Offset(lineX - (lineWidth / 2f), lineY),
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
