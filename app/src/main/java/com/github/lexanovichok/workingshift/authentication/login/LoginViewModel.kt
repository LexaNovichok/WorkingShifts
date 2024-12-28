package com.github.lexanovichok.workingshift.authentication.login

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.lexanovichok.workingshift.authentication.AdminLiveDataWrapper
import com.github.lexanovichok.workingshift.authentication.LoggedInLiveDataWrapper
import com.github.lexanovichok.workingshift.authentication.UserState
import com.github.lexanovichok.workingshift.authentication.ViewerLiveDataWrapper
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
    private val viewerLiveDataWrapper: ViewerLiveDataWrapper.Mutable,
    private val adminLiveDataWrapper: AdminLiveDataWrapper.Mutable,
    repository : AuthRepository,
    inputValidator: InputValidator
) : AuthViewModel(repository, inputValidator) {

    private val _isEmailVerified = MutableLiveData<Boolean>()
    val isEmailVerified: LiveData<Boolean> get() = _isEmailVerified


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



    suspend fun checkUserStatus() : UserState {
        val loggedIn = authRepository.isLoggedIn()
        val verified = if (loggedIn) authRepository.isEmailVerified() else false
        val admin = if (verified) authRepository.isAdmin() else false
        val viewer = if (verified) authRepository.isViewer() else false

        // Обновляем combinedState напрямую
        val userState = UserState(loggedIn, verified, admin, viewer)

        adminLiveDataWrapper.update(userState.isAdmin)
        viewerLiveDataWrapper.update(userState.isViewer)

        Log.d("ROLE", "LoginViewModel isAdmin: ${userState.isAdmin}, isViewer: ${userState.isViewer}")
        return userState
    }

//    fun checkAdminStatus() {
//        viewModelScope.launch {
//            try {
//                val adminStatus = authRepository.isAdmin() // Метод из AuthRepository
//                _isAdmin.postValue(adminStatus)
//            } catch (e: Exception) {
//                Log.e("AUTH", "Error checking admin status: ${e.message}")
//                _isAdmin.postValue(false)
//            }
//        }
//    }

//    fun checkIsLoggedIn() : Boolean {
//        val isLoggedIn
//        viewModelScope.launch {
//            if (authRepository.isLoggedIn() && authRepository.isEmailVerified()) {
//                return true
//            }
//        }
//        return false
//    }

    // Про аутентификацию
    suspend fun login(email: String, password: String) {

        if (!checkIsFieldsCorrect(email, password)) return
        try {
            authRepository.login(email, password)

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

    fun viewerLiveData() = viewerLiveDataWrapper.liveData()
    fun adminLiveData() = adminLiveDataWrapper.liveData()

    companion object {
        private const val KEY = "DATA_USER_BUNDLE_KEY"
    }
}