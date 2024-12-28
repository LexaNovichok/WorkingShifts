package com.github.lexanovichok.workingshift.authentication.auth

import android.util.Log
import com.github.lexanovichok.workingshift.authentication.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
                        val user = authResult.user
                        user?.sendEmailVerification()?.addOnSuccessListener {
                            Log.d(tag, "Email verification sent")

                            // Сохранение пользователя в Firestore
                            val firestore = FirebaseFirestore.getInstance()
                            val newUser = User(
                                uid = user.uid,
                                email = user.email ?: "",
                                isAdmin = false, // По умолчанию false,
                                isViewer = false
                            )

                            firestore.collection("users").document(user.uid).set(newUser)
                                .addOnSuccessListener {
                                    Log.d(tag, "User saved in Firestore")
                                    continuation.resume(Unit)
                                }
                                .addOnFailureListener { e ->
                                    Log.e(tag, "Failed to save user: ${e.message}")
                                    continuation.resumeWithException(e)
                                }
                        }?.addOnFailureListener { e ->
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

    suspend fun isAdmin(): Boolean {
        return suspendCoroutine { continuation ->
            val userId = firebaseAuth.currentUser?.uid ?: return@suspendCoroutine continuation.resume(false)
            val firestore = FirebaseFirestore.getInstance()

            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val isAdmin = document.getBoolean("admin") ?: false
                    Log.d("ROLE", "repo isAdmin: $isAdmin")
                    continuation.resume(isAdmin)
                }
                .addOnFailureListener { e ->
                    Log.e(tag, "Failed to fetch admin status: ${e.message}")
                    continuation.resume(false)
                }
        }
    }

    suspend fun isViewer(): Boolean {
        return suspendCoroutine { continuation ->
            val userId = firebaseAuth.currentUser?.uid ?: return@suspendCoroutine continuation.resume(false)
            val firestore = FirebaseFirestore.getInstance()

            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val isViewer = document.getBoolean("viewer") ?: false
                    Log.d("ROLE", "repo isViewer: $isViewer")
                    continuation.resume(isViewer)
                }
                .addOnFailureListener { e ->
                    Log.e(tag, "Failed to fetch viewer status: ${e.message}")
                    continuation.resume(false)
                }
        }
    }

}