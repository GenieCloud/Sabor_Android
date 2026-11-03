package com.gio.sabor_android.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {
    // Lazily initialize FirebaseAuth so it doesn't crash on app startup before context is ready
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUser() = try {
        auth.currentUser
    } catch (e: Exception) {
        null
    }

    fun signInWithEmail(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage ?: "Sign in failed. Please try again.")
            }
    }

    fun signUpWithEmail(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage ?: "Sign up failed. Please try again.")
            }
    }

    fun signOut() {
        auth.signOut()
    }
}