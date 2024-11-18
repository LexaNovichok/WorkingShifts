package com.github.lexanovichok.workingshifts.schedule.tasks.core

import com.github.lexanovichok.workingshifts.core.LiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.userData.Task

interface TasksHistoryLiveDataWrapper {
    interface Read : LiveDataWrapper.Read<List<Task>>
    interface Update : LiveDataWrapper.Update<List<Task>>
    interface Mutable : Read, Update
    interface Add {
        fun add(value : Task)
    }
    interface All : Mutable, Add

    class Base : LiveDataWrapper.Abstract<List<Task>>(), All {
        override fun add(value: Task) {
            val currentList = liveData.value?.toMutableList() ?: ArrayList()
            currentList.add(value)
            update(currentList)
        }

    }
}