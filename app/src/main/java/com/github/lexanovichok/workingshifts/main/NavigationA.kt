package com.github.lexanovichok.workingshifts.main

import android.util.Log
import com.github.lexanovichok.workingshifts.core.LiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.main.ScreenF

interface NavigationA {

    interface Read : LiveDataWrapper.Read<ScreenA>
    interface Update : LiveDataWrapper.Update<ScreenA>
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<ScreenA>(), Mutable {
//        override fun update(value: ScreenA) {
//            super.update(value)
//            Log.d("LC", "NavigationA update: $value")
//        }
    }
}