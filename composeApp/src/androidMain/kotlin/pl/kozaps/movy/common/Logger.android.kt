package pl.kozaps.movy.common

import android.util.Log

actual fun debugLog(tag: String, message: String) {
    Log.d(tag, message)
}
