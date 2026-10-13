package com.driftgirls

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.dataWithBytes
import platform.Foundation.languageCode
import platform.Foundation.preferredLanguages
import platform.UIKit.UIDevice
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.pow
import kotlin.math.sin

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun saveGameData(data: String) {
    NSUserDefaults.standardUserDefaults.setObject(data, forKey = "save_payload")
}

actual fun loadGameData(): String? {
    return NSUserDefaults.standardUserDefaults.stringForKey("save_payload")
}

actual fun getFormattedCurrentDate(): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "yyyy-MM-dd HH:mm"
    return formatter.stringFromDate(NSDate())
}

actual fun getSystemLanguageCode(): String {
    val preferred = (NSLocale.preferredLanguages.firstOrNull() as? String)
    val lang = preferred ?: NSLocale.currentLocale.languageCode
    return lang.lowercase()
}

actual fun applyLanguageSetting(setting: com.driftgirls.i18n.LanguageSetting) {
    val defaults = NSUserDefaults.standardUserDefaults
    when (setting) {
        com.driftgirls.i18n.LanguageSetting.SYSTEM -> {
            defaults.removeObjectForKey("AppleLanguages")
        }
        com.driftgirls.i18n.LanguageSetting.ES -> {
            defaults.setObject(listOf("es"), forKey = "AppleLanguages")
        }
        com.driftgirls.i18n.LanguageSetting.EN -> {
            defaults.setObject(listOf("en"), forKey = "AppleLanguages")
        }
    }
}

actual fun playBackgroundMusic(musicTrack: String) {
    IosRetroMidiSynthesizer.playTrack(musicTrack)
}

actual fun stopBackgroundMusic() {
    IosRetroMidiSynthesizer.stopTrack()
}

actual fun setMusicMuted(muted: Boolean) {
    IosRetroMidiSynthesizer.setMuted(muted)
}

actual fun isMusicMuted(): Boolean {
    return IosRetroMidiSynthesizer.isMuted()
}

actual fun setMusicVolume(volume: Float) {
    IosRetroMidiSynthesizer.setVolume(volume)
}

