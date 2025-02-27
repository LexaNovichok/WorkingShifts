package com.github.lexanovichok.workingshifts.authentication.login

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.lexanovichok.workingshifts.authentication.auth.AuthRepository
import com.github.lexanovichok.workingshifts.authentication.auth.AuthViewModel
import com.github.lexanovichok.workingshifts.core.InputValidator
import com.github.lexanovichok.workingshifts.main.NavigationA
import com.github.lexanovichok.workingshifts.authentication.register.RegisterScreenA
import com.github.lexanovichok.workingshifts.authentication.resetPassword.PasswordResetScreenA
import com.github.lexanovichok.workingshifts.schedule.main.MainFragmentScreenA
import kotlinx.coroutines.launch

class LoginViewModel(
    private val navigationA: NavigationA.Mutable,
    repository : AuthRepository,
    inputValidator: InputValidator
) : AuthViewModel(repository, inputValidator) {

    protected val _isEmailVerified = MutableLiveData<Boolean>()
    val isEmailVerified: LiveData<Boolean> get() = _isEmailVerified

    fun init() {
        viewModelScope.launch {
            if (authRepository.isLoggedIn() && authRepository.isEmailVerified()) {
                navigationA.update(MainFragmentScreenA)
                Log.d("AUTH", "isLoggedIn: $isLoggedIn, isEmailVerified: ${authRepository.isEmailVerified()}")
            }
        }
    }

    // Про аутентификацию
    fun login(email: String, password: String) {

        if (!checkIsFieldsCorrect(email, password)) return

        viewModelScope.launch {
            try {
                authRepository.login(email, password)
                _isLoggedIn.value = authRepository.isLoggedIn()
                checkEmailVerification()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun isLoggedIn() : Boolean = authRepository.isLoggedIn()

    fun checkEmailVerification() {
        viewModelScope.launch {
            try {
                _isEmailVerified.value = authRepository.isEmailVerified()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    // Про bundle

    fun save(bundle : Bundle, list : ArrayList<String>) {
        bundle.putStringArrayList(KEY, list)
    }

    fun restore(bundle : Bundle) =
        bundle.getStringArrayList(KEY) ?: ArrayList()

    // Про навигацию
    fun mainFragment() {
        navigationA.update(MainFragmentScreenA)
    }

    fun registerFragment() {
        navigationA.update(RegisterScreenA)
    }

    fun passwordResetScreen() {
        navigationA.update(PasswordResetScreenA)
    }

    companion object {
        private const val KEY = "DATA_USER_BUNDLE_KEY"
    }
}