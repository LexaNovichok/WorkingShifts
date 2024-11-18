package com.github.lexanovichok.workingshifts.schedule.main

import android.util.Log
import com.github.lexanovichok.workingshifts.core.LiveDataWrapper
import com.github.lexanovichok.workingshifts.main.ScreenA

interface NavigationF {
    interface Read : LiveDataWrapper.Read<ScreenF >
    interface Update : LiveDataWrapper.Update<ScreenF>
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<ScreenF>(), Mutable {
        override fun update(value: ScreenF) {
            super.update(value)
            Log.d("LC", "NavigationF update: $value")
        }
    }
}