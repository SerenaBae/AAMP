package com.reena.aamp.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.reena.aamp.R

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
    }
}