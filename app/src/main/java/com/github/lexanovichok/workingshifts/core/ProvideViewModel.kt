package com.github.lexanovichok.workingshifts.core

import com.github.lexanovichok.workingshifts.auth.AuthViewModel
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.auth.AuthRepository
import java.lang.IllegalStateException

interface ProvideViewModel {

    fun <T : ViewModel> viewModel(viewModelClass: Class<T>) : T

    class Base : ProvideViewModel {
        private val authRepository = AuthRepository()
        override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T {
            return when(viewModelClass) {
                AuthViewModel::class.java -> AuthViewModel(authRepository)

                else -> throw IllegalStateException("unknown viewModelClass $viewModelClass")
            } as T
        }

    }
}