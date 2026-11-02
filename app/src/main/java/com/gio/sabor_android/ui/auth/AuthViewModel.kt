package com.gio.sabor_android.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gio.sabor_android.data.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null
        repository.signInWithEmail(
            email = email,
            pass = pass,
            onSuccess = {
                isLoading = false
                onSuccess()
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    fun register(email: String, pass: String, onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null
        repository.signUpWithEmail(
            email = email,
            pass = pass,
            onSuccess = {
                isLoading = false
                onSuccess()
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }
}