package com.reena.aamp.app

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

import android.app.Application
import com.reena.aamp.handler.Crash

class AAMP : Application() {
    override fun onCreate() {
        super.onCreate()
        Crash.init(this)
    }
}