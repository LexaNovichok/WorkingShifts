package com.github.lexanovichok.workingshifts.schedule.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.authentication.auth.AuthRepository
import com.github.lexanovichok.workingshifts.authentication.login.LoginScreenA
import com.github.lexanovichok.workingshifts.main.NavigationA
import com.github.lexanovichok.workingshifts.schedule.address.core.AddressesScreenF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TaskHistoryScreenF
import com.github.lexanovichok.workingshifts.schedule.tasks.core.TasksScreenF
import com.github.lexanovichok.workingshifts.schedule.worker.core.WorkersScreenF

class MainFragmentViewModel(
    private val navigationF: NavigationF.Mutable,
    private val navigationA : NavigationA.Mutable,
    private val authRepository: AuthRepository
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

    fun tasksFragment() {
        navigationF.update(TasksScreenF)
        Log.d("NAVIGATION", "MainFragmentViewModel update to: TasksScreenF")
    }

    fun workersFragment() {
        navigationF.update(WorkersScreenF)
        Log.d("NAVIGATION", "MainFragmentViewModel update to: WorkersScreenF")
    }

    fun addressesFragment() {
        navigationF.update(AddressesScreenF)
        Log.d("NAVIGATION", "MainFragmentViewModel update to: AddressesScreenF")
    }

    fun tasksHistoryFragment() {
        navigationF.update(TaskHistoryScreenF)
        Log.d("NAVIGATION", "MainFragmentViewModel update to: TaskHistoryScreenF")
    }

    fun logout() {
        authRepository.logout()

        //navigationA.update(LoginScreenA)
    }
}