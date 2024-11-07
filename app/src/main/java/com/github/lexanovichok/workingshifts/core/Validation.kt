package com.github.lexanovichok.workingshifts.core

class InputValidator {
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}"
        return password.matches(passwordPattern.toRegex())
    }

    fun arePasswordsMatching(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}
