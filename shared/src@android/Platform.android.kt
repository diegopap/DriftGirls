package com.driftgirls

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.sin

object AndroidContextHolder {
    var context: Context? = null
}

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun saveGameData(data: String) {
    val ctx = AndroidContextHolder.context ?: return
    val prefs = ctx.getSharedPreferences("drift_girls_save_v2", Context.MODE_PRIVATE)
    prefs.edit().putString("save_payload", data).apply()
}

actual fun loadGameData(): String? {
    val ctx = AndroidContextHolder.context ?: return null
    val prefs = ctx.getSharedPreferences("drift_girls_save_v2", Context.MODE_PRIVATE)
    return prefs.getString("save_payload", null)
}

actual fun getFormattedCurrentDate(): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date())
}

actual fun playBackgroundMusic(musicTrack: String) {
    RetroMidiSynthesizer.playTrack(musicTrack)
}

actual fun stopBackgroundMusic() {
    RetroMidiSynthesizer.stopTrack()
}

actual fun setMusicMuted(muted: Boolean) {
    RetroMidiSynthesizer.setMuted(muted)
}

actual fun isMusicMuted(): Boolean {
    return RetroMidiSynthesizer.isMuted()
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}

object RetroMidiSynthesizer {
    private var audioTrack: AudioTrack? = null
    @Volatile
    private var isPlaying = false
    private var playbackThread: Thread? = null
    private var currentTrack: String = ""
    private var isMuted: Boolean = false

    fun playTrack(trackName: String) {
        if (isPlaying && currentTrack == trackName && !isMuted) return
        stopTrack()

        currentTrack = trackName
        if (isMuted) return

        isPlaying = true
        playbackThread = Thread {
            runAudioLoop(trackName)
        }.apply {
            priority = Thread.MIN_PRIORITY
            start()
        }
    }

    fun stopTrack() {
        isPlaying = false
        playbackThread?.interrupt()
        playbackThread = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {
            // Fail-safe
        }
        audioTrack = null
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
        if (muted) {
            stopTrack()
        } else if (currentTrack.isNotBlank()) {
            playTrack(currentTrack)
        }
    }

    fun isMuted(): Boolean = isMuted

    private fun noteToFreq(midiNote: Int): Double {
        if (midiNote <= 0) return 0.0
        return 440.0 * Math.pow(2.0, (midiNote - 69) / 12.0)
    }

    private fun runAudioLoop(trackName: String) {
        val sampleRate = 22050
        val minBuffer = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val bufferSize = Math.max(minBuffer, 4096)

        try {
            val track = AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM
            )
            audioTrack = track
            track.play()

            val (bpm, leadNotes, bassNotes) = when (trackName) {
                "race_track_tutorial" -> Triple(
                    135,
                    intArrayOf(60, 64, 67, 72, 71, 67, 64, 62, 60, 64, 67, 69, 67, 64, 60, 62, 65, 69, 72, 74, 72, 69, 65, 67, 67, 71, 74, 77, 76, 74, 71, 67),
                    intArrayOf(36, 48, 36, 48, 36, 48, 36, 48, 41, 53, 41, 53, 43, 55, 43, 55)
                )
                "race_track_neon_city" -> Triple(
                    150,
                    intArrayOf(62, 65, 69, 74, 72, 69, 65, 64, 62, 65, 69, 77, 76, 74, 69, 72, 57, 60, 64, 69, 67, 64, 60, 62, 58, 62, 65, 70, 69, 65, 62, 60),
                    intArrayOf(38, 50, 38, 50, 38, 50, 38, 50, 33, 45, 33, 45, 34, 46, 34, 46)
                )
                "race_track_mountain" -> Triple(
                    145,
                    intArrayOf(64, 67, 69, 71, 74, 71, 69, 67, 64, 67, 69, 74, 76, 74, 71, 69, 67, 71, 74, 76, 79, 76, 74, 71, 62, 66, 69, 74, 71, 69, 66, 64),
                    intArrayOf(40, 52, 40, 52, 40, 52, 40, 52, 43, 55, 43, 55, 38, 50, 38, 50)
                )
                "race_track_ocean" -> Triple(
                    128,
                    intArrayOf(67, 71, 74, 76, 74, 71, 67, 69, 72, 76, 79, 81, 79, 76, 72, 74, 74, 78, 81, 83, 81, 78, 74, 71, 64, 67, 71, 76, 74, 71, 67, 64),
                    intArrayOf(43, 55, 43, 55, 48, 60, 48, 60, 50, 62, 50, 62, 40, 52, 40, 52)
                )
                "race_track_endless" -> Triple(
                    160,
                    intArrayOf(66, 69, 73, 78, 76, 73, 69, 68, 66, 69, 73, 76, 78, 80, 78, 76, 62, 66, 69, 74, 73, 69, 66, 64, 64, 68, 71, 76, 74, 71, 68, 66),
                    intArrayOf(42, 54, 42, 54, 42, 54, 42, 54, 38, 50, 38, 50, 40, 52, 40, 52)
                )
                else -> Triple( // "menu" & default
                    125,
                    intArrayOf(57, 60, 64, 67, 69, 67, 64, 60, 53, 57, 60, 64, 65, 64, 60, 57, 55, 59, 62, 65, 67, 65, 62, 59, 52, 55, 59, 62, 64, 62, 59, 55),
                    intArrayOf(33, 45, 33, 45, 29, 41, 29, 41, 31, 43, 31, 43, 28, 40, 28, 40)
                )
            }

            val samplesPerBeat = (sampleRate * 60) / (bpm * 2)
            val pcmBuffer = ShortArray(2048)

            var sampleCount = 0L
            var phaseLead = 0.0
            var phaseBass = 0.0

            while (isPlaying) {
                for (i in 0 until pcmBuffer.size) {
                    val currentBeatIndex = (sampleCount / samplesPerBeat).toInt()
                    val leadNote = leadNotes[currentBeatIndex % leadNotes.size]
                    val bassNote = bassNotes[currentBeatIndex % bassNotes.size]

                    val freqLead = noteToFreq(leadNote)
                    val freqBass = noteToFreq(bassNote)

                    if (freqLead > 0) {
                        phaseLead += (2.0 * PI * freqLead) / sampleRate
                        if (phaseLead > 2.0 * PI) phaseLead -= 2.0 * PI
                    }
                    if (freqBass > 0) {
                        phaseBass += (2.0 * PI * freqBass) / sampleRate
                        if (phaseBass > 2.0 * PI) phaseBass -= 2.0 * PI
                    }

                    val leadSquare = if (sin(phaseLead) > 0.0) 0.5 else -0.5
                    val bassTriangle = (2.0 / PI) * asin(sin(phaseBass))

                    val mixed = (leadSquare * 0.45 + bassTriangle * 0.55) * 12000.0
                    pcmBuffer[i] = mixed.toInt().coerceIn(-32768, 32767).toShort()

                    sampleCount++
                }

                if (isPlaying) {
                    track.write(pcmBuffer, 0, pcmBuffer.size)
                }
            }
        } catch (e: Exception) {
            // Fail-safe
        }
    }
}
