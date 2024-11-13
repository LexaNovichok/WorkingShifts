package com.github.lexanovichok.workingshifts.main

import com.github.lexanovichok.workingshifts.core.LiveDataWrapper

interface NavigationA {

    interface Read : LiveDataWrapper.Read<ScreenA>
    interface Update : LiveDataWrapper.Update<ScreenA>
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<ScreenA>(), Mutable
}