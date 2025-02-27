package com.github.lexanovichok.workingshifts.schedule.worker.core

import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshifts.core.LiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.userData.Worker

interface WorkerInfoLiveDataWrapper {
    interface Read {
        fun liveData() : LiveData<Worker>
    }

    interface Update {
        fun update(value: Worker)
    }
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<Worker>(), Mutable
}