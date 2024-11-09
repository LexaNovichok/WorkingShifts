package com.github.lexanovichok.workingshifts.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        return firebaseAuth.currentUser != null
    }

    suspend fun register(email: String, password: String) {
        return suspendCoroutine<Unit> { continuation ->
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        Log.d(tag, "Register success")
                        authResult.user?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                Log.d(tag, "Email verification sent")
                                continuation.resume(Unit)
                            }
                            ?.addOnFailureListener { e ->
                                Log.d(tag, "Email verification failed: ${e.message}")
                                continuation.resumeWithException(e)
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.d(tag, "Register failed: ${e.message}")
                        continuation.resumeWithException(e)
                    }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                continuation.resumeWithException(e)
            }
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
                        //Log.d("AUTH", "$tag: login failure ${it.message}")
                        Log.d("AUTH", "Email or password is incorrect")
                        continuation.resumeWithException(Exception("Email or password is incorrect"))
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Log.d("AUTH", "$tag: login exception ${e.message}")
            throw e
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun isEmailVerified(): Boolean {
        return suspendCoroutine { continuation ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                currentUser.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(currentUser.isEmailVerified)
                    } else {
                        continuation.resumeWithException(task.exception ?: Exception("Failed to reload user"))
                    }
                }
            } else {
                continuation.resumeWithException(Exception("User is not logged in"))
            }
        }
    }

    suspend fun sendPasswordResetEmail(email: String) {
        return suspendCoroutine { continuation ->
            try {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Log.d(tag, "Password reset email sent successfully")
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { e ->
                        Log.d(tag, "Failed to send password reset email: ${e.message}")
                        continuation.resumeWithException(e)
                    }
            } catch (e: Exception) {
                Log.d(tag, "Error sending password reset email: ${e.message}")
                continuation.resumeWithException(e)
            }
        }
    }

}