package com.github.lexanovichok.workingshifts.authentication

import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshifts.core.LiveDataWrapper

interface LoggedInLiveDataWrapper {

    interface Read : LiveDataWrapper.Read<Boolean>
    interface Update {
        fun update(value : Boolean)
    }
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<Boolean>(), Mutable
}