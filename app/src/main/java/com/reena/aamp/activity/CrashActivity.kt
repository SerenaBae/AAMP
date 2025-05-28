package com.reena.aamp.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.reena.aamp.R
import java.io.File

class CrashActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CRASH_INFO = "extra_crash_info"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crash)
        val crashInfo: String = intent.getStringExtra(EXTRA_CRASH_INFO) ?: "Unknown error occurred"
        val tvMessage = findViewById<TextView>(R.id.tvErrorMessage)
        tvMessage.text = crashInfo

        val btnCopy = findViewById<Button>(R.id.btnCopy)
        btnCopy.setOnClickListener {
            copyToClipboard(crashInfo)
        }

        val btnShare = findViewById<Button>(R.id.btnShare)
        btnShare.setOnClickListener {
            shareCrashLog()
        }

        val btnRestart = findViewById<Button>(R.id.btnRestart)
        btnRestart.setOnClickListener {
            restartApp()
        }
    }

    private fun restartApp() {
        packageManager.getLaunchIntentForPackage(packageName)?.let { intent ->
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        finish()
    }

    private fun shareCrashLog() {
        try {
            val lastCrashFile = getSharedPreferences("crash_prefs", MODE_PRIVATE)
                .getString("last_crash_file", null)

            lastCrashFile?.let { filePath ->
                val crashFile = File(filePath)
                if (crashFile.exists()) {
                    val fileUri = FileProvider.getUriForFile(
                        this,
                        "$packageName.fileprovider",
                        crashFile
                    )

                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_STREAM, fileUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(Intent.createChooser(this, "Share Crash Log"))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ServiceCast")
    fun copyToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Text copied to clipboard", Snackbar.LENGTH_SHORT).show()
        }
    }
}