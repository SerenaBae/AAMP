package com.reena.aamp.app

import android.app.Application
import com.reena.aamp.handler.Crash

class AAMP : Application() {
    override fun onCreate() {
        super.onCreate()
        Crash.init(this)
    }
}