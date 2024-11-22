package com.github.lexanovichok.workingshift.schedule.address.core

import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshift.core.LiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.userData.Address

interface AddressesListLiveDataWrapper {

    interface Read {
        fun liveData() : LiveData<List<Address>>
    }
    interface Update {
        fun update(value : List<Address>)
    }
    interface Mutable : Read, Update
    interface Add {
        fun add(value : Address)
    }
    interface All : Mutable, Add

    class Base : LiveDataWrapper.Abstract<List<Address>>(), All {
        override fun add(value: Address) {
            val currentList = liveData.value?.toMutableList() ?: ArrayList()
            currentList.add(value)
            update(currentList)
        }

    }
}