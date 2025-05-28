package com.reena.aamp.handler

/*
 *  This file is part of AAMP © 2025.
 *
 *  AAMP is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AAMP is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AAMP. If not, see <https://www.gnu.org/licenses/>.
 */

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Looper
import android.util.Log
import com.reena.aamp.activity.CrashActivity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.Volatile
import kotlin.system.exitProcess
import androidx.core.content.edit

class Crash private constructor(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    companion object {
        @SuppressLint("StaticFieldLeak")
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
            val crashLog = generateCrashLog(e)
            val crashFile = saveCrashToFile(crashLog)

            context.getSharedPreferences("crash_prefs", Context.MODE_PRIVATE).edit {
                putString("last_crash_file", crashFile.absolutePath)
            }

            object : Thread() {
                override fun run() {
                    Looper.prepare()
                    val intent = Intent(context, CrashActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra(CrashActivity.EXTRA_CRASH_INFO, crashLog)
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Looper.loop()
                }
            }.start()
            Thread.sleep(1000)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        } catch (_: Exception) {
            defaultHandler?.uncaughtException(t, e)
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