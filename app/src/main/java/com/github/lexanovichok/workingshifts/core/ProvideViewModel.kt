package com.github.lexanovichok.workingshifts.core

import com.github.lexanovichok.workingshifts.auth.AuthViewModel
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.auth.AuthRepository
import com.github.lexanovichok.workingshifts.login.LoginViewModel
import com.github.lexanovichok.workingshifts.main.MainViewModel
import com.github.lexanovichok.workingshifts.main.Navigation
import com.github.lexanovichok.workingshifts.register.RegisterViewModel
import com.github.lexanovichok.workingshifts.resetPassword.PasswordResetViewModel
import java.lang.IllegalStateException

interface ProvideViewModel {

    fun <T : ViewModel> viewModel(viewModelClass: Class<T>) : T

    class Base(
        private val clearViewModel : ClearViewModel
    ) : ProvideViewModel {
        private val authRepository = AuthRepository()
        private val inputValidator  = InputValidator()
        private val navigation = Navigation.Base()
        override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T {
            return when(viewModelClass) {
                MainViewModel::class.java -> MainViewModel(navigation)
                AuthViewModel::class.java -> AuthViewModel(authRepository, inputValidator)
                LoginViewModel::class.java -> LoginViewModel(navigation, authRepository, inputValidator)
                RegisterViewModel::class.java -> RegisterViewModel(navigation, authRepository, inputValidator)
                PasswordResetViewModel::class.java -> PasswordResetViewModel(navigation, clearViewModel, authRepository, inputValidator)

                else -> throw IllegalStateException("unknown viewModelClass $viewModelClass")
            } as T
        }

    }
}