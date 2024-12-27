package com.github.lexanovichok.workingshift.authentication.login

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.lexanovichok.workingshift.authentication.LoggedInLiveDataWrapper
import com.github.lexanovichok.workingshift.authentication.UserState
import com.github.lexanovichok.workingshift.authentication.auth.AuthRepository
import com.github.lexanovichok.workingshift.authentication.auth.AuthViewModel
import com.github.lexanovichok.workingshift.core.InputValidator
import com.github.lexanovichok.workingshift.main.NavigationA
import com.github.lexanovichok.workingshift.authentication.register.RegisterScreenA
import com.github.lexanovichok.workingshift.authentication.resetPassword.PasswordResetScreenA
import com.github.lexanovichok.workingshift.schedule.main.MainFragmentScreenA
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel(
    private val navigationA: NavigationA.Mutable,
    private val loggedInLiveDataWrapper : LoggedInLiveDataWrapper.Mutable,
    repository : AuthRepository,
    inputValidator: InputValidator
) : AuthViewModel(repository, inputValidator) {

    private val _isEmailVerified = MutableLiveData<Boolean>()
    val isEmailVerified: LiveData<Boolean> get() = _isEmailVerified


    private val _isAdmin = MutableLiveData<Boolean>(false)
    val isAdmin : LiveData<Boolean> get() = _isAdmin

    private val _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> = _userState


    init {
        Log.d("LC", "LoginViewModel init")

        viewModelScope.launch {
            try {
                if (authRepository.isLoggedIn() && authRepository.isEmailVerified()) {
                    val isAdmin = authRepository.isAdmin()
                    val isViewer = authRepository.isViewer()
                    Log.d(
                        "LC",
                        "LoginViewModel isLoggedIn: ${authRepository.isLoggedIn()} isEmailVerified: ${authRepository.isEmailVerified()} isAdmin: $isAdmin"
                    )
                    if (isAdmin || isViewer) {
                        Log.d("LC", "LoginViewModel navigation update to MainFragmentScreen")
                        navigationA.update(MainFragmentScreenA)
                        loggedInLiveDataWrapper.update(true)
                    }
                } else {
                    Log.d("AUTH", "User is not logged in or email not verified")
                }
            } catch (e: FirebaseNetworkException) {
                Log.e("NETWORK", "Network error: ${e.message}")
                _errorMessage.postValue("Network error. Please check your connection.")
            } catch (e: Exception) {
                Log.e("ERROR", "Unexpected error: ${e.message}")
                _errorMessage.postValue("An unexpected error occurred. Please try again.")
            }
        }
    }


    suspend fun checkUserStatus(): UserState {
        val loggedIn = authRepository.isLoggedIn()
        val verified = if (loggedIn) authRepository.isEmailVerified() else false
        val admin = if (verified) authRepository.isAdmin() else false
        val viewer = if (verified) authRepository.isViewer() else false

        val userState = UserState(loggedIn, verified, admin, viewer)
        _userState.postValue(userState)
        return userState
    }


    // Про аутентификацию
    suspend fun login(email: String, password: String) {

        if (!checkIsFieldsCorrect(email, password)) return
        try {
            authRepository.login(email, password)
            Log.d(
                "AUTH",
                "LoginViewModel isLoggedIn: ${_isLoggedIn.value} isVerified: ${_isEmailVerified.value} isAdmin: ${_isAdmin.value}"
            )
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }


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
        Log.d("NAVIGATION", "LoginViewModel update to: MainFragmentScreenA")
    }

    fun registerFragment() {
        navigationA.update(RegisterScreenA)
        Log.d("NAVIGATION", "LoginViewModel update to: RegisterScreenA")
    }

    fun passwordResetScreen() {
        navigationA.update(PasswordResetScreenA)
        Log.d("NAVIGATION", "LoginViewModel update to: PasswordResetScreenA")
    }

    companion object {
        private const val KEY = "DATA_USER_BUNDLE_KEY"
    }
}