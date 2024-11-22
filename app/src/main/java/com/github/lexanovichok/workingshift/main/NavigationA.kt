package com.github.lexanovichok.workingshift.main

import com.github.lexanovichok.workingshift.core.LiveDataWrapper

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