package com.github.lexanovichok.workingshift.schedule.tasks.core

import androidx.lifecycle.LiveData
import com.github.lexanovichok.workingshift.core.LiveDataWrapper
import com.github.lexanovichok.workingshift.schedule.userData.Task

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