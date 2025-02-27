package com.github.lexanovichok.workingshifts.schedule.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressesScreenF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TasksScreenF
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkersScreenF

class MainFragmentViewModel(
    private val navigationF: NavigationF.Mutable,
) : ViewModel(), NavigationF.Read {

    override fun liveData(): LiveData<ScreenF> = navigationF.liveData()

    fun init() {
        navigationF.update(TasksScreenF)
    }
    fun tasksFragment() {
        navigationF.update(TasksScreenF)
    }

    fun workersFragment() {
        navigationF.update(WorkersScreenF)
    }

    fun addressesFragment() {
        navigationF.update(AddressesScreenF)
    }

}