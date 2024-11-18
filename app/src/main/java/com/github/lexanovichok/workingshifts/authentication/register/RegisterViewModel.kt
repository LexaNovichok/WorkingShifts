package com.github.lexanovichok.workingshifts.authentication.register

import android.util.Log
import com.github.lexanovichok.workingshifts.authentication.auth.AuthRepository
import com.github.lexanovichok.workingshifts.authentication.auth.AuthViewModel
import com.github.lexanovichok.workingshifts.core.InputValidator
import com.github.lexanovichok.workingshifts.authentication.login.LoginScreenA
import com.github.lexanovichok.workingshifts.main.NavigationA
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val navigationA : NavigationA.Mutable,
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
        navigationA.update(LoginScreenA)
        Log.d("NAVIGATION", "RegisterViewModel update to: LoginScreenA")
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (!checkIsFieldsCorrect(email, password)) return

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
                _errorMessage.value = "Please check email to verify your account."
            } catch (e: Exception) {
                _errorMessage.value = "Registration failed: ${e.localizedMessage}"
            }
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            try {
                val user = authRepository.getCurrentUser()
                if (user != null && !user.isEmailVerified) {
                    user.sendEmailVerification()
                    _errorMessage.value = "Verification email has been sent again. Please check your inbox."
                } else {
                    _errorMessage.value = "User is either not logged in or already verified."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to resend verification email: ${e.localizedMessage}"
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
