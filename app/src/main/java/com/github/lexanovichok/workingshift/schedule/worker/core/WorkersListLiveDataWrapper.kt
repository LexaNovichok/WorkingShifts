package com.github.lexanovichok.workingshift.schedule.worker.core

import android.util.Log
import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshift.core.LiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.userData.Worker

interface WorkersListLiveDataWrapper {

    interface Read {
        fun liveData() : LiveData<List<Worker>>
    }

    interface Update {
        fun update(value: List<Worker>)
    }
    interface Mutable : Read, Update

    interface Add {
        fun add(value : Worker)
    }

    interface All : Mutable, Add
    class Base : LiveDataWrapper.Abstract<List<Worker>>(), All {

        override fun add(value : Worker) {
            val currentList = liveData.value?.toMutableList() ?: ArrayList()
            currentList.add(value)
            update(currentList)
        }

        override fun update(value: List<Worker>) {
            super.update(value)
            Log.d("SHEDULE", "LIVEDATA UPDATED WITH $value")
        }
    }
}