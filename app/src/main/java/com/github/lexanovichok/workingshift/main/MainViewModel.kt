package com.github.lexanovichok.workingshift.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshift.authentication.LoggedInLiveDataWrapper
import com.github.lexanovichok.workingshift.authentication.login.LoginScreenA

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
        if (isFirstRun) {
            navigationA.update(LoginScreenA)
            Log.d("LC", "MainViewModel update to: LoginScreen")
        }
    }

    fun isLoggedIn() : Boolean =
        loggedInLiveDataWrapper.liveData().value ?: false
}