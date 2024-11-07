package com.github.lexanovichok.workingshifts.main

import com.github.lexanovichok.workingshifts.core.LiveDataWrapper

interface Navigation {

    interface Read : LiveDataWrapper.Read<Screen>
    interface Update : LiveDataWrapper.Update<Screen>
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<Screen>(), Mutable
}