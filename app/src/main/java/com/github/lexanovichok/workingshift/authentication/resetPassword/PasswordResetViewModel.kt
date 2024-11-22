package com.github.lexanovichok.workingshift.authentication.resetPassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.lexanovichok.workingshift.authentication.auth.AuthRepository
import com.github.lexanovichok.workingshift.authentication.auth.AuthViewModel
import com.github.lexanovichok.workingshift.core.ClearViewModel
import com.github.lexanovichok.workingshift.core.InputValidator
import com.github.lexanovichok.workingshift.main.NavigationA
import com.github.lexanovichok.workingshift.main.ScreenA
import kotlinx.coroutines.launch

class PasswordResetViewModel(
    private val navigationA: NavigationA.Update,
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
        navigationA.update(ScreenA.Pop)
        Log.d("NAVIGATION", "PasswordResetViewModel update to: ScreenA.Pop")
        clear.clearViewModel(PasswordResetViewModel::class.java)
    }

}