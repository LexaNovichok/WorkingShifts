package com.github.lexanovichok.workingshifts.resetPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lexanovichok.workingshifts.auth.AuthRepository
import com.github.lexanovichok.workingshifts.auth.AuthViewModel
import com.github.lexanovichok.workingshifts.core.ClearViewModel
import com.github.lexanovichok.workingshifts.core.InputValidator
import com.github.lexanovichok.workingshifts.main.Navigation
import com.github.lexanovichok.workingshifts.main.Screen
import kotlinx.coroutines.launch

class PasswordResetViewModel(
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    repository : AuthRepository,
    inputValidator: InputValidator
) : AuthViewModel(repository, inputValidator) {

    private val _passwordResetSuccess = MutableLiveData<Boolean>()
    val passwordResetSuccess: LiveData<Boolean> get() = _passwordResetSuccess

     fun resetPassword(email : String) {
         if (!isEmailValid(email)) {
             _errorMessage.value = "Invalid email format"
             return
         } else if (email.isBlank()) {
             _errorMessage.value = "Email cannot be empty"
             return
         }

         viewModelScope.launch {
             try {
                 authRepository.sendPasswordResetEmail(email)
                 _passwordResetSuccess.value = true
                 comeback()
             } catch (e: Exception) {
                 _errorMessage.value = "Error sending password reset email: ${e.message}"
                 _passwordResetSuccess.value = false
             }
         }
     }

    private fun comeback() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(PasswordResetViewModel::class.java)
    }

}