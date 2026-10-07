package com.driftgirls

import android.content.Context
import android.os.Build

object AndroidContextHolder {
    var context: Context? = null
}

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun saveGameData(data: String) {
    val ctx = AndroidContextHolder.context ?: return
    val prefs = ctx.getSharedPreferences("drift_girls_save_v1", Context.MODE_PRIVATE)
    prefs.edit().putString("save_payload", data).apply()
}

actual fun loadGameData(): String? {
    val ctx = AndroidContextHolder.context ?: return null
    val prefs = ctx.getSharedPreferences("drift_girls_save_v1", Context.MODE_PRIVATE)
    return prefs.getString("save_payload", null)
}
