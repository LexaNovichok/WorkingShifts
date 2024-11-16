package com.github.lexanovichok.workingshifts.schedule.tasks.core

import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshifts.core.LiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.userData.Task
import com.github.lexanovichok.workingshifts.schedule.userData.Worker

interface TaskInfoLiveDataWrapper {
    interface Read {
        fun liveData() : LiveData<Task>
    }

    interface Update {
        fun update(value: Task)
    }
    interface Mutable : Read, Update

    class Base : LiveDataWrapper.Abstract<Task>(), Mutable
}