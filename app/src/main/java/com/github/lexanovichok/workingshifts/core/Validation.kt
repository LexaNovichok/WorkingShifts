package com.github.lexanovichok.workingshifts.core

class InputValidator {
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun arePasswordsMatching(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}
