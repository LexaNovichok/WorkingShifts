package com.github.lexanovichok.workingshift.authentication

import com.github.lexanovichok.workingshift.core.LiveDataWrapper

class ViewerLiveDataWrapper {
    interface Read : LiveDataWrapper.Read<Boolean>
    interface Update {
        fun update(value : Boolean)
    }
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<Boolean>(), Mutable
}