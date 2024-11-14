package com.github.lexanovichok.workingshifts.schedule.address.core

import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshifts.core.LiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.userData.Address

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