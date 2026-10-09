package com.driftgirls.i18n

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.driftgirls.getSystemLanguageCode
import com.driftgirls.model.*

enum class Language(val code: String, val displayName: String, val flag: String) {
    ES("es", "Español", "🇪🇸"),
    EN("en", "English", "🇬🇧")
}

enum class LanguageSetting(val code: String) {
    SYSTEM("system"),
    ES("es"),
    EN("en")
}

enum class ThemeSetting(val code: String) {
    SYSTEM("system"),
    DARK("dark"),
    LIGHT("light")
}

object AppSettings {
    var languageSetting by mutableStateOf(LanguageSetting.SYSTEM)
    var themeSetting by mutableStateOf(ThemeSetting.SYSTEM)
    var musicVolume by mutableStateOf(1.0f)
    var isMuted by mutableStateOf(false)

    val currentLanguage: Language
        get() = when (languageSetting) {
            LanguageSetting.SYSTEM -> if (getSystemLanguageCode().startsWith("es")) Language.ES else Language.EN
            LanguageSetting.ES -> Language.ES
            LanguageSetting.EN -> Language.EN
        }
}

object AppLanguage {
    val currentLanguage: Language
        get() = AppSettings.currentLanguage
}

// Model String Extensions based on selected language
fun Car.localizedSubtitle(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "trueno_ae86" -> if (lang == Language.ES) "El Legendario Hatchback de Drift" else "The Legendary Drift Hatch"
        "rx7_spirit" -> if (lang == Language.ES) "El Cohete de Motor Rotativo" else "Rotary Engine Rocket"
        "gtr_horizon" -> if (lang == Language.ES) "Godzilla Tracción Total" else "All-Wheel Godzilla"
        "hyper_phantom" -> if (lang == Language.ES) "El Hiperdeportivo Cíber Definitivo" else "Ultimate Cyber Hypercar"
        else -> subtitle
    }
}

fun GirlCompanion.localizedTitle(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "aoi" -> if (lang == Language.ES) "La Especialista en Turbo" else "The Turbo Specialist"
        "sora" -> if (lang == Language.ES) "La Reina del Drift" else "The Drift Queen"
        "elena" -> if (lang == Language.ES) "Alto Octanaje" else "High Octane"
        "mia" -> if (lang == Language.ES) "Piloto de Precisión" else "Precision Pilot"
        else -> title
    }
}

fun GirlCompanion.localizedBio(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "aoi" -> if (lang == Language.ES) "Mecánica alegre apasionada del tuning de altas revoluciones y las carreras nocturnas en la autopista." else "Spunky master mechanic who loves high-rev tuning and late-night highway sprints."
        "sora" -> if (lang == Language.ES) "Piloto de touge tranquila y serena que conoce la geometría de cada curva cerrada." else "Calm and composed touge racer who knows the geometry of every hairpin turn."
        "elena" -> if (lang == Language.ES) "Icono de las carreras callejeras que vive por la velocidad pura y la adrenalina." else "Sassy street racing icon who lives for raw speed and adrenaline."
        "mia" -> if (lang == Language.ES) "Prodigio analítica de las carreras que calcula las trayectorias al milímetro." else "Analytical racing prodigy who calculates apex trajectories down to the millimeter."
        else -> bio
    }
}

fun GirlCompanion.localizedDialogueLines(lang: Language = AppLanguage.currentLanguage): List<String> {
    return when (id) {
        "aoi" -> if (lang == Language.ES) listOf(
            "¡Escucha el rugido de ese motor!",
            "¡¡Activa el Nitro AHORA!!",
            "¡Kyaa~ ¡Qué curva tan suave!",
            "¡Estás conduciendo como una leyenda!",
            "¡Sujétate bien, el turbo está haciendo efecto!"
        ) else listOf(
            "Listen to that engine roar!",
            "Hit the Nitro NOW!!",
            "Kyaa~ What a smooth corner!",
            "You're driving like a legend!",
            "Hold tight, turbo's kicking in!"
        )
        "sora" -> if (lang == Language.ES) listOf(
            "Mantén tu trazada constante...",
            "¡Ángulo perfecto! ¡Brillante!",
            "¡Siente cómo se pierde el agarre!",
            "¡Combo imparable!",
            "Ese derrape fue arte puro."
        ) else listOf(
            "Keep your line steady...",
            "Perfect angle! Brilliant!",
            "Feel the tire grip breaking!",
            "Unstoppable combo!",
            "That drift was pure art."
        )
        "elena" -> if (lang == Language.ES) listOf(
            "¡Pisa el acelerador de una vez!",
            "¡¡Woohoo!! ¡De eso estoy hablando!",
            "¡Demuéstrales quién manda en estas calles!",
            "¡Más rápido! ¡MÁS RÁPIDO!",
            "¡De verdad me dejaste sin aliento!"
        ) else listOf(
            "Step on the gas already!",
            "Woohoo!! That's what I'm talking about!",
            "Show them who owns these streets!",
            "Faster! FASTER!",
            "You actually made me gasp!"
        )
        "mia" -> if (lang == Language.ES) listOf(
            "Trayectoria óptima.",
            "Sincronización de precisión ejecutada.",
            "Zona de derrape fijada.",
            "Ejecución sublime, piloto.",
            "¡Eficiencia del 100%!"
        ) else listOf(
            "Trajectory optimal.",
            "Precision timing executed.",
            "Drift zone locked in.",
            "Sublime execution driver.",
            "Efficiency rating 100%!"
        )
        else -> dialogueLines
    }
}

