package com.github.lexanovichok.workingshift.authentication.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshift.core.InputValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class AuthViewModel(protected val authRepository: AuthRepository, protected val inputValidator: InputValidator) : ViewModel(){
    // Переменная для хранения состояния аутентификации
    protected val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    protected val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    // Слежка за ошибками
    protected val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


    fun logout() {
        authRepository.logout()
        _isLoggedIn.value = false
    }

    protected fun isEmailValid(email: String): Boolean {
        return inputValidator.isEmailValid(email)
    }

    protected fun checkIsFieldsCorrect(email : String, password : String) : Boolean {
        if (!isEmailValid(email)) {
            _errorMessage.value = "Invalid email format"
            return false
        }

        if (password.trim() == "") {
            _errorMessage.value = "The password cannot be empty"
            return false
        }
        return true
    }

    fun resetError() {
        _errorMessage.value = null
    }
}