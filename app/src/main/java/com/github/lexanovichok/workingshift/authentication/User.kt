package com.github.lexanovichok.workingshift.authentication

data class User(
    val uid: String = "",
    val email: String = "",
    val isAdmin: Boolean = false
)