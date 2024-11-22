package com.github.lexanovichok.workingshifts.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.authentication.LoggedInLiveDataWrapper
import com.github.lexanovichok.workingshifts.authentication.login.LoginScreenA
import com.github.lexanovichok.workingshifts.schedule.main.MainFragment
import com.github.lexanovichok.workingshifts.schedule.main.MainFragmentScreenA

class MainViewModel(
    private val navigationA: NavigationA.Mutable,
    private val loggedInLiveDataWrapper : LoggedInLiveDataWrapper.Mutable
) : ViewModel(), NavigationA.Read {

    private val _navigationState = MutableLiveData<ScreenA>()

    init {
        Log.d("LC", "MainViewModel init")
    }

    override fun liveData(): LiveData<ScreenA> = navigationA.liveData()


    fun init(isFirstRun : Boolean) {
//        if (isFirstRun) {
//            navigationA.update(LoginScreenA)
//            Log.d("NAVIGATION", "MainViewModel update to: LoginScreen")
//        }
        navigationA.update(MainFragmentScreenA)
    }

    fun isLoggedIn() : Boolean =
        loggedInLiveDataWrapper.liveData().value ?: false
}