fun CompanionBonusType.localizedDescription(lang: Language = AppLanguage.currentLanguage): String {
    return when (this) {
        CompanionBonusType.NITRO_RECHARGE -> if (lang == Language.ES) "Recarga de Nitro más Rápida" else "Faster Nitro Recharge"
        CompanionBonusType.DRIFT_SCORE_MULTIPLIER -> if (lang == Language.ES) "Multiplicador de Puntuación de Derrape" else "Bonus Drift Score Multiplier"
        CompanionBonusType.TOP_SPEED_BOOST -> if (lang == Language.ES) "Aumento de Velocidad Máxima" else "Top Speed Boost"
        CompanionBonusType.DRIFT_TIMING_WINDOW -> if (lang == Language.ES) "Margen de Derrape Perfecto más Amplio" else "Wider Perfect Drift Window"
    }
}

fun DateActivity.localizedTitle(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "date_cafe" -> if (lang == Language.ES) "Cita en el Café de Gatos" else "Cat Cafe Date"
        "date_night_drive" -> if (lang == Language.ES) "Paseo Nocturno al Mirador" else "Midnight Lookout Drive"
        "date_amusement" -> if (lang == Language.ES) "Noche en la Noria" else "Ferris Wheel Night"
        else -> title
    }
}

fun DateActivity.localizedLocation(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "date_cafe" -> if (lang == Language.ES) "Calle Harajuku" else "Harajuku Street"
        "date_night_drive" -> if (lang == Language.ES) "Mirador de la Ciudad" else "Skyline Summit"
        "date_amusement" -> if (lang == Language.ES) "Parque Cíber" else "Cyber World Park"
        else -> location
    }
}

fun DateActivity.localizedDescription(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "date_cafe" -> if (lang == Language.ES) "Disfruta de un té boba dulce y adorables gatitos en un ambiente acogedor." else "Enjoy sweet boba tea and adorable kittens in a cozy atmosphere."
        "date_night_drive" -> if (lang == Language.ES) "Conduce hasta la cima de la montaña para contemplar las luces de la ciudad juntos." else "Drive up to the mountain peak to gaze at city lights together."
        "date_amusement" -> if (lang == Language.ES) "Súbanse a la romántica noria gigante iluminada bajo los fuegos artificiales." else "Ride the romantic giant glowing ferris wheel under fireworks."
        else -> description
    }
}

fun GiftItem.localizedName(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "gift_tea" -> if (lang == Language.ES) "Té de Burbujas" else "Boba Milk Tea"
        "gift_roses" -> if (lang == Language.ES) "Ramo de Rosas" else "Rose Bouquet"
        "gift_pendant" -> if (lang == Language.ES) "Colgante de Gemas" else "Gem Pendant"
        "gift_ring" -> if (lang == Language.ES) "Anillo de Diamante" else "Diamond Ring"
        else -> name
    }
}

fun RaceTrack.localizedLocation(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "track_tutorial" -> if (lang == Language.ES) "Paso de Montaña" else "Mountain Hill"
        "track_neon_city" -> if (lang == Language.ES) "Autopista Metropolitana" else "Metropolis Highway"
        "track_mountain" -> if (lang == Language.ES) "Touge del Monte Fuji" else "Mount Fuji Touge"
        "track_ocean" -> if (lang == Language.ES) "Autopista del Pacífico" else "Pacific Highway"
        "track_endless" -> if (lang == Language.ES) "Cumbre Celestial" else "Celestial Summit"
        else -> location
    }
}

fun RaceTrack.localizedDifficulty(lang: Language = AppLanguage.currentLanguage): String {
    return when (id) {
        "track_tutorial" -> if (lang == Language.ES) "Principiante" else "Beginner"
        "track_neon_city" -> if (lang == Language.ES) "Fácil" else "Easy"
        "track_mountain" -> if (lang == Language.ES) "Intermedio" else "Medium"
        "track_ocean" -> if (lang == Language.ES) "Difícil" else "Hard"
        "track_endless" -> if (lang == Language.ES) "Infinito (60s)" else "Endless (60s)"
        else -> difficulty
    }
}

fun DriftRating.localizedLabel(lang: Language = AppLanguage.currentLanguage): String {
    return when (this) {
        DriftRating.PERFECT -> if (lang == Language.ES) "¡¡PERFECTO!!" else "PERFECT!!"
        DriftRating.GREAT -> if (lang == Language.ES) "¡GENIAL!" else "GREAT!"
        DriftRating.GOOD -> if (lang == Language.ES) "BUENO" else "GOOD"
        DriftRating.LATE -> if (lang == Language.ES) "TARDE" else "LATE"
        DriftRating.MISS -> if (lang == Language.ES) "FALLO" else "MISS"
    }
}

fun CornerLength.localizedLabel(lang: Language = AppLanguage.currentLanguage): String {
    return when (this) {
        CornerLength.SHORT -> if (lang == Language.ES) "CHICANE CERRADA" else "SHARP CHICANE"
        CornerLength.MEDIUM -> if (lang == Language.ES) "HORQUILLA ESTÁNDAR" else "STANDARD HAIRPIN"
        CornerLength.LONG -> if (lang == Language.ES) "CURVA AMPLIA" else "SWEEPING ARC"
        CornerLength.ULTRA_LONG -> if (lang == Language.ES) "CURVA DE RESISTENCIA" else "ENDURANCE BEND"
    }
}
