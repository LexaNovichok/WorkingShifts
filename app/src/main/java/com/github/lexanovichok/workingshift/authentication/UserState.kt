package com.github.lexanovichok.workingshift.authentication

data class UserState(
    val isLoggedIn : Boolean,
    val isEmailVerified : Boolean,
    val isAdmin : Boolean,
    val isViewer: Boolean
)