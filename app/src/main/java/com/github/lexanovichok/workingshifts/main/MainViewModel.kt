package com.github.lexanovichok.workingshifts.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.login.LoginScreen

class MainViewModel(
    private val navigation: Navigation.Mutable
) : ViewModel(), Navigation.Read {
    override fun liveData(): LiveData<Screen> = navigation.liveData()

    fun init(isFirstRun : Boolean) {
        if (isFirstRun)
            navigation.update(LoginScreen)
    }
}