package com.github.lexanovichok.workingshifts.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepository {

    private val tag = "Working Shifts"
    private val firebaseAuth = Firebase.auth

    // Check if a user is already logged in
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun isLoggedIn(): Boolean {
        if (firebaseAuth.currentUser != null) {
            println(tag + "Already logged in")
            return true
        }

        return false
    }

    fun register(email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    Log.d("AUTH", "$tag: register success")
                    // Отправка письма с подтверждением
                    authResult.user?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Log.d("AUTH", "$tag: email verification sent")
                        }
                        ?.addOnFailureListener {
                            Log.d("AUTH", "$tag: email verification failed ${it.message}")
                        }
                }
                .addOnFailureListener {
                    Log.d("AUTH", "$tag: register failure ${it.message}")
                }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Log.d("AUTH", "$tag: register exception ${e.message}")
        }
    }

    suspend fun login(email: String, password: String) {
        try {
            suspendCoroutine<Unit> { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        if (user != null && user.isEmailVerified) {
                            Log.d("AUTH", "$tag: login success")
                            continuation.resume(Unit)
                        } else {
                            Log.d("AUTH", "$tag: login failure - email not verified")
                            continuation.resumeWithException(Exception("Email not verified"))
                        }
                    }
                    .addOnFailureListener {
                        Log.d("AUTH", "$tag: login failure ${it.message}")
                        continuation.resumeWithException(it)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Log.d("AUTH", "$tag: login exception ${e.message}")
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}