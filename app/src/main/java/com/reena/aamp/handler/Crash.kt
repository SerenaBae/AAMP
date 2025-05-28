package com.reena.aamp.handler

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.Volatile

class Crash private constructor(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    companion object {
        @Volatile
        private var instance: Crash? = null

        /**
         * Initialize Crash handler
         *
         * @param app
         */
        fun init(app: Application) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Crash(app)
                        Thread.setDefaultUncaughtExceptionHandler(instance)
                    }
                }
            }
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {

        } catch (_: Exception) {

        }
    }

    private fun generateCrashLog(throwable: Throwable): String {
        return buildString {
            append("Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\n\n")
            append("========== DEVICE INFORMATION =========\n")
            append("Brand: ${Build.BRAND}\n")
            append("Device: ${Build.DEVICE}\n")
            append("Model: ${Build.MODEL}\n")
            append("Android Version: ${Build.VERSION.RELEASE}\n")
            append("SDK: ${Build.VERSION.SDK_INT}\n")
            append("========== END OF DEVICE INFORMATION =========\n\n")
            append("========== START STACK TRACE =========\n\n")
            append(Log.getStackTraceString(throwable))
            append("========== END OF STACK TRACE =========\n\n")
        }
    }

    private fun saveCrashToFile(crashLog: String): File {
        val fileName = "crash_${System.currentTimeMillis()}.txt"
        val file = File(context.getExternalFilesDir(null), fileName)
        FileOutputStream(file).use {
            it.write(crashLog.toByteArray())
        }
        return file
    }
}