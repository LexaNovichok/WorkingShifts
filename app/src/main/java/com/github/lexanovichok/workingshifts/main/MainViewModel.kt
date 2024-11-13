package com.github.lexanovichok.workingshifts.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.authentication.login.LoginScreenA

class MainViewModel(
    private val navigationA: NavigationA.Mutable
) : ViewModel(), NavigationA.Read {
    override fun liveData(): LiveData<ScreenA> = navigationA.liveData()

    fun init(isFirstRun : Boolean) {
        if (isFirstRun)
            navigationA.update(LoginScreenA)
    }
}