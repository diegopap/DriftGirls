package com.driftgirls

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlatformIosTest {
    @Test
    fun `platform name is reported`() {
        assertTrue(getPlatform().name.isNotBlank())
    }

    @Test
    fun `save and load game data works on ios`() {
        val testPayload = "cash:50000|gems:100|cars:trueno_ae86"
        saveGameData(testPayload)
        val loaded = loadGameData()
        assertEquals(testPayload, loaded)
    }

    @Test
    fun `date format matches pattern`() {
        val date = getFormattedCurrentDate()
        assertTrue(date.matches(Regex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")))
    }

    @Test
    fun `system language code is non empty`() {
        val lang = getSystemLanguageCode()
        assertTrue(lang.isNotEmpty())
    }

    @Test
    fun `audio volume and mute controls work on ios`() {
        setMusicVolume(0.75f)
        assertEquals(0.75f, getMusicVolume(), 0.01f)

        setMusicMuted(true)
        assertTrue(isMusicMuted())

        setMusicMuted(false)
        assertTrue(!isMusicMuted())
    }
}
