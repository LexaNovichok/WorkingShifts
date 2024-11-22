package com.github.lexanovichok.workingshift.schedule.main

import com.github.lexanovichok.workingshift.core.LiveDataWrapper

interface NavigationF {
    interface Read : LiveDataWrapper.Read<ScreenF >
    interface Update : LiveDataWrapper.Update<ScreenF>
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<ScreenF>(), Mutable
}