actual fun getMusicVolume(): Float {
    return IosRetroMidiSynthesizer.getVolume()
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {}

object IosRetroMidiSynthesizer {
    private var audioPlayer: AVAudioPlayer? = null
    private var currentTrack: String = ""
    private var isMuted: Boolean = false
    private var musicVolume: Float = 1.0f

    @OptIn(ExperimentalForeignApi::class)
    fun playTrack(trackName: String) {
        if (currentTrack == trackName && audioPlayer?.isPlaying() == true && !isMuted) return
        stopTrack()

        currentTrack = trackName
        if (isMuted) return

        try {
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, error = null)

            val wavBytes = generateWavDataForTrack(trackName)
            val nsData = wavBytes.toNSData()

            val player = AVAudioPlayer(data = nsData, error = null)
            player.numberOfLoops = -1 // Loop indefinitely
            player.volume = musicVolume
            player.prepareToPlay()
            player.play()
            audioPlayer = player
        } catch (e: Exception) {
            // Fail-safe
        }
    }

    fun stopTrack() {
        try {
            audioPlayer?.stop()
        } catch (e: Exception) {
            // Fail-safe
        }
        audioPlayer = null
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

    fun setVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        musicVolume = clamped
        audioPlayer?.volume = clamped
    }

    fun getVolume(): Float = musicVolume

    private fun noteToFreq(midiNote: Int): Double {
        if (midiNote <= 0) return 0.0
        return 440.0 * 2.0.pow((midiNote - 69) / 12.0)
    }

    private fun generateWavDataForTrack(trackName: String): ByteArray {
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

        val sampleRate = 22050
        val samplesPerBeat = (sampleRate * 60) / (bpm * 2)
        val totalBeats = leadNotes.size
        val totalPcmSamples = totalBeats * samplesPerBeat
        val pcmByteLength = totalPcmSamples * 2
        val wavHeaderLength = 44
        val totalFileLength = wavHeaderLength + pcmByteLength

        val wavBuffer = ByteArray(totalFileLength)

        // Write WAV Header
        // "RIFF"
        wavBuffer[0] = 'R'.code.toByte()
        wavBuffer[1] = 'I'.code.toByte()
        wavBuffer[2] = 'F'.code.toByte()
        wavBuffer[3] = 'F'.code.toByte()

        val fileSizeMinus8 = totalFileLength - 8
        wavBuffer[4] = (fileSizeMinus8 and 0xFF).toByte()
        wavBuffer[5] = ((fileSizeMinus8 ushr 8) and 0xFF).toByte()
        wavBuffer[6] = ((fileSizeMinus8 ushr 16) and 0xFF).toByte()
        wavBuffer[7] = ((fileSizeMinus8 ushr 24) and 0xFF).toByte()

        // "WAVE"
        wavBuffer[8] = 'W'.code.toByte()
        wavBuffer[9] = 'A'.code.toByte()
        wavBuffer[10] = 'V'.code.toByte()
        wavBuffer[11] = 'E'.code.toByte()

        // "fmt "
        wavBuffer[12] = 'f'.code.toByte()
        wavBuffer[13] = 'm'.code.toByte()
        wavBuffer[14] = 't'.code.toByte()
        wavBuffer[15] = ' '.code.toByte()

        // Subchunk1Size = 16
        wavBuffer[16] = 16
        wavBuffer[17] = 0
        wavBuffer[18] = 0
        wavBuffer[19] = 0

        // AudioFormat = 1 (PCM)
        wavBuffer[20] = 1
        wavBuffer[21] = 0

        // NumChannels = 1 (Mono)
        wavBuffer[22] = 1
        wavBuffer[23] = 0

        // SampleRate = 22050
        wavBuffer[24] = (sampleRate and 0xFF).toByte()
        wavBuffer[25] = ((sampleRate ushr 8) and 0xFF).toByte()
        wavBuffer[26] = ((sampleRate ushr 16) and 0xFF).toByte()
        wavBuffer[27] = ((sampleRate ushr 24) and 0xFF).toByte()

        // ByteRate = sampleRate * 1 * 2 = 44100
        val byteRate = sampleRate * 2
        wavBuffer[28] = (byteRate and 0xFF).toByte()
        wavBuffer[29] = ((byteRate ushr 8) and 0xFF).toByte()
        wavBuffer[30] = ((byteRate ushr 16) and 0xFF).toByte()
        wavBuffer[31] = ((byteRate ushr 24) and 0xFF).toByte()

        // BlockAlign = 2
        wavBuffer[32] = 2
        wavBuffer[33] = 0

        // BitsPerSample = 16
        wavBuffer[34] = 16
        wavBuffer[35] = 0

        // "data"
        wavBuffer[36] = 'd'.code.toByte()
        wavBuffer[37] = 'a'.code.toByte()
        wavBuffer[38] = 't'.code.toByte()
        wavBuffer[39] = 'a'.code.toByte()

        // pcmByteLength
        wavBuffer[40] = (pcmByteLength and 0xFF).toByte()
        wavBuffer[41] = ((pcmByteLength ushr 8) and 0xFF).toByte()
        wavBuffer[42] = ((pcmByteLength ushr 16) and 0xFF).toByte()
        wavBuffer[43] = ((pcmByteLength ushr 24) and 0xFF).toByte()

        // Generate PCM Samples
        var phaseLead = 0.0
        var phaseBass = 0.0
        var byteIdx = 44

        for (sampleIndex in 0 until totalPcmSamples) {
            val currentBeatIndex = sampleIndex / samplesPerBeat
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
            val pcmShort = mixed.toInt().coerceIn(-32768, 32767).toShort()

            wavBuffer[byteIdx++] = (pcmShort.toInt() and 0xFF).toByte()
            wavBuffer[byteIdx++] = ((pcmShort.toInt() ushr 8) and 0xFF).toByte()
        }

        return wavBuffer
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData()
    return usePinned { pinned ->
        NSData.dataWithBytes(pinned.addressOf(0), length = size.toULong())
    }
}
