package com.github.lexanovichok.workingshift.schedule.address.core

import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshift.core.LiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.userData.Address

interface AddressInfoLiveDataWrapper {

    interface Read {
        fun liveData() : LiveData<Address>
    }
    interface Update {
        fun update(value : Address)
    }
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<Address>(), Mutable
}