package com.github.lexanovichok.workingshifts.schedule.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressesScreenF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskHistoryScreenF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TasksScreenF
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkersScreenF

class MainFragmentViewModel(
    private val navigationF: NavigationF.Mutable,
) : ViewModel(), NavigationF.Read {

    private var isInitialized = false
    private var currentScreen: ScreenF? = null

    override fun liveData(): LiveData<ScreenF> = navigationF.liveData()

    fun init(firstRun: Boolean) {
        if (!isInitialized && firstRun) { // Проверяем, инициализирована ли ViewModel
            navigationF.update(TasksScreenF)
            isInitialized = true
        }
    }

    private fun updateScreen(screen: ScreenF) {
        if (currentScreen != screen) {
            currentScreen = screen
            navigationF.update(screen)
        }
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

    fun tasksHistoryFragment() {
        navigationF.update(TaskHistoryScreenF)
    }
}