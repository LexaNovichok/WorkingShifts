package com.github.lexanovichok.workingshifts.login

import com.github.lexanovichok.workingshifts.auth.AuthRepository
import com.github.lexanovichok.workingshifts.auth.AuthViewModel
import com.github.lexanovichok.workingshifts.core.InputValidator
import com.github.lexanovichok.workingshifts.main.Navigation
import com.github.lexanovichok.workingshifts.register.RegisterFragment
import com.github.lexanovichok.workingshifts.register.RegisterScreen
import kotlinx.coroutines.launch

class LoginViewModel(
    private val navigation: Navigation.Mutable,
    repository : AuthRepository, inputValidator:
    InputValidator
) : AuthViewModel(repository, inputValidator) {

    fun init() {
        viewModelScope.launch {

        }
    }

    fun registerFragment() {
        navigation.update(RegisterScreen)
    }

    fun login(email: String, password: String) {
        if (!isEmailValid(email)) {
            _errorMessage.value = "Invalid email format"
            return
        }

        viewModelScope.launch {
            try {
                authRepository.login(email, password)
                _isLoggedIn.value = authRepository.isLoggedIn()
            } catch (e: Exception) {
                _errorMessage.value = "Login failed: ${e.localizedMessage}"
            }
        }

    }
}