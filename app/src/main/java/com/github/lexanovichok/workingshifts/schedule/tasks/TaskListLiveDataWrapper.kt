package com.github.lexanovichok.workingshifts.schedule.tasks

import com.github.lexanovichok.workingshifts.core.LiveDataWrapper
import com.github.lexanovichok.workingshifts.schedule.userData.Task

interface TaskListLiveDataWrapper {

//    interface Read : LiveDataWrapper.Read<ArrayList<Task>>
//    interface Update : LiveDataWrapper.Update<ArrayList<Task>>
//    interface Mutable : Read, Update
    interface Add {
        fun add(value : Task)
    }
//    interface All : Mutable, Add

    class Base : LiveDataWrapper.Abstract<ArrayList<Task>>(), Add {
        override fun add(value: Task) {
            val currentList = liveData.value ?: ArrayList()
            currentList.add(value)
            update(currentList)
        }

    }
}