package com.github.lexanovichok.workingshifts.register

import com.github.lexanovichok.workingshifts.auth.AuthRepository
import com.github.lexanovichok.workingshifts.auth.AuthViewModel
import com.github.lexanovichok.workingshifts.core.InputValidator
import com.github.lexanovichok.workingshifts.login.LoginScreen
import com.github.lexanovichok.workingshifts.main.Navigation
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val navigation : Navigation.Mutable,
    authRepository: AuthRepository,
    inputValidator: InputValidator
) : AuthViewModel(authRepository, inputValidator) {

    private fun isPasswordValid(password: String): Boolean {
        return inputValidator.isPasswordValid(password)
    }

    private fun arePasswordsMatching(password: String, confirmPassword: String): Boolean {
        return inputValidator.arePasswordsMatching(password, confirmPassword)
    }

    fun loginFragment() {
        navigation.update(LoginScreen)
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (!isEmailValid(email)) {
            return
        }

        if (!inputValidator.isPasswordValid(password)) {
            if (password.length < 6) {
                _errorMessage.value =
                    "Password must be at least 6 characters long, contain a number and a special character"
            } else {
                _errorMessage.value = "Password does not meet requirements"
            }
            return
        }

        if (!inputValidator.arePasswordsMatching(password, confirmPassword)) {
            _errorMessage.value = "Passwords do not match"
            return
        }

        viewModelScope.launch {
            try {
                authRepository.register(email, password)
                _errorMessage.value = "Please check your email to verify your account."
            } catch (e: Exception) {
                _errorMessage.value = "Registration failed: ${e.localizedMessage}"
            }
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch {
            if (authRepository.isEmailVerified()) {
                _isLoggedIn.value = true // Now allow login
            } else {
                _errorMessage.value = "Please verify your email first."
            }
        }
    }

}
