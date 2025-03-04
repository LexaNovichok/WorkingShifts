package com.github.lexanovichok.workingshift.schedule.worker.core

import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshift.core.LiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.userData.Worker